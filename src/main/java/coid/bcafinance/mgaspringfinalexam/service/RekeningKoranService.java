package coid.bcafinance.mgaspringfinalexam.service;// RekeningKoranService.java

import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import coid.bcafinance.mgaspringfinalexam.repo.RekeningKoranRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RekeningKoranService {

    @Autowired
    private RekeningKoranRepository rekeningKoranRepository;

    public Page<RekeningKoran> getAllRekeningKorans(Pageable pageable) {
        return rekeningKoranRepository.findAll(pageable);
    }

    public Optional<RekeningKoran> getRekeningKoranById(Long id) {
        return rekeningKoranRepository.findById(id);
    }

    public RekeningKoran saveOrUpdateRekeningKoran(RekeningKoran rekeningKoran) {
        return rekeningKoranRepository.save(rekeningKoran);
    }

    public void deleteRekeningKoran(Long id) {
        rekeningKoranRepository.deleteById(id);
    }
}
