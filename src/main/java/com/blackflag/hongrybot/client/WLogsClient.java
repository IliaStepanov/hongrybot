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
    private static final String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5MjU4M2Y0ZS1kYTc5LTQ5ZTctOTA3ZC03MzNhNDhkYTRhOGEiLCJqdGkiOiIzOTI1NmM5ZmM3MzE4ZDEzNzkwM2Q3NmQyOWIxZTFmOWJlMTVmMzdiZjJiZDYyMzAwZmQ4NDc5ZjExNTQ5ZTEyNjRlMTc4ODQ0N2FhYmRhOCIsImlhdCI6MTYxMDAzNDM1OCwibmJmIjoxNjEwMDM0MzU4LCJleHAiOjE2MTI2MjYzNTgsInN1YiI6IiIsInNjb3BlcyI6WyJ2aWV3LXByaXZhdGUtcmVwb3J0cyJdfQ.x-ATSnK6qoN8Mo_Oh3FnycFj6F9lYyU0nmQf4QB42Wy25Fx9miAnPzB_9mULXr4-R_Z7YF9nwpHyhnwnqCLvQZrLq1YdaTYl-zPxJSA_ga0V76bf2Ob8xtSaNLXs2Kvys1xsCvwJwE01obA-DOUvYveDc87B9xAigTTpRu444wq12BtQgqWP2JjJGZqpr_MEhsOPLP8Kouhl_LkBaEGPeylSr88lKGec5SUYr1hp1_pT7a23S65bVcVyOtlPCpzvoEUBYbjUTQ6ikcBTyoiV0Ai6tV8pS-WgzK68fQw0J1DovYl4KecC8_jADRRdpi-fsXzv3tLS9hRK1rwIt8jVmAJr6YskxUy5ea3irYFQHq-iRHf7qnsWeVmHsS6gQxFeuw3JfWkoB4d24D3vmBsODq2NVbaHLgmy1HEkqKLbO2bgarzq1ribSQjiNQfjUKXr30gMoBeAIUOjZHtNlQ2uyv4LrgZ2ibidWqzoGVDi7-lS6XLyCg6HZs0l1pelBh-BXz8qSx5wRrrZhBf7M_QUXsmo-2A_ul0E1q9lder-hoHRmEyc11sKCv2AgkZcpN33dZoyvSj8bIGfSQJYwK_DrRV4_ItLCJFc8YwdPDOjf3z4nAU0Vuis4S7CK490DIzlhEnFgG_1bw2s9YZAKmrvKvRyXyw2JBW61L2j5s4gVrc";


    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;
    private final String warcraftLogsAuthUrl;
    private final String warcraftLogsApiUrl;

    @Autowired
    public WLogsClient(ObjectMapper objectMapper,
                       @Value("${warcraftLogs-id}") String clientId,
                       @Value("${warcraftLogs-secret}") String clientSecret,
                       @Value("${warcraftLogs-auth-url}") String warcraftLogsAuthUrl,
                       @Value("${warcraftLogs-url}") String warcraftLogsApiUrl) {
        this.objectMapper = objectMapper;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
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
