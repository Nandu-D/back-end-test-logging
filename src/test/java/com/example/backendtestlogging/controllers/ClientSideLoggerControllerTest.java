package com.example.backendtestlogging.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backendtestlogging.model.client_logs.Action;
import com.example.backendtestlogging.model.client_logs.Properties;
import com.example.backendtestlogging.model.client_logs.UserLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class ClientSideLoggerControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnOk() throws Exception {
        List<Action> actionList = new ArrayList<>();

        Properties properties1 = new Properties(5, 10, null, null, null);
        Action action1 = new Action("2018-10-18T21:38:28+06:00", "CLICK", properties1);
        actionList.add(action1);
        Properties properties2 = new Properties(null, null, null, "communities", "inventory");
        Action action2 = new Action("2018-10-18T21:38:28+06:00", "NAVIGATE", properties1);
        actionList.add(action2);

        UserLog userLog = new UserLog("ABCD", "EFGH", actionList);
        this.mockMvc.perform(post("/api/v1/client-log").contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(userLog)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn_StatusCode_UnProcessableEntity() throws Exception {
        List<Action> actionList = new ArrayList<>();

        Properties properties1 = new Properties(5, 10, null, null, null);
        Action action1 = new Action("2018-10-18T21:38:28+06:00", "CLICK", properties1);
        actionList.add(action1);
        Properties properties2 = new Properties(null, null, null, "communities", "inventory");
        Action action2 = new Action("2018-10-18T21:38:28+06:00", "NAVIGATE", properties1);
        actionList.add(action2);

        UserLog userLog = new UserLog(null, "EFGH", actionList);
        this.mockMvc.perform(post("/api/v1/client-log").contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(userLog)))
                .andExpect(status().isUnprocessableEntity());
    }
}