package coid.bcafinance.mgaspringfinalexam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class DataRekeningKoran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double nominal;

    private String deskripsi;

    private String verifikasi;

    @Column(columnDefinition = "boolean default false")
    private boolean checker1;

    @Column(columnDefinition = "boolean default false")
    private boolean checker2;

    // IF WANT TO NOT SHOW DATA REKENING KORAN
//    @ManyToOne
//    @JoinColumn(name = "rekening_koran_id")
//    private RekeningKoran rekeningKoran;

    @ManyToOne
    @JoinColumn(name = "rekening_koran_id")
    @JsonBackReference // Indicates that this is the non-owning side of the relationship
    private RekeningKoran rekeningKoran;


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
