package coid.bcafinance.mgaspringfinalexam.repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;

@Repository
public interface RekeningKoranRepository extends JpaRepository<RekeningKoran, Long> {

    Page<RekeningKoran> findByNamaRekeningKoranContaining(String namaRekeningKoran, Pageable pageable);

}

