package coid.bcafinance.mgaspringfinalexam.service;// RekeningKoranService.java

import coid.bcafinance.mgaspringfinalexam.configuration.OtherConfig;
import coid.bcafinance.mgaspringfinalexam.handler.RequestCapture;
import coid.bcafinance.mgaspringfinalexam.model.DataRekeningKoran;
import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import coid.bcafinance.mgaspringfinalexam.repo.RekeningKoranRepository;
import coid.bcafinance.mgaspringfinalexam.repo.UserRepo;
import coid.bcafinance.mgaspringfinalexam.util.LoggingFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import coid.bcafinance.mgaspringfinalexam.handler.ResponseHandler;

import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

@Service
public class RekeningKoranService {



    @Autowired
    private RekeningKoranRepository rekeningKoranRepository;

    @Autowired
    private UserRepo userRepository;



    public ResponseEntity<?> getAllRekeningKorans(String namaRekeningKoran, Pageable pageable, HttpServletRequest request) {
        try {
            Page<RekeningKoran> result;
            if (namaRekeningKoran != null && !namaRekeningKoran.trim().isEmpty()) {
                result = rekeningKoranRepository.findByNamaRekeningKoranContaining(namaRekeningKoran, pageable);
            } else {
                result = rekeningKoranRepository.findAll(pageable);
            }

            return new ResponseHandler().generateResponse("GET berhasil",
                    HttpStatus.OK,
                    result,
                    null, request);

            // Wrap the result in a ResponseEntity
        } catch (Exception e) {
            logException("getAllRekeningKorans", e, request);
            return new ResponseHandler().generateResponse("Error fetching data", HttpStatus.INTERNAL_SERVER_ERROR, null, "FERGS001", request);
        }
    }


    public ResponseEntity<?> getAllRekeningKoransNoPagination(String namaRekeningKoran, HttpServletRequest request) {
        try {
            List<RekeningKoran> result;
            if (namaRekeningKoran != null && !namaRekeningKoran.trim().isEmpty()) {
                result = rekeningKoranRepository.findByNamaRekeningKoranContaining(namaRekeningKoran);
            } else {
                result = rekeningKoranRepository.findAll();
            }

            return new ResponseHandler().generateResponse("GET berhasil",
                    HttpStatus.OK,
                    result,
                    null, request);

        } catch (Exception e) {
            logException("getAllRekeningKoransNoPagination", e, request);
            return new ResponseHandler().generateResponse("Error fetching data", HttpStatus.INTERNAL_SERVER_ERROR, null, "FERGS001", request);
        }
    }



    public ResponseEntity<Object> getRekeningKoranById(Long id, int page, int size, HttpServletRequest request) {
        try {
            Optional<RekeningKoran> rekeningKoranOptional = rekeningKoranRepository.findById(id);
            if (rekeningKoranOptional.isPresent()) {
                RekeningKoran rekeningKoran = rekeningKoranOptional.get();
                List<DataRekeningKoran> fullList = rekeningKoran.getDataRekeningKoranList();

                // Calculate pagination details
                int totalElements = fullList.size();
                int totalPages = (int) Math.ceil((double) totalElements / size);
                int start = Math.min(page * size, totalElements);
                int end = Math.min((page + 1) * size, totalElements);
                List<DataRekeningKoran> paginatedList = fullList.subList(start, end);

                // Build the response object with pagination details
                Map<String, Object> response = new HashMap<>();
                response.put("dataRekeningKoranList", paginatedList);
                response.put("pageable", Map.of(
                        "sort", Map.of("sorted", true, "unsorted", false, "empty", false),
                        "pageNumber", page,
                        "pageSize", size,
                        "offset", start,
                        "paged", true,
                        "unpaged", false
                ));
                response.put("totalPages", totalPages);
                response.put("totalElements", totalElements);
                response.put("last", page == totalPages - 1);
                response.put("first", page == 0);
                response.put("numberOfElements", paginatedList.size());
                response.put("size", size);
                response.put("number", page);
                response.put("sort", Map.of("sorted", true, "unsorted", false, "empty", false));
                response.put("empty", paginatedList.isEmpty());

                return new ResponseHandler().generateResponse("GET Rekening Koran by ID berhasil",
                        HttpStatus.OK,
                        response,
                        null, request);
            } else {
                return new ResponseHandler().generateResponse("Rekening Koran not found",
                        HttpStatus.NOT_FOUND,
                        null,
                        "Not Found", request);
            }
        } catch (Exception e) {
            logException("getRekeningKoranByIdWithPagination", e, request);
            return new ResponseHandler().generateResponse("Error fetching Rekening Koran by ID",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FERGS002",
                    request);
        }
    }




