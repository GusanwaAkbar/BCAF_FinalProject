package coid.bcafinance.mgaspringfinalexam.controller;



import coid.bcafinance.mgaspringfinalexam.model.DataRekeningKoran;
import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import coid.bcafinance.mgaspringfinalexam.service.DataRekeningKoranService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.opencsv.CSVReader;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;



import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;


@Controller
@RequestMapping("/RekeningKoran")
@Validated

public class DataRekeningKoranController {

    @Autowired
    private DataRekeningKoranService dataRekeningKoranService;

    // Other mappings...


    @PostMapping("/{rekeningKoranId}/upload-csv")
    public ResponseEntity<String> uploadCSV(@PathVariable("rekeningKoranId") Long rekeningKoranId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please upload a CSV file", HttpStatus.BAD_REQUEST);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(br);
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();

            String[] headers = csvReader.readNext();
            int nominalIndex = -1;
            int deskripsiIndex = -1;
            int verifikasiIndex = -1;
            int checker1Index = -1;
            int checker2Index = -1;

            // Find column indices by header names
            for (int i = 0; i < headers.length; i++) {
                if ("nominal".equalsIgnoreCase(headers[i])) {
                    nominalIndex = i;
                } else if ("deskripsi".equalsIgnoreCase(headers[i])) {
                    deskripsiIndex = i;
                } else if ("verifikasi".equalsIgnoreCase(headers[i])) {
                    verifikasiIndex = i;
                } else if ("checker1".equalsIgnoreCase(headers[i])) {
                    checker1Index = i;
                } else if ("checker2".equalsIgnoreCase(headers[i])) {
                    checker2Index = i;
                }
            }

            if (nominalIndex == -1 || deskripsiIndex == -1) {
                return new ResponseEntity<>("CSV file is missing required columns", HttpStatus.BAD_REQUEST);
            }

            List<DataRekeningKoran> dataList = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                double nominal = Double.parseDouble(line[nominalIndex]);
                String deskripsi = line[deskripsiIndex];
                String verifikasi = "tidak otomatis";
                boolean checker1 = false; // Default value
                boolean checker2 = false; // Default value

                //can use either checker column or not
                if (checker1Index != -1 && line.length > checker1Index && !line[checker1Index].isEmpty()) {
                    checker1 = Boolean.parseBoolean(line[checker1Index]);
                }

                if (checker2Index != -1 && line.length > checker2Index && !line[checker2Index].isEmpty()) {
                    checker2 = Boolean.parseBoolean(line[checker2Index]);
                }

                // Create DataRekeningKoran object
                DataRekeningKoran dataRekeningKoran = new DataRekeningKoran();
                dataRekeningKoran.setNominal(nominal);
                dataRekeningKoran.setDeskripsi(deskripsi);
                dataRekeningKoran.setVerifikasi(verifikasi);
                dataRekeningKoran.setChecker1(checker1);
                dataRekeningKoran.setChecker2(checker2);

                // Set the RekeningKoran with the provided ID
                Optional<RekeningKoran> rekeningKoran = dataRekeningKoranService.getRekeningKoranById(rekeningKoranId);
                if (rekeningKoran.isPresent()) {
                    dataRekeningKoran.setRekeningKoran(rekeningKoran.get());
                    dataRekeningKoranService.saveOrUpdateDataRekeningKoran(dataRekeningKoran);
                    dataList.add(dataRekeningKoran); // Add data to the list
                } else {
                    return new ResponseEntity<>("RekeningKoran with ID " + rekeningKoranId + " not found", HttpStatus.NOT_FOUND);
                }
            }

            // Convert the list to array
            DataRekeningKoran[] dataArray = dataList.toArray(new DataRekeningKoran[dataList.size()]);

            // Convert array to JSON
            String jsonData = objectMapper.writeValueAsString(dataArray);

