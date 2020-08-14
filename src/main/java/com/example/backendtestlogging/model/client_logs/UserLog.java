package com.example.backendtestlogging.model.client_logs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class UserLog {
    private String userId;
    private String sessionId;
    private List<Action> actions = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", actions=" + actions +
                '}';
    }
}