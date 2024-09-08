package com.viennalife.checkbin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardDetailRepository extends JpaRepository<CreditCartDetail, Integer> {

	@Query("SELECT ccd FROM CreditCartDetail ccd WHERE ccd.tckn IS NOT NULL")
	Page<CreditCartDetail> findTcknsWithNonNull(Pageable pageable);

}
