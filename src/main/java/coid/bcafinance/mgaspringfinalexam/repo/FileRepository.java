package coid.bcafinance.mgaspringfinalexam.repo;

import coid.bcafinance.mgaspringfinalexam.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}

