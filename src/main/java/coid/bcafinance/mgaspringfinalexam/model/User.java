package coid.bcafinance.mgaspringfinalexam.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.PropertyValues;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUser")
@Table(name = "MstUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUser")
    private Long idUser;

    @Column(name = "Email",unique = false, nullable = false)
    private String email;

    @Column(name = "NoHp",unique = false, nullable = false)
    private String noHp;

    @Column(name = "Username",unique = true, nullable = false)
    private String username;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "NamaLengkap", nullable = false)
    private String namaLengkap;


    @Column(name = "TanggalLahir", nullable = false)
    private Date tanggalLahir = new Date();;

    @Column(name = "Alamat", nullable = false)
    private String alamat;

    @Transient
    private Integer umur;

    @Column(name = "Token")
    private String token;

    @Column(name = "Otp")  // New field for OTP
    private Integer otp;

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    @Column(name = "IsRegistered")
    private Boolean isRegistered;

    @Column(name = "LastOtpSentTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastOtpSentTime;



    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "IdUser"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )


    private Set<Role> roles = new HashSet<>();





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

    public User(Long idUser, String email, String noHp, String username, String password, String namaLengkap, Date tanggalLahir, String alamat, Integer umur, String token, Boolean isRegistered, Long createdBy, Date createdDate, Long modifiedBy, Date modifiedDate) {
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

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
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


    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public Date getLastOtpSentTime() {
        return lastOtpSentTime;
    }

    public void setLastOtpSentTime(Date lastOtpSentTime) {
        this.lastOtpSentTime = lastOtpSentTime;
    }
}

