package com.example.backendtestlogging.model.client_logs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Properties {
    private Integer locationX;
    private Integer locationY;
    private String viewedId;
    private String pageFrom;
    private String pageTo;

    @Override
    public String toString() {
        return "Properties{" +
                "locationX=" + locationX +
                ", locationY=" + locationY +
                ", viewedId='" + viewedId + '\'' +
                ", pageFrom='" + pageFrom + '\'' +
                ", pageTo='" + pageTo + '\'' +
                '}';
    }
}