            // Send JSON data to Django
            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers2);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:8000/predict_csv", requestEntity, String.class);
            String predictedLabel = responseEntity.getBody();

            // Deserialize predicted label JSON array
            JsonNode jsonNode = objectMapper.readTree(predictedLabel);
            String[] predictedLabels = objectMapper.convertValue(jsonNode.get("predicted_labels"), String[].class);

            // Update verifikasi data in database with new data from predicted label
            for (int i = 0; i < dataArray.length && i < predictedLabels.length; i++) {
                dataArray[i].setVerifikasi(predictedLabels[i]);
                dataRekeningKoranService.saveOrUpdateDataRekeningKoran(dataArray[i]); // Save or update in the database
            }

            return new ResponseEntity<>("CSV file uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload CSV file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//
//            // Prepare JSON data to send to Django

//
//            // Save the predicted label to database
//            savePredictedLabel(rekeningKoranId, predictedLabel);
//
//
//            //prepare for django
//            private String prepareJSONDataForDjango(Long rekeningKoranId) {
//                // Retrieve DataRekeningKoran from the database based on rekeningKoranId
//                Optional<DataRekeningKoran> optionalDataRekeningKoran = dataRekeningKoranService.getDataRekeningKoranById(rekeningKoranId);
//
//                if (optionalDataRekeningKoran.isPresent()) {
//                    DataRekeningKoran dataRekeningKoran = optionalDataRekeningKoran.get();
//
//                    // Create a JSON object containing the required fields
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("nominal", dataRekeningKoran.getNominal());
//                    jsonObject.put("deskripsi", dataRekeningKoran.getDeskripsi());
//                    // Add other fields as required
//
//                    // Return the JSON string
//                    return jsonObject.toString();
//                } else {
//                    return ""; // Return empty JSON if DataRekeningKoran is not found
//                }
//            }


//            return new ResponseEntity<>("CSV file uploaded successfully", HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to upload CSV file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @PostMapping("/{rekeningKoranId}")
    public ResponseEntity<DataRekeningKoran> createDataRekeningKoran(@PathVariable("rekeningKoranId") Long rekeningKoranId, @Valid @RequestBody DataRekeningKoran dataRekeningKoran) throws JsonProcessingException {
        // Set the RekeningKoran for the DataRekeningKoran
        Optional<RekeningKoran> rekeningKoran = dataRekeningKoranService.getRekeningKoranById(rekeningKoranId);
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        if (rekeningKoran.isPresent()) {
            dataRekeningKoran.setRekeningKoran(rekeningKoran.get());

            // Serialize dataRekeningKoran to JSON
            //ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(dataRekeningKoran);

            // Prepare headers and request entity
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);

            // Send POST request to Django
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:8000/predict_single_data", requestEntity, String.class);
            String predictedLabel = responseEntity.getBody();

            // Deserialize predicted label JSON array
            JsonNode jsonNode = objectMapper.readTree(predictedLabel);
            String[] predictedLabels = objectMapper.convertValue(jsonNode.get("predicted_labels"), String[].class);

            String verifikasi = predictedLabels[0];
            dataRekeningKoran.setVerifikasi(verifikasi);

            // Save or update DataRekeningKoran
            DataRekeningKoran createdDataRekeningKoran = dataRekeningKoranService.saveOrUpdateDataRekeningKoran(dataRekeningKoran);

            return new ResponseEntity<>(createdDataRekeningKoran, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{rekeningKoranId}/delete/{dataRekeningKoranId}")
    public ResponseEntity<HttpStatus> deleteDataRekeningKoran(@PathVariable("rekeningKoranId") Long rekeningKoranId, @PathVariable("dataRekeningKoranId") Long dataRekeningKoranId) {
        try {
            // Check if the provided IDs exist
            Optional<RekeningKoran> rekeningKoranOptional = dataRekeningKoranService.getRekeningKoranById(rekeningKoranId);
            Optional<DataRekeningKoran> dataRekeningKoranOptional = dataRekeningKoranService.getDataRekeningKoranById(dataRekeningKoranId);

            if (rekeningKoranOptional.isPresent() && dataRekeningKoranOptional.isPresent()) {
                // If both IDs exist, proceed with deletion
                dataRekeningKoranService.deleteDataRekeningKoran(dataRekeningKoranId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // If either ID does not exist, return 404 Not Found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // If an error occurs during deletion, return 500 Internal Server Error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{rekeningKoranId}/edit/{dataRekeningKoranId}")
    public ResponseEntity<DataRekeningKoran> updateDataRekeningKoran(@PathVariable("rekeningKoranId") Long rekeningKoranId, @PathVariable("dataRekeningKoranId") Long dataRekeningKoranId, @Valid @RequestBody DataRekeningKoran updatedDataRekeningKoran) throws JsonProcessingException {
        // Check if the provided rekeningKoranId and dataRekeningKoranId exist
        Optional<RekeningKoran> rekeningKoranOptional = dataRekeningKoranService.getRekeningKoranById(rekeningKoranId);
        Optional<DataRekeningKoran> dataRekeningKoranOptional = dataRekeningKoranService.getDataRekeningKoranById(dataRekeningKoranId);

        if (rekeningKoranOptional.isPresent() && dataRekeningKoranOptional.isPresent()) {
            //initiate mapper and rest
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();

            // Get the existing DataRekeningKoran from the database
            DataRekeningKoran existingDataRekeningKoran = dataRekeningKoranOptional.get();

            // Update the fields of existingDataRekeningKoran with the fields from updatedDataRekeningKoran
            existingDataRekeningKoran.setDeskripsi(updatedDataRekeningKoran.getDeskripsi());
            existingDataRekeningKoran.setNominal(updatedDataRekeningKoran.getNominal());
            // Update other fields similarly...

            // Serialize updatedDataRekeningKoran to JSON
            String jsonData = objectMapper.writeValueAsString(updatedDataRekeningKoran);

            // Prepare headers and request entity
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);

            // Send POST request to Django for prediction
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:8000/predict_single_data", requestEntity, String.class);
            String predictedLabel = responseEntity.getBody();

            // Deserialize predicted label JSON array
            JsonNode jsonNode = objectMapper.readTree(predictedLabel);
            String[] predictedLabels = objectMapper.convertValue(jsonNode.get("predicted_labels"), String[].class);

            String verifikasi = predictedLabels[0];

            // Update the 'verifikasi' field of existingDataRekeningKoran
            existingDataRekeningKoran.setVerifikasi(verifikasi);

            // Save the updated DataRekeningKoran
            DataRekeningKoran updatedDataRekeningKoranEntity = dataRekeningKoranService.saveOrUpdateDataRekeningKoran(existingDataRekeningKoran);

            // Return the updated DataRekeningKoran entity in the response
            return new ResponseEntity<>(updatedDataRekeningKoranEntity, HttpStatus.OK);
        } else {
            // If the rekeningKoranId or dataRekeningKoranId is not found, return 404 Not Found status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{rekeningKoranId}/editVerifikasi/{dataRekeningKoranId}")
    public ResponseEntity<DataRekeningKoran> updateVerifikasi(
            @PathVariable Long rekeningKoranId,
            @PathVariable Long dataRekeningKoranId,
            @RequestBody Map<String, String> requestBody) {

        try {
            // Fetch the existing DataRekenicleangKoran entity from the service layer
            DataRekeningKoran existingDataRekeningKoran = dataRekeningKoranService.getDataRekeningKoranById(dataRekeningKoranId)
                    .orElseThrow(() -> new NoSuchElementException());

            // Update the "verifikasi" field with the value from the request body
            String verifikasi = requestBody.get("verifikasi");
            existingDataRekeningKoran.setVerifikasi(verifikasi);

            // Save the updated entity back to the database through the service layer
            DataRekeningKoran updatedEntity = dataRekeningKoranService.saveOrUpdateDataRekeningKoran(existingDataRekeningKoran);
            

            // Return response with updated entity
            return ResponseEntity.ok(updatedEntity);
        } catch (NoSuchElementException e) {
            // Let the global exception handler handle the exception
            throw e;
        }
    }

    @PutMapping("/{rekeningKoranId}/editChecker1/{dataRekeningKoranId}")
    public ResponseEntity<DataRekeningKoran> updateChecker1(
            @PathVariable Long rekeningKoranId,
            @PathVariable Long dataRekeningKoranId,
            @RequestBody Map<String, Boolean> requestBody) {

        try {
            // Fetch the existing DataRekeningKoran entity from the service layer
            DataRekeningKoran existingDataRekeningKoran = dataRekeningKoranService.getDataRekeningKoranById(dataRekeningKoranId)
                    .orElseThrow(() -> new NoSuchElementException());

            // Update the "checker1" field with the value from the request body
            Boolean checker1Value = requestBody.get("checker1");
            if (checker1Value != null) {
                existingDataRekeningKoran.setChecker1(checker1Value);
            } else {
                // Handle the case where "checker1" is not present in the request body
                // You may throw an exception, return an error response, or handle it as per your requirements
                // For now, let's assume setting checker1 to false if not provided
                existingDataRekeningKoran.setChecker1(false);
            }

            // Save the updated entity back to the database through the service layer
            DataRekeningKoran updatedEntity = dataRekeningKoranService.saveOrUpdateDataRekeningKoran(existingDataRekeningKoran);

            // Return response with updated entity
            return ResponseEntity.ok(updatedEntity);
        } catch (NoSuchElementException e) {
            // Let the global exception handler handle the exception
            throw e;
        }
    }


    @PutMapping("/{rekeningKoranId}/editChecker2/{dataRekeningKoranId}")
    public ResponseEntity<DataRekeningKoran> updateChecker2(
            @PathVariable Long rekeningKoranId,
            @PathVariable Long dataRekeningKoranId,
            @RequestBody Map<String, Boolean> requestBody) {

        try {
            // Fetch the existing DataRekeningKoran entity from the service layer
            DataRekeningKoran existingDataRekeningKoran = dataRekeningKoranService.getDataRekeningKoranById(dataRekeningKoranId)
                    .orElseThrow(() -> new NoSuchElementException());

            // Update the "checker2" field with the value from the request body
            Boolean checker2Value = requestBody.get("checker2");
            if (checker2Value != null) {
                existingDataRekeningKoran.setChecker2(checker2Value);
            } else {
                // Handle the case where "checker2" is not present in the request body
                // You may throw an exception, return an error response, or handle it as per your requirements
                // For now, let's assume setting checker2 to false if not provided
                existingDataRekeningKoran.setChecker2(false);
            }

            // Save the updated entity back to the database through the service layer
            DataRekeningKoran updatedEntity = dataRekeningKoranService.saveOrUpdateDataRekeningKoran(existingDataRekeningKoran);

            // Return response with updated entity
            return ResponseEntity.ok(updatedEntity);
        } catch (NoSuchElementException e) {
            // Let the global exception handler handle the exception
            throw e;
        }
    }






}



