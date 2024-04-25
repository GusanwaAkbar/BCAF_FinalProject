package coid.bcafinance.mgaspringfinalexam.controller;



import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import coid.bcafinance.mgaspringfinalexam.service.RekeningKoranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;

@Controller
@RequestMapping("/rekening-koran")
@Validated
public class RekeningKoranController {

    @Autowired
    private RekeningKoranService rekeningKoranService;

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @GetMapping("/")
    public ResponseEntity<Page<RekeningKoran>> getAllRekeningKorans(Pageable pageable) {
        Page<RekeningKoran> rekeningKorans = rekeningKoranService.getAllRekeningKorans(pageable);
        return new ResponseEntity<>(rekeningKorans, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RekeningKoran> getRekeningKoranById(@PathVariable @Min(1) Long id) {
        Optional<RekeningKoran> rekeningKoran = rekeningKoranService.getRekeningKoranById(id);
        return rekeningKoran.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public ResponseEntity<RekeningKoran> createRekeningKoran(@Valid @RequestBody RekeningKoran rekeningKoran) {
        RekeningKoran createdRekeningKoran = rekeningKoranService.saveRekeningKoran(rekeningKoran);
        return new ResponseEntity<>(createdRekeningKoran, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RekeningKoran> updateRekeningKoran(@PathVariable @Min(1) Long id, @Valid @RequestBody RekeningKoran rekeningKoran) {
        Optional<RekeningKoran> existingRekeningKoran = rekeningKoranService.getRekeningKoranById(id);
        if (existingRekeningKoran.isPresent()) {
            rekeningKoran.setId(id);
            RekeningKoran updatedRekeningKoran = rekeningKoranService.updateRekeningKoran(rekeningKoran);
            return new ResponseEntity<>(updatedRekeningKoran, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRekeningKoran(@PathVariable @Min(1) Long id) {
        try {
            rekeningKoranService.deleteRekeningKoran(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
