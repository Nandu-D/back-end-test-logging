package com.example.backendtestlogging.services;

import com.example.backendtestlogging.model.client_logs.UserLog;
import com.example.backendtestlogging.model.client_logs.db_model.UserActionLog;

import java.util.List;

public interface ClientSideLoggerService {
    void handleIncomingClientLogs(UserLog userLog);
    List<UserLog> getUserLogs(String userId, String fromDateString, String toDateString, String type);
    List<UserActionLog> getUserActionLogs(String userId, String fromDateString, String toDateString, String type);
}