    public Optional<RekeningKoran> getRekeningKoranById(Long id) {
        return rekeningKoranRepository.findById(id);
    }




    @Transactional
    public ResponseEntity<Object> saveRekeningKoran(RekeningKoran rekeningKoran, HttpServletRequest request) {
        try {
            // Retrieve the currently authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Retrieve the full name by username
            Optional<String> namaLengkapOptional = userRepository.findNamaLengkapByUsername(username);
            if (!namaLengkapOptional.isPresent()) {
                // Handle the case where the user's full name could not be found
                return new ResponseHandler().generateResponse("USER FULL NAME NOT FOUND", HttpStatus.NOT_FOUND, null, "FERGS001", request);
            }

            String namaLengkap = namaLengkapOptional.get();

            // Set createdBy and updatedBy fields
            rekeningKoran.setCreatedBy(namaLengkap);
            rekeningKoran.setUpdatedBy(namaLengkap);

            // Save or update the RekeningKoran entity
            RekeningKoran savedRekeningKoran = rekeningKoranRepository.save(rekeningKoran);
            return new ResponseHandler().generateResponse("Rekening Koran saved successfully",
                    HttpStatus.CREATED,
                    savedRekeningKoran,
                    null, request);
        } catch (Exception e) {
            // Log the exception and return an appropriate response
            logException("saveRekeningKorans", e, request);
            return new ResponseHandler().generateResponse("Error saving Rekening Koran data", HttpStatus.INTERNAL_SERVER_ERROR, null, "FERGS001", request);
        }
    }



    @Transactional
    public ResponseEntity<Object> updateRekeningKoran(RekeningKoran rekeningKoran, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Optional<String> namaLengkapOptional = userRepository.findNamaLengkapByUsername(username);

            if (!namaLengkapOptional.isPresent()) {
                return new ResponseHandler().generateResponse("User full name not found", HttpStatus.NOT_FOUND, null, "FERGS002", request);
            }
            String namaLengkap = namaLengkapOptional.get();

            rekeningKoran.setUpdatedBy(namaLengkap);

            Optional<RekeningKoran> existingRekeningKoranOptional = rekeningKoranRepository.findById(rekeningKoran.getId());
            if (!existingRekeningKoranOptional.isPresent()) {
                return new ResponseHandler().generateResponse("Rekening Koran (parent) not found!", HttpStatus.NOT_FOUND, null, "FERGS001", request);
            }

            RekeningKoran existingRekeningKoran = existingRekeningKoranOptional.get();
            existingRekeningKoran.setNamaRekeningKoran(rekeningKoran.getNamaRekeningKoran());
            // Update other fields as necessary

            RekeningKoran updatedRekeningKoran = rekeningKoranRepository.save(existingRekeningKoran);
            return new ResponseHandler().generateResponse("Rekening Koran updated successfully",
                    HttpStatus.OK,
                    updatedRekeningKoran,
                    null, request);
        } catch (Exception e) {
            logException("Update RekeningKoran", e, request);
            return new ResponseHandler().generateResponse("Error updating Rekening Koran data", HttpStatus.INTERNAL_SERVER_ERROR, null, "FERGS001", request);
        }
    }



    public ResponseEntity<Object> deleteRekeningKoran(Long id, HttpServletRequest request) {
        try {
            Optional<RekeningKoran> existingRekeningKoran = rekeningKoranRepository.findById(id);
            if (!existingRekeningKoran.isPresent()) {
                // If the Rekening Koran does not exist, return a not found message
                return new ResponseHandler().generateResponse("Rekening Koran not found", HttpStatus.NOT_FOUND, null, "FERGS002", request);
            }

            // If found, delete the Rekening Koran
            rekeningKoranRepository.deleteById(id);
            return new ResponseHandler().generateResponse("Rekening Koran successfully deleted",
                    HttpStatus.OK,
                    null,
                    null, request);
        } catch (Exception e) {
            // Log the exception and return an appropriate response
            logException("Delete RekeningKoran", e, request);
            return new ResponseHandler().generateResponse("DELETE REKENING KORAN ERROR!", HttpStatus.INTERNAL_SERVER_ERROR, null, "FERGS001", request);
        }
    }



    //loging process method
    private void logException(String methodName, Exception e, HttpServletRequest request) {
        String[] strExceptionArr = new String[2];
        strExceptionArr[0] = "Error in " + methodName + " at " + new Date();
        strExceptionArr[1] = methodName + "(...) LINE " + new Throwable().getStackTrace()[0].getLineNumber() + " " + RequestCapture.allRequest(request);
        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
    }
}
