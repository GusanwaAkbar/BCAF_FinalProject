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
import org.springframework.beans.factory.annotation.Value;
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


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.*;
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
    public ResponseEntity<Object> uploadCSV(@PathVariable Long rekeningKoranId, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a CSV file");
        }

        try {
            return dataRekeningKoranService.processCSVFile(rekeningKoranId, file, request);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RekeningKoran with ID " + rekeningKoranId + " not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload CSV file: " + e.getMessage());
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
    public ResponseEntity<?> createDataRekeningKoran(@PathVariable Long rekeningKoranId, @Valid @RequestBody DataRekeningKoran dataRekeningKoran) {
        try {
            DataRekeningKoran createdDataRekeningKoran = dataRekeningKoranService.createDataRekeningKoran(rekeningKoranId, dataRekeningKoran);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDataRekeningKoran);
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Data Rekening Koran: " + ex.getMessage());
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



