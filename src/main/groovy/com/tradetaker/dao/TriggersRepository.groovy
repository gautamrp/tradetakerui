package com.tradetaker.dao

import com.tradetaker.entity.TriggerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TriggersRepository extends JpaRepository<TriggerEntity, Long> {
    @Query(value = "SELECT * FROM TRIGGERS where TRIGGERED_DATE=:triggeredDate ORDER BY ID DESC", nativeQuery = true)
    List<TriggerEntity> findByTriggeredDateOrderByTriggeredAt(@Param("triggeredDate") String triggeredDate)

    @Query(value = "SELECT TRIGGERED_DATE FROM TRIGGERS  GROUP BY TRIGGERED_DATE ORDER BY TO_DATE(TRIGGERED_DATE, 'dd-mm-yyyy')  DESC", nativeQuery = true)
    List<String> findDistinctByTriggeredDate()

    @Query(value = "SELECT * FROM TRIGGERS where stock=:stock ORDER BY TRIGGERED_DATE DESC", nativeQuery = true)
    List<TriggerEntity> findArchiveTriggersByStock(@Param("stock") String stock)
}