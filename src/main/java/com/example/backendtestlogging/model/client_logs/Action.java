package com.example.backendtestlogging.model.client_logs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Action {
    private String time;
    private String type;
    private Properties properties;

    @Override
    public String toString() {
        return "Action{" +
                "time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", properties=" + properties +
                '}';
    }
}