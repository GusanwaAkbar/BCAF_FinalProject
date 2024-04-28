package coid.bcafinance.mgaspringfinalexam.RepositoryV2;

import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RekeningKoranRepositoryV2 extends PagingAndSortingRepository<RekeningKoran, Long>, JpaSpecificationExecutor<RekeningKoran> {



}
