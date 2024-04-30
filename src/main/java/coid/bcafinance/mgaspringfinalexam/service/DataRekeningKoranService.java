// DataRekeningKoranService.java
package coid.bcafinance.mgaspringfinalexam.service;

import coid.bcafinance.mgaspringfinalexam.configuration.OtherConfig;
import coid.bcafinance.mgaspringfinalexam.handler.RequestCapture;
import coid.bcafinance.mgaspringfinalexam.handler.ResponseHandler;
import coid.bcafinance.mgaspringfinalexam.model.DataRekeningKoran;
import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import coid.bcafinance.mgaspringfinalexam.repo.DataRekeningKoranRepository;
import coid.bcafinance.mgaspringfinalexam.repo.RekeningKoranRepository;
import coid.bcafinance.mgaspringfinalexam.util.LoggingFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DataRekeningKoranService {

    @Autowired
    private DataRekeningKoranRepository dataRekeningKoranRepository;


    @Autowired
    private RekeningKoranRepository rekeningKoranRepository;

    @Value("${django.service.url}")
    private String djangoServiceUrl;

    public Page<DataRekeningKoran> getAllDataRekeningKorans(Pageable pageable) {
        return dataRekeningKoranRepository.findAll(pageable);
    }

    public Optional<DataRekeningKoran> getDataRekeningKoranById(Long id) {
        return dataRekeningKoranRepository.findById(id);
    }

    public DataRekeningKoran saveOrUpdateDataRekeningKoran(DataRekeningKoran dataRekeningKoran) {
        return dataRekeningKoranRepository.save(dataRekeningKoran);
    }

    public void deleteDataRekeningKoran(Long id) {
        dataRekeningKoranRepository.deleteById(id);
    }

    public Optional<RekeningKoran> getRekeningKoranById(Long rekeningKoranId) {
        return rekeningKoranRepository.findById(rekeningKoranId);
    }

    public Page<DataRekeningKoran> getAllDataRekeningKoransByRekeningKoranId(Long rekeningKoranId, Pageable pageable) {
        return dataRekeningKoranRepository.findByRekeningKoranId(rekeningKoranId, pageable);
    }


    public List<DataRekeningKoran> getAllDataRekeningKoransByRekeningKoranId(Long rekeningKoranId) {
        return dataRekeningKoranRepository.findByRekeningKoranId(rekeningKoranId);
    }

    public ResponseEntity<Object> processCSVFile(Long rekeningKoranId, MultipartFile file, HttpServletRequest request) throws Exception {
        if (!file.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                    CSVReader csvReader = new CSVReader(br);
                    ObjectMapper objectMapper = new ObjectMapper();
                    RestTemplate restTemplate = new RestTemplate();

                    String[] headers = csvReader.readNext();
                    if (headers == null) {
                        return new ResponseHandler().generateResponse("CSV FILE EMPTY OR MISSING", HttpStatus.INTERNAL_SERVER_ERROR, null, "FERGS001", request);
                    }
                    // Extract column indices from headers
                    int nominalIndex = findColumnIndex(headers, "nominal");
                    int deskripsiIndex = findColumnIndex(headers, "deskripsi");
                    if (nominalIndex == -1 || deskripsiIndex == -1) {

                        return new ResponseHandler().generateResponse("MISSING REQUIRED COLUMNS 'nominal' AND 'deskripsi' ", HttpStatus.INTERNAL_SERVER_ERROR, null, "FERGS001", request);
                    }

                    List<DataRekeningKoran> dataList = new ArrayList<>();
                    String[] line;
                    while ((line = csvReader.readNext()) != null) {
                        DataRekeningKoran data = createDataRekeningKoran(line, nominalIndex, deskripsiIndex);
                        Optional<RekeningKoran> rekeningKoran = rekeningKoranRepository.findById(rekeningKoranId);
                        rekeningKoran.ifPresent(data::setRekeningKoran);
                        dataList.add(data);
                    }

                    // Convert the list to JSON and send it to Django
                    String jsonData = objectMapper.writeValueAsString(dataList);
                    HttpHeaders headers2 = new HttpHeaders();
                    headers2.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers2);
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(djangoServiceUrl, requestEntity, String.class);

                    // Handle the response from Django
                    if (responseEntity.getStatusCode().is2xxSuccessful()) {
                        String predictedLabel = responseEntity.getBody();
                        JsonNode jsonNode = objectMapper.readTree(predictedLabel);
                        String[] predictedLabels = objectMapper.convertValue(jsonNode.get("predicted_labels"), String[].class);

                        // Update verifikasi data in database with new data from predicted label
                        for (int i = 0; i < dataList.size() && i < predictedLabels.length; i++) {
                            dataList.get(i).setVerifikasi(predictedLabels[i]);
                            dataRekeningKoranRepository.save(dataList.get(i)); // Save or update in the database
                        }
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"CSV file processed and data updated successfully\"}");
                    } else {
                        throw new RuntimeException("Failed to receive valid response from Django service");
                    }
                }

            catch (Exception e) {
                logException("processCSVFILE", e, request);
                return new ResponseHandler().generateResponse("Error Prosess CSV FILE", HttpStatus.INTERNAL_SERVER_ERROR, null, "FERGS001", request);
            }
        }

        return new ResponseHandler().generateResponse("FILE CSV IS EMPTY", HttpStatus.BAD_REQUEST, null, "FERGS001", request);
    }

    public ResponseEntity<Object> findAllByRekeningKoranId(Long rekeningKoranId, Pageable pageable, HttpServletRequest request) {
        try {
            Page<DataRekeningKoran> dataPage = dataRekeningKoranRepository.findByRekeningKoranId(rekeningKoranId, pageable);
            if (dataPage.hasContent()) {
                return new ResponseHandler().generateResponse("Data fetched successfully", HttpStatus.OK, dataPage, null, request);
            } else {
                return new ResponseHandler().generateResponse("No Data Found", HttpStatus.NOT_FOUND, null, "FERGS003", request);
            }
        } catch (Exception e) {
            logException("Find All DataRekeningKoran By ID", e, request);
            return new ResponseHandler().generateResponse("Error fetching data", HttpStatus.INTERNAL_SERVER_ERROR, null, "FERGS004", request);
        }
    }

    private int findColumnIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (columnName.equalsIgnoreCase(headers[i])) {
                return i;
            }
        }
        return -1;
    }

    public DataRekeningKoran createDataRekeningKoran(Long rekeningKoranId, DataRekeningKoran dataRekeningKoran) {
        Optional<RekeningKoran> rekeningKoranOpt = rekeningKoranRepository.findById(rekeningKoranId);
        if (!rekeningKoranOpt.isPresent()) {

        }
        dataRekeningKoran.setRekeningKoran(rekeningKoranOpt.get());
        dataRekeningKoranRepository.save(dataRekeningKoran);

        return dataRekeningKoran;
    }

    private DataRekeningKoran createDataRekeningKoran(String[] line, int nominalIndex, int deskripsiIndex) {
        double nominal = Double.parseDouble(line[nominalIndex]);
        String deskripsi = line[deskripsiIndex];
        DataRekeningKoran dataRekeningKoran = new DataRekeningKoran();
        dataRekeningKoran.setNominal(nominal);
        dataRekeningKoran.setDeskripsi(deskripsi);
        return dataRekeningKoran;
    }

    private void logException(String methodName, Exception e, HttpServletRequest request) {
        String[] strExceptionArr = new String[2];
        strExceptionArr[0] = "Error in " + methodName + " at " + new Date();
        strExceptionArr[1] = methodName + "(...) LINE " + new Throwable().getStackTrace()[0].getLineNumber() + " " + RequestCapture.allRequest(request);
        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
    }
}
