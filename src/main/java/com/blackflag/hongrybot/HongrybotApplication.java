package com.blackflag.hongrybot;

import com.blackflag.hongrybot.client.WLogsClient;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootApplication
public class HongrybotApplication {

    private static final String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5MjU4M2Y0ZS1kYTc5LTQ5ZTctOTA3ZC03MzNhNDhkYTRhOGEiLCJqdGkiOiIzOTI1NmM5ZmM3MzE4ZDEzNzkwM2Q3NmQyOWIxZTFmOWJlMTVmMzdiZjJiZDYyMzAwZmQ4NDc5ZjExNTQ5ZTEyNjRlMTc4ODQ0N2FhYmRhOCIsImlhdCI6MTYxMDAzNDM1OCwibmJmIjoxNjEwMDM0MzU4LCJleHAiOjE2MTI2MjYzNTgsInN1YiI6IiIsInNjb3BlcyI6WyJ2aWV3LXByaXZhdGUtcmVwb3J0cyJdfQ.x-ATSnK6qoN8Mo_Oh3FnycFj6F9lYyU0nmQf4QB42Wy25Fx9miAnPzB_9mULXr4-R_Z7YF9nwpHyhnwnqCLvQZrLq1YdaTYl-zPxJSA_ga0V76bf2Ob8xtSaNLXs2Kvys1xsCvwJwE01obA-DOUvYveDc87B9xAigTTpRu444wq12BtQgqWP2JjJGZqpr_MEhsOPLP8Kouhl_LkBaEGPeylSr88lKGec5SUYr1hp1_pT7a23S65bVcVyOtlPCpzvoEUBYbjUTQ6ikcBTyoiV0Ai6tV8pS-WgzK68fQw0J1DovYl4KecC8_jADRRdpi-fsXzv3tLS9hRK1rwIt8jVmAJr6YskxUy5ea3irYFQHq-iRHf7qnsWeVmHsS6gQxFeuw3JfWkoB4d24D3vmBsODq2NVbaHLgmy1HEkqKLbO2bgarzq1ribSQjiNQfjUKXr30gMoBeAIUOjZHtNlQ2uyv4LrgZ2ibidWqzoGVDi7-lS6XLyCg6HZs0l1pelBh-BXz8qSx5wRrrZhBf7M_QUXsmo-2A_ul0E1q9lder-hoHRmEyc11sKCv2AgkZcpN33dZoyvSj8bIGfSQJYwK_DrRV4_ItLCJFc8YwdPDOjf3z4nAU0Vuis4S7CK490DIzlhEnFgG_1bw2s9YZAKmrvKvRyXyw2JBW61L2j5s4gVrc";


    public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(HongrybotApplication.class, args);


        WLogsClient wLogsClient = context.getBean(WLogsClient.class);


        for (int i = 0; i < 10; i++) {
            System.out.println(wLogsClient.getLastReportLink());
        }




    }

}
