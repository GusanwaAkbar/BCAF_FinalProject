package coid.bcafinance.mgaspringfinalexam.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "MstUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUser")
    private Long idUser;

    @Column(name = "Email",unique = true, nullable = false)
    private String email;

    @Column(name = "NoHp",unique = true, nullable = false)
    private String noHp;

    @Column(name = "Username",unique = true, nullable = false)
    private String username;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "NamaLengkap", nullable = false)
    private String namaLengkap;

    @Column(name = "TanggalLahir", nullable = false)
    private LocalDate tanggalLahir;

    @Column(name = "Alamat", nullable = false)
    private String alamat;

    @Transient
    private Integer umur;

    @Column(name = "Token")
    private String token;

    @Column(name = "IsRegistered")
    private Boolean isRegistered;

    /**
     Start Group Audit trails
     */
    @Column(name = "CreatedBy", nullable = false)
    private Long createdBy = 1L;

    @Column(name = "CreatedDate", nullable = false)
    private Date createdDate = new Date();

    @Column(name = "ModifiedBY")
    private Long modifiedBy;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public User(Long idUser, String email, String noHp, String username, String password, String namaLengkap, LocalDate tanggalLahir, String alamat, Integer umur, String token, Boolean isRegistered, Long createdBy, Date createdDate, Long modifiedBy, Date modifiedDate) {
        this.idUser = idUser;
        this.email = email;
        this.noHp = noHp;
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.umur = umur;
        this.token = token;
        this.isRegistered = isRegistered;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    public User() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Integer getUmur() {
        return umur;
    }

    public void setUmur(Integer umur) {
        this.umur = umur;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getRegistered() {
        return isRegistered;
    }

    public void setRegistered(Boolean registered) {
        isRegistered = registered;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}

