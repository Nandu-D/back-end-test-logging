package com.example.backendtestlogging.model.client_logs.db_model;

import com.example.backendtestlogging.model.client_logs.Properties;
import com.example.backendtestlogging.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = Constants.ELASTICSEARCH_INDEX)
public class UserActionLog {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String userId;
    @Field(type = FieldType.Text)
    private String sessionId;
    @Field(type = FieldType.Text)
    private String time;
    @Field(type = FieldType.Text)
    private String type;
    @Field(type = FieldType.Nested, includeInParent = true)
    private Properties properties;

    public UserActionLog(String userId, String sessionId, String time, String type, Properties properties) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.time = time;
        this.type = type;
        this.properties = properties;
    }
}
