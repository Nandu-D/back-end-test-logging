package com.example.backendtestlogging.repository;

import com.example.backendtestlogging.model.client_logs.db_model.UserActionLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientSideUserActionLogRepository extends ElasticsearchRepository<UserActionLog, String> {
    List<UserActionLog> findAll();
}
