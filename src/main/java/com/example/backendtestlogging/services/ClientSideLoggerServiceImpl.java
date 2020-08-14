package com.example.backendtestlogging.services;

import com.example.backendtestlogging.config.ElasticsearchConfig;
import com.example.backendtestlogging.exceptions.ArgumentParsingException;
import com.example.backendtestlogging.model.client_logs.Action;
import com.example.backendtestlogging.model.client_logs.Properties;
import com.example.backendtestlogging.model.client_logs.UserLog;
import com.example.backendtestlogging.model.client_logs.db_model.UserActionLog;
import com.example.backendtestlogging.repository.ClientSideUserActionLogRepository;
import com.example.backendtestlogging.repository.ClientSideUserLogDAL;
import com.example.backendtestlogging.repository.ClientSideUserLogRepository;
import com.example.backendtestlogging.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ClientSideLoggerServiceImpl implements ClientSideLoggerService {

    private final ClientSideUserLogRepository clientSideUserLogRepository;
    private final ClientSideUserLogDAL clientSideUserLogDAL;
    private final ClientSideUserActionLogRepository clientSideUserActionLogRepository;
    private final RestHighLevelClient client;

    public ClientSideLoggerServiceImpl(ClientSideUserLogRepository clientSideUserLogRepository,
                                       ClientSideUserLogDAL clientSideUserLogDAL,
                                       ClientSideUserActionLogRepository clientSideUserActionLogRepository,
                                       @Qualifier("high-level-client") RestHighLevelClient client) {
        this.clientSideUserLogRepository = clientSideUserLogRepository;
        this.clientSideUserLogDAL = clientSideUserLogDAL;
        this.clientSideUserActionLogRepository = clientSideUserActionLogRepository;
        this.client = client;
    }

    @Override
    public void handleIncomingClientLogs(UserLog userLog) {
        List<UserActionLog> userActionLogs = new ArrayList<>();
        String userId = userLog.getUserId();
        String sessionId = userLog.getSessionId();
        for (Action action : userLog.getActions()) {
            String time = action.getTime();
            String type = action.getType();
            Properties properties = action.getProperties();
            log.info("{} {} {} {} {}", userId, sessionId, time, type, properties);
            userActionLogs.add(new UserActionLog(userId, sessionId, time, type, properties));
        }
        clientSideUserActionLogRepository.saveAll(userActionLogs);
    }

    @Override
    public List<UserLog> getUserLogs(String userId, String fromDateString, String toDateString, String type) {
        if(userId == null && fromDateString == null && toDateString == null && type == null) {
            return clientSideUserLogRepository.findAll();
        }

        List<UserLog> logList;
        logList = clientSideUserLogRepository.getUserLogsByUserIdTimeAndType(userId, fromDateString, toDateString, type);
//        if (userId == null) {
//            if (fromDateString == null && toDateString == null) {
//                logList = clientSideUserLogRepository.getUserLogsByType(type);
//            } else {
//                logList = null;
//            }
//        } else {
//            logList = clientSideUserLogRepository.getUserLogsByUserId(userId);
//        }

        return logList;
    }

    @Override
    public List<UserActionLog> getUserActionLogs(String userId, String fromDateString, String toDateString, String type) {
        if(userId == null && fromDateString == null && toDateString == null && type == null) {
            return clientSideUserActionLogRepository.findAll();
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (userId != null) {
            boolQueryBuilder.must(new TermQueryBuilder(Constants.ELASTICSEARCH_FIELD_USERID, userId));
        }
        if (type != null) {
            boolQueryBuilder.must(new TermQueryBuilder(Constants.ELASTICSEARCH_FIELD_TYPE, type));
        }
        if (fromDateString != null) {
            boolQueryBuilder.must(new RangeQueryBuilder(Constants.ELASTICSEARCH_FIELD_TIME).gte(fromDateString));
            boolQueryBuilder.must(new RangeQueryBuilder(Constants.ELASTICSEARCH_FIELD_TIME).lte(toDateString));
        }

        SearchRequest searchRequest = new SearchRequest(Constants.ELASTICSEARCH_INDEX);
        searchRequest.source().query(boolQueryBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ArgumentParsingException("Could not parse date");
        }

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<UserActionLog> userActionLogs = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String userIdResult = (String) sourceAsMap.get(Constants.ELASTICSEARCH_FIELD_USERID);
            String sessionIdResult = (String) sourceAsMap.get(Constants.ELASTICSEARCH_FIELD_SESSIONID);
            String timeResult = (String) sourceAsMap.get(Constants.ELASTICSEARCH_FIELD_TIME);
            String typeResult = (String) sourceAsMap.get(Constants.ELASTICSEARCH_FIELD_TYPE);
            Map<String, Object> propertiesMap = (Map<String, Object>) sourceAsMap.get(Constants.ELASTICSEARCH_FIELD_PROPERTIES);
            Integer locationX = (Integer) propertiesMap.get(Constants.ELASTICSEARCH_FIELD_LOCATIONX);
            Integer locationY = (Integer) propertiesMap.get(Constants.ELASTICSEARCH_FIELD_LOCATIONY);
            String viewedId = (String) propertiesMap.get(Constants.ELASTICSEARCH_FIELD_VIEWEDID);
            String pageFrom = (String) propertiesMap.get(Constants.ELASTICSEARCH_FIELD_PAGEFROM);
            String pageTo = (String) propertiesMap.get(Constants.ELASTICSEARCH_FIELD_PAGETO);
            Properties properties = new Properties(locationX, locationY, viewedId, pageFrom, pageTo);

            UserActionLog userActionLog = new UserActionLog(userIdResult, sessionIdResult, timeResult, typeResult, properties);
            userActionLogs.add(userActionLog);
        }
        return userActionLogs;
    }
}
