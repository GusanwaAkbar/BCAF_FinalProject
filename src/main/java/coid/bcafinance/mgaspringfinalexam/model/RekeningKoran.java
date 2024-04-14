package coid.bcafinance.mgaspringfinalexam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class RekeningKoran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaRekeningKoran;

    @Column( nullable = false, updatable = false)
    @CreationTimestamp
    private Date created;

    @Column(columnDefinition = "boolean default false")
    private boolean isDone;





    @OneToMany(mappedBy = "rekeningKoran", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonIgnore // Ignore this property during JSON serialization
    @JsonManagedReference // Indicates that this is the owning side of the relationship
    private List<DataRekeningKoran> dataRekeningKoranList;

    public RekeningKoran() {

    }
    public RekeningKoran(Long id) {
        this.id = id;
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
