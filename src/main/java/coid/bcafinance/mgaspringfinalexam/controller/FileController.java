package coid.bcafinance.mgaspringfinalexam.controller;

import coid.bcafinance.mgaspringfinalexam.model.FileEntity;
import coid.bcafinance.mgaspringfinalexam.service.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping
    public List<FileEntity> getAllFiles() {
        return fileService.getAllFiles();
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public ResponseEntity<FileEntity> getFileById(@PathVariable Long id) {
        Optional<FileEntity> file = fileService.getFileById(id);
        return file.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/upload")
    public ResponseEntity<FileEntity> uploadFile(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("namaFile") String namaFile,
                                                 @RequestParam("direktoriFile") String direktoriFile,
                                                 @RequestParam("checker1") String checker1,
                                                 @RequestParam("checker2") String checker2) {
        try {
            FileEntity createdFile = fileService.uploadFile(file, namaFile, direktoriFile, checker1, checker2);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFile);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}")
    public ResponseEntity<FileEntity> updateFile(@PathVariable Long id, @RequestBody FileEntity updatedFile) {
        FileEntity file = fileService.updateFile(id, updatedFile);
        if (file != null) {
            return ResponseEntity.ok().body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        boolean deleted = fileService.deleteFile(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/json/{id}")
    public ResponseEntity<String> getFileJsonById(@PathVariable Long id) {
        // Mengambil entitas file berdasarkan ID
        FileEntity fileEntity = fileService.getFileById(id).orElse(null);

        if (fileEntity != null) {
            // Mengubah konten file CSV menjadi objek JSON (disini kita asumsikan bahwa konten CSV dapat diubah menjadi JSON dengan mudah)

            byte[] tmp = fileEntity.getFileContent();
            String csvContent = new String(tmp, StandardCharsets.UTF_8);
            String jsonContent = convertCsvToJson(csvContent);



            return ResponseEntity.ok().body(jsonContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Metode untuk mengubah konten CSV menjadi JSON
    public static String convertCsvToJson(String csvContent) {
        // Inisialisasi objek mapper CSV dan JSON
        CsvMapper csvMapper = new CsvMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Membaca baris-baris CSV menjadi list of maps
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            List<Object> objects = csvMapper.readerFor(Map.class).with(schema).readValues(csvContent).readAll();

            // Mengkonversi list of maps menjadi JSON
            return objectMapper.writeValueAsString(objects);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
