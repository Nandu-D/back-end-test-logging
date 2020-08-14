package com.example.backendtestlogging.repository;

import com.example.backendtestlogging.model.client_logs.UserLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientSideUserLogRepository extends MongoRepository<UserLog, String> {
    @Query("{ 'userId': ?0,  'actions': { 'type' : 'CLICK' } }")
    List<UserLog> getUserLogsByUserIdTimeAndType(String userId, String fromDate, String toDate, String type);

    //@Query("{ 'userId': ?0, 'time' : { $gt: ?1, $lt: ?2 }, 'type' : ?3 }")
    List<UserLog> getUserLogsByUserId(String userId);

    @Query("{ 'type' : ?0 }")
    List<UserLog> getUserLogsByType(String type);
}
