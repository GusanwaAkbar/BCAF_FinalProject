// DataRekeningKoranService.java
package coid.bcafinance.mgaspringfinalexam.service;

import coid.bcafinance.mgaspringfinalexam.model.DataRekeningKoran;
import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import coid.bcafinance.mgaspringfinalexam.repo.DataRekeningKoranRepository;
import coid.bcafinance.mgaspringfinalexam.repo.RekeningKoranRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

}
