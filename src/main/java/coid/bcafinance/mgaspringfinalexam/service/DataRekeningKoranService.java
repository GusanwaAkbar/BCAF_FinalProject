// DataRekeningKoranService.java
package coid.bcafinance.mgaspringfinalexam.service;

import coid.bcafinance.mgaspringfinalexam.model.DataRekeningKoran;
import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import coid.bcafinance.mgaspringfinalexam.repo.DataRekeningKoranRepository;
import coid.bcafinance.mgaspringfinalexam.repo.RekeningKoranRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataRekeningKoranService {

    @Autowired
    private DataRekeningKoranRepository dataRekeningKoranRepository;



    @Autowired
    private RekeningKoranRepository rekeningKoranRepository;

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

    public String processCSVFile(Long rekeningKoranId, MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CSVReader csvReader = new CSVReader(br);
                ObjectMapper objectMapper = new ObjectMapper();
                RestTemplate restTemplate = new RestTemplate();

                String[] headers = csvReader.readNext();
                if (headers == null) {
                    throw new IllegalArgumentException("CSV file is empty or invalid");
                }
                // Extract column indices from headers
                int nominalIndex = findColumnIndex(headers, "nominal");
                int deskripsiIndex = findColumnIndex(headers, "deskripsi");
                if (nominalIndex == -1 || deskripsiIndex == -1) {
                    throw new IllegalArgumentException("CSV file is missing required columns");
                }

                List<DataRekeningKoran> dataList = new ArrayList<>();
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    DataRekeningKoran data = createDataRekeningKoran(line, nominalIndex, deskripsiIndex);
                    Optional<RekeningKoran> rekeningKoran = rekeningKoranRepository.findById(rekeningKoranId);
                    rekeningKoran.ifPresent(data::setRekeningKoran);
                    dataRekeningKoranRepository.save(data);
                    dataList.add(data);
                }

                // Further processing like sending data to Django server would be similar to your previous code
                return "CSV file processed successfully";
            }
        }
        throw new IllegalArgumentException("Provided file is empty");
    }

    private int findColumnIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (columnName.equalsIgnoreCase(headers[i])) {
                return i;
            }
        }
        return -1;
    }

    private DataRekeningKoran createDataRekeningKoran(String[] line, int nominalIndex, int deskripsiIndex) {
        double nominal = Double.parseDouble(line[nominalIndex]);
        String deskripsi = line[deskripsiIndex];
        DataRekeningKoran dataRekeningKoran = new DataRekeningKoran();
        dataRekeningKoran.setNominal(nominal);
        dataRekeningKoran.setDeskripsi(deskripsi);
        return dataRekeningKoran;
    }

}
