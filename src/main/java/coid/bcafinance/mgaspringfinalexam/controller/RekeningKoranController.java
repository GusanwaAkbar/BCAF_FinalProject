package coid.bcafinance.mgaspringfinalexam.controller;



import coid.bcafinance.mgaspringfinalexam.ServiceV2.RekeningKoranServiceV2;
import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import coid.bcafinance.mgaspringfinalexam.service.DataRekeningKoranService;
import coid.bcafinance.mgaspringfinalexam.service.RekeningKoranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@RequestMapping("/rekening-koran")
@Validated
public class RekeningKoranController {

    @Autowired
    private RekeningKoranService rekeningKoranService;

    @Autowired
    private RekeningKoranServiceV2 rekeningKoranServiceV2;

    @Autowired
    private DataRekeningKoranService dataRekeningKoranService;


    @GetMapping("/")
    public ResponseEntity<?> getAllRekeningKorans(
            @RequestParam(required = false) String namaRekeningKoran,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, HttpServletRequest request) {

        return rekeningKoranService.getAllRekeningKorans(namaRekeningKoran, pageable, request);
    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllRekeningKoransNoPagination(
            @RequestParam(required = false) String namaRekeningKoran, HttpServletRequest request) {

        return rekeningKoranService.getAllRekeningKoransNoPagination(namaRekeningKoran, request);
    }





    //error req

    @GetMapping("/{id}")
    public ResponseEntity<?> getRekeningKoranById(@PathVariable @Min(1) Long id,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        return rekeningKoranService.getRekeningKoranById(id, page, size, request);
    }

    @GetMapping("all/{id}")
    public ResponseEntity<?> getRekeningKoranById(@PathVariable @Min(1) Long id, HttpServletRequest request) {
        return dataRekeningKoranService.findAllByRekeningKoranId(id, request);
    }


    //v2
    @GetMapping("/v2/{id}")
    public ResponseEntity<?> getAllDataRekeningKoranByRekeningKoranId(
            @PathVariable Long id,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {

        return dataRekeningKoranService.findAllByRekeningKoranId(id, pageable, request);
    }


    //error checkpoint

    @PostMapping("/")
    public ResponseEntity<Object> createRekeningKoran(@Valid @RequestBody RekeningKoran rekeningKoran, HttpServletRequest request) {



        return rekeningKoranService.saveRekeningKoran(rekeningKoran, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRekeningKoran(@PathVariable @Min(1) Long id, @Valid @RequestBody RekeningKoran rekeningKoran, HttpServletRequest request) {
        rekeningKoran.setId(id); // Ensure the ID is set based on the path variable.

        Optional<RekeningKoran> existingRekeningKoran = rekeningKoranService.getRekeningKoranById(id);
        if (existingRekeningKoran.isPresent()) {
            return rekeningKoranService.updateRekeningKoran(rekeningKoran, request);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRekeningKoran(@PathVariable @Min(1) Long id, HttpServletRequest request) {

        return  rekeningKoranService.deleteRekeningKoran(id, request);
    }



}
