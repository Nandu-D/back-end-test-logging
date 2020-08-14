package com.example.backendtestlogging.repository;

import com.example.backendtestlogging.model.client_logs.UserLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ClientSideUserLogDALImpl implements ClientSideUserLogDAL {

    private final MongoTemplate mongoTemplate;

    public ClientSideUserLogDALImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<UserLog> getUserLogs(String userId, String fromDateString, String toDateString, String type) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<UserLog> userLogs = mongoTemplate.find(query, UserLog.class);
        log.info("userLogs: {}", userLogs);
        return userLogs;
    }
}
