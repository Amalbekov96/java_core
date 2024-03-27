package com.javacore.workload.repo.impl;

import com.javacore.workload.model.MonthlySummary;
import com.javacore.workload.repo.MonthlySummaryQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;



@Repository
@RequiredArgsConstructor
public class MonthlySummaryQueryDaoImpl implements MonthlySummaryQueryDao {

    private final MongoTemplate mongoTemplate;
    @Override
    public void deleteSummaryByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        mongoTemplate.remove(query, MonthlySummary.class);
    }
}
