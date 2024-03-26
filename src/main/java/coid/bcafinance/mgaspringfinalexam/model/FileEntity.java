package coid.bcafinance.mgaspringfinalexam.model;

import javax.persistence.*;

@Entity
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaFile;
    private String direktoriFile;
    private String checker1;
    private String checker2;

    @Lob
    private byte[] fileContent; // Menyimpan konten file dalam bentuk byte[]

    // Constructors, getters, and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public void setNamaFile(String namaFile) {
        this.namaFile = namaFile;
    }

    public String getDirektoriFile() {
        return direktoriFile;
    }

    public void setDirektoriFile(String direktoriFile) {
        this.direktoriFile = direktoriFile;
    }

    public String getChecker1() {
        return checker1;
    }

    public void setChecker1(String checker1) {
        this.checker1 = checker1;
    }

    public String getChecker2() {
        return checker2;
    }

    public void setChecker2(String checker2) {
        this.checker2 = checker2;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
