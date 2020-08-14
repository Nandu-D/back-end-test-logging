package com.example.backendtestlogging.controllers;

import com.example.backendtestlogging.exceptions.MissingCrucialLogInfoException;
import com.example.backendtestlogging.model.client_logs.Action;
import com.example.backendtestlogging.model.client_logs.Properties;
import com.example.backendtestlogging.model.client_logs.UserLog;
import com.example.backendtestlogging.model.client_logs.db_model.UserActionLog;
import com.example.backendtestlogging.services.ClientSideLoggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1")
public class ClientSideLoggerController {

    private final ClientSideLoggerService clientSideLoggerService;

    public ClientSideLoggerController(ClientSideLoggerService clientSideLoggerService) {
        this.clientSideLoggerService = clientSideLoggerService;
    }

    @PostMapping("client-log")
    private ResponseEntity<?> handleClientSideLogs(@RequestBody UserLog userLog) {
        if (userLog.getUserId() == null) {
            log.warn("userId missing from log body for sessionId {}", userLog.getSessionId());
            throw new MissingCrucialLogInfoException("Request body is missing userId");
        }
        if (userLog.getSessionId() == null) {
            log.warn("sessionId missing from log body for userId {}", userLog.getUserId());
            throw new MissingCrucialLogInfoException("Request body is missing sessionId");
        }

        clientSideLoggerService.handleIncomingClientLogs(userLog);

        return ResponseEntity.ok().build();
    }

    @GetMapping("client-log")
    private ResponseEntity<?> retrieveClientLogs(
            @RequestParam(required = false, name = "userid") String userId,
            @RequestParam(required = false, name = "fromdate") String fromDate,
            @RequestParam(required = false, name = "todate") String toDate,
            @RequestParam(required = false, name = "loglevel") String type) {

        //Expecting fromDate and toDate to be in the format yyyy-MM-ddTHH:mm:ss.SSSX.
        //So if date is "2018-10-18T21:37:28+06:00" then it will be url decoded to "2018-10-18T21:37:28 06:00"
        //In that a '+' is missing. Re adding it if needed.
        if (fromDate != null) fromDate = fromDate.replace(' ', '+');
        if (toDate != null) toDate = toDate.replace(' ', '+');
        if (fromDate != null && toDate == null) {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            toDate = dateTime.format(formatter) + "+00:00";
        }

        if (fromDate == null) fromDate = "";
        if (toDate == null) fromDate = "";

        List<UserActionLog> logsSaved = clientSideLoggerService.getUserActionLogs(userId, fromDate, toDate, type);

        return ResponseEntity.ok(logsSaved);
    }
}
