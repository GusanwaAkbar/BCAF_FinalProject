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
    public RekeningKoran saveRekeningKoran(RekeningKoran rekeningKoran) {
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


    @Transactional
    public RekeningKoran updateRekeningKoran(RekeningKoran rekeningKoran) {
        // Retrieve the currently authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Retrieve the namaLengkap by username
        Optional<String> namaLengkapOptional = userRepository.findNamaLengkapByUsername(username);
        if (namaLengkapOptional.isEmpty()) {
            // Handle the case where namaLengkap is not found
            // You can throw an exception, log an error, or handle it based on your application's requirements
        }
        String namaLengkap = namaLengkapOptional.get();

        // Set createdBy and updatedBy fields
        rekeningKoran.setCreatedBy(namaLengkap);
        rekeningKoran.setUpdatedBy(namaLengkap);

        // Retrieve the existing RekeningKoran entity from the database
        Optional<RekeningKoran> existingRekeningKoranOptional = rekeningKoranRepository.findById(rekeningKoran.getId());
        if (existingRekeningKoranOptional.isPresent()) {
            RekeningKoran existingRekeningKoran = existingRekeningKoranOptional.get();

            // Update the fields of the existing entity with the values from the updated entity
            existingRekeningKoran.setNamaRekeningKoran(rekeningKoran.getNamaRekeningKoran());
            // Update other fields as needed

            // Save the updated entity
            return rekeningKoranRepository.save(existingRekeningKoran);
        } else {
            // If the entity with the given ID does not exist, you can handle it according to your application's requirements
            // For example, you can throw an exception or return null
            return null;
        }
    }


    public void deleteRekeningKoran(Long id) {
        rekeningKoranRepository.deleteById(id);
    }
}
