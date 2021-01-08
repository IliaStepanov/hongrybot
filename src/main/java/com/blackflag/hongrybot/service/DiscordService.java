package com.blackflag.hongrybot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordService {

    private final LogLinkListener logLinkListener;

    @PostConstruct
    public void init() throws LoginException {
        JDA jda = JDABuilder.createDefault("Nzk3MDIzOTY4MjA4NDg2NDIy.X_gcag.n_0zWbJR1kr4X0-KY0Gg1-FIesE").build();

        jda.addEventListener(logLinkListener);
    }
}
