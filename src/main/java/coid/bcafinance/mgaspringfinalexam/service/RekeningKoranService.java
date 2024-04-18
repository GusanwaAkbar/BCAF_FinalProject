package coid.bcafinance.mgaspringfinalexam.service;// RekeningKoranService.java

import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import coid.bcafinance.mgaspringfinalexam.model.User;
import coid.bcafinance.mgaspringfinalexam.repo.RekeningKoranRepository;
import coid.bcafinance.mgaspringfinalexam.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class RekeningKoranService {



    @Autowired
    private RekeningKoranRepository rekeningKoranRepository;

    @Autowired
    private UserRepo userRepository;


    public Page<RekeningKoran> getAllRekeningKorans(Pageable pageable) {
        return rekeningKoranRepository.findAll(pageable);
    }

    public Optional<RekeningKoran> getRekeningKoranById(Long id) {
        return rekeningKoranRepository.findById(id);
    }

    @Transactional
    public RekeningKoran saveOrUpdateRekeningKoran(RekeningKoran rekeningKoran) {
        // Retrieve the currently authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Retrieve the namaLengkap by username
        Optional<String> namaLengkapOptional = userRepository.findNamaLengkapByUsername(username);
        if (namaLengkapOptional.isEmpty()) {

        }
        String namaLengkap = namaLengkapOptional.get();

        // Set createdBy and updatedBy fields
        rekeningKoran.setCreatedBy(namaLengkap);
        rekeningKoran.setUpdatedBy(namaLengkap);

        // Save or update the RekeningKoran entity
        return rekeningKoranRepository.save(rekeningKoran);
    }

    public void deleteRekeningKoran(Long id) {
        rekeningKoranRepository.deleteById(id);
    }
}
