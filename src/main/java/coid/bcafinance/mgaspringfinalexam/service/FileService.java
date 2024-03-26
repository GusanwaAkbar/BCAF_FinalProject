package coid.bcafinance.mgaspringfinalexam.service;

import coid.bcafinance.mgaspringfinalexam.model.FileEntity;
import coid.bcafinance.mgaspringfinalexam.repo.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public Optional<FileEntity> getFileById(Long id) {
        return fileRepository.findById(id);
    }

    public FileEntity uploadFile(MultipartFile file, String namaFile, String direktoriFile, String checker1, String checker2) throws IOException {
        byte[] fileBytes = file.getBytes();
        FileEntity fileEntity = new FileEntity();
        fileEntity.setNamaFile(namaFile);
        fileEntity.setDirektoriFile(direktoriFile);
        fileEntity.setChecker1(checker1);
        fileEntity.setChecker2(checker2);
        fileEntity.setFileContent(fileBytes);
        return fileRepository.save(fileEntity);
    }

    public FileEntity updateFile(Long id, FileEntity updatedFile) {
        if (fileRepository.existsById(id)) {
            updatedFile.setId(id);
            return fileRepository.save(updatedFile);
        } else {
            return null; // or throw an exception
        }
    }

    public boolean deleteFile(Long id) {
        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
