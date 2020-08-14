package com.example.backendtestlogging.repository;

import com.example.backendtestlogging.model.client_logs.UserLog;

import java.util.List;

public interface ClientSideUserLogDAL {
    List<UserLog> getUserLogs(String userId, String fromDateString, String toDateString, String type);
}
