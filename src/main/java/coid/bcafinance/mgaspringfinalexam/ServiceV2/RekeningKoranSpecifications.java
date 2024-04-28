package coid.bcafinance.mgaspringfinalexam.ServiceV2;

import coid.bcafinance.mgaspringfinalexam.model.RekeningKoran;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class RekeningKoranSpecifications {

    public static Specification<RekeningKoran> hasNamaRekeningKoran(String namaRekeningKoran) {
        return (root, query, criteriaBuilder) -> {
            if (namaRekeningKoran == null) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("namaRekeningKoran")), "%" + namaRekeningKoran.toLowerCase() + "%");
        };
    }
}
