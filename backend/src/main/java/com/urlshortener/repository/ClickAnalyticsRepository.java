package com.urlshortener.repository;

import com.urlshortener.entity.ClickAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickAnalyticsRepository extends JpaRepository<ClickAnalytics, Long> {
        List<ClickAnalytics> findByUrlId(Long urlId);

        @Query("SELECT DATE(c.clickedAt) as date, COUNT(c) as count " +
                        "FROM ClickAnalytics c " +
                        "WHERE c.urlId = :urlId " +
                        "GROUP BY DATE(c.clickedAt) " +
                        "ORDER BY DATE(c.clickedAt)")
        List<Object[]> findClicksByDateForUrl(@Param("urlId") Long urlId);

        @Query("SELECT DATE(c.clickedAt) as date, COUNT(c) as count " +
                        "FROM ClickAnalytics c " +
                        "JOIN Url u ON c.urlId = u.id " +
                        "WHERE u.userId = :userId " +
                        "GROUP BY DATE(c.clickedAt) " +
                        "ORDER BY DATE(c.clickedAt)")
        List<Object[]> findClicksByDateForUser(@Param("userId") Long userId);

        long countByUrlId(Long urlId);
}
