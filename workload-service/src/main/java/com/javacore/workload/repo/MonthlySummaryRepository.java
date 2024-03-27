package com.javacore.workload.repo;

import com.javacore.workload.model.MonthlySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonthlySummaryRepository extends MongoRepository<MonthlySummary,String> {
    Optional<MonthlySummary> findByUsername(String username);
}
