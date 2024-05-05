package coid.bcafinance.mgaspringfinalexam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class DataRekeningKoran {

    //data atribut
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double nominal;

    private String deskripsi;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'Belum Verifikasi'")
    private String verifikasi;

    @Column(columnDefinition = "bit default 0")
    private boolean checker1;

    @Column(columnDefinition = "bit default 0")
    private boolean checker2;


    //audit trail
    @Column(nullable = true, updatable = false)
    @CreationTimestamp
    private Date created;


    @Column(nullable = true)
    @UpdateTimestamp
    private Date updated;

    @Column(nullable = true, columnDefinition = "varchar(255) default 'admin'")
    private String createdBy;

    @Column(nullable = true, columnDefinition = "varchar(255) default 'admin'")
    private String updatedBy;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "rekening_koran_id")// Indicates that this is the non-owning side of the relationship
    private RekeningKoran rekeningKoran;




    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


// IF WANT TO NOT SHOW DATA REKENING KORAN
//    @ManyToOne
//    @JoinColumn(name = "rekening_koran_id")
//    private RekeningKoran rekeningKoran;




    // Constructors, getters, and setters

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getVerifikasi() {
        return verifikasi;
    }

    public void setVerifikasi(String verifikasi) {
        this.verifikasi = verifikasi;
    }

    public boolean isChecker1() {
        return checker1;
    }

    public void setChecker1(boolean checker1) {
        this.checker1 = checker1;
    }

    public boolean isChecker2() {
        return checker2;
    }

    public void setChecker2(boolean checker2) {
        this.checker2 = checker2;
    }

    public RekeningKoran getRekeningKoran() {
        return rekeningKoran;
    }

    public void setRekeningKoran(RekeningKoran rekeningKoran) {
        this.rekeningKoran = rekeningKoran;
    }
}
