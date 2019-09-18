package com.bk.exchange.repository;

import com.bk.exchange.models.ExchangeRateHistory;
import com.bk.exchange.models.ExchangeRateHistoryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
/**
 * Saving successful exchange rate data to DB
 */
public interface ExchangeHistoryDao extends JpaRepository<ExchangeRateHistory, ExchangeRateHistoryKey> {

    @Query(value = "SELECT e FROM ExchangeRateHistory e where year(e.key.date)=:year and month(e.key.date)=:month")
    List<ExchangeRateHistory> findExchangeRateHistoriesByYearMonth(@Param("year") Integer year,
                                                              @Param("month") Integer month);

    @Query(value = "SELECT e FROM ExchangeRateHistory e where year(e.key.date)=:year and month(e.key.date)=:month" +
            " and day(e.key.date)=:day")
    List<ExchangeRateHistory> findExchangeRateHistoriesByYearMonthandDate(@Param("year") Integer year,
                                                                          @Param("month") Integer month,
                                                                          @Param("day") Integer day);

}
