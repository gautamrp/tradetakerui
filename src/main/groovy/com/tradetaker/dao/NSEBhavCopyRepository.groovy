package com.tradetaker.dao

import com.tradetaker.entity.NSEBhavCopyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface NSEBhavCopyRepository extends JpaRepository<NSEBhavCopyEntity, Long> {
    @Query(value = "SELECT * FROM NSEBHAVCOPY WHERE EXPIRY_DT=:expiryDate ORDER BY EXPIRY_DT, SYMBOL, OPTION_TYP, OPEN_INT DESC", nativeQuery = true)
    List<NSEBhavCopyEntity> findByExpiryDate(@Param("expiryDate") Date expiryDate)

    @Query(value = "SELECT * FROM NSEBHAVCOPY  WHERE SYMBOL=:symbol AND EXPIRY_DT=:expiryDate ORDER BY EXPIRY_DT, SYMBOL, OPTION_TYP, OPEN_INT DESC", nativeQuery = true)
    List<NSEBhavCopyEntity> findBySymbolAndExpiryDate(@Param("symbol") String symbol, @Param("expiryDate") Date expiryDate)

    @Query(value = "SELECT * FROM NSEBHAVCOPY ORDER BY EXPIRY_DT, SYMBOL, OPTION_TYP, OPEN_INT DESC", nativeQuery = true)
    List<NSEBhavCopyEntity> findAll()

    @Query(value = "SELECT DISTINCT EXPIRY_DT FROM NSEBHAVCOPY ORDER BY EXPIRY_DT", nativeQuery = true)
    List<String> findDistinctByExpiryDate()

    @Query(value = "SELECT DISTINCT TIMESTAMP FROM NSEBHAVCOPY", nativeQuery = true)
    List<String> findDistinctByExtractDate()
}