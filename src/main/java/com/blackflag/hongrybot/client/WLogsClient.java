package com.blackflag.hongrybot.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Slf4j
@Service
public class WLogsClient {


    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;
    private final String warcraftLogsAuthUrl;
    private final String warcraftLogsApiUrl;

    @Autowired
    public WLogsClient(ObjectMapper objectMapper,
                       @Value("${warcraftLogs-auth-url}") String warcraftLogsAuthUrl,
                       @Value("${warcraftLogs-url}") String warcraftLogsApiUrl) {
        this.objectMapper = objectMapper;
        this.clientId = System.getProperty("warcraftLogs-id");
        this.clientSecret = System.getProperty("warcraftLogs-secret");
        this.warcraftLogsAuthUrl = warcraftLogsAuthUrl;
        this.warcraftLogsApiUrl = warcraftLogsApiUrl;

        webClient = WebClient.builder().build();
    }


    public String getLastReportLink() {
        String result = Objects.requireNonNull(webClient.post()
                .uri(warcraftLogsApiUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"query\":\"query{\\n reportData{\\n  reports(guildID:78858, limit:1){\\n  data{\\n    code\\n    startTime\\n    guild{\\n      name\\n    }\\n  }\\n  }\\n}\\n}\\n\"}")
                .retrieve()
                .toEntity(String.class)
                .block()).getBody();

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(result);
        } catch (JsonProcessingException e) {
            log.error("Error parsing base response: {}", result);
            e.printStackTrace();
        }
        String code = Objects.requireNonNull(jsonNode).get("data").get("reportData").get("reports").get("data").get(0).get("code").asText();

        return "WlogLink is: https://www.warcraftlogs.com/reports/" + code;
    }


    private String getToken() {
        String response = Objects.requireNonNull(webClient.post()
                .uri(warcraftLogsAuthUrl)
                .header("Content-Type", "multipart/form-data")
                .header("content-type", "multipart/form-data; boundary=---011000010111000001101001")
                .bodyValue(String.format("-----011000010111000001101001\r\nContent-Disposition: " +
                        "form-data; name=\"grant_type\"\r\n\r\nclient_credentials\r\n" +
                        "-----011000010111000001101001\r\nContent-Disposition: " +
                        "form-data; name=\"client_id\"\r\n\r\n%s\r\n" +
                        "-----011000010111000001101001\r\nContent-Disposition: " +
                        "form-data; name=\"client_secret\"\r\n\r\n%s\r\n" +
                        "-----011000010111000001101001--\r\n", clientId, clientSecret))
                .retrieve()
                .toEntity(String.class)
                .block()).getBody();

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            log.error("Error parsing token response: {}", response);
            e.printStackTrace();
        }

        return Objects.requireNonNull(jsonNode).get("access_token").asText();
    }
}
