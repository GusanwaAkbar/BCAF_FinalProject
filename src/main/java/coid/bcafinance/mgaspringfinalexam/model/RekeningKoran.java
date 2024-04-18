package coid.bcafinance.mgaspringfinalexam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class RekeningKoran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //data atribut
    private String namaRekeningKoran;

    @Column(columnDefinition = "bit default 0")
    private boolean isDone;


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

    @OneToMany(mappedBy = "rekeningKoran", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonIgnore // Ignore this property during JSON serialization
    @JsonManagedReference // Indicates that this is the owning side of the relationship
    private List<DataRekeningKoran> dataRekeningKoranList;





    //setter and getter
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }












    // Constructors, getters, and setters




    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaRekeningKoran() {
        return namaRekeningKoran;
    }

    public void setNamaRekeningKoran(String namaRekeningKoran) {
        this.namaRekeningKoran = namaRekeningKoran;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public List<DataRekeningKoran> getDataRekeningKoranList() {
        return dataRekeningKoranList;
    }

    public void setDataRekeningKoranList(List<DataRekeningKoran> dataRekeningKoranList) {
        this.dataRekeningKoranList = dataRekeningKoranList;
    }
}
