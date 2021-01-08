package com.blackflag.hongrybot.service;

import com.blackflag.hongrybot.client.WLogsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogLinkListener extends ListenerAdapter {

    private final WLogsClient wLogsClient;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        log.info("Got a message! {}", msg);
        String contentRaw = msg.getContentRaw();
        if (contentRaw.equals("!logs") || contentRaw.matches(".*ссылк.* на лог.*")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Here is last BlackFlag report link!  " + wLogsClient.getLastReportLink()).queue();
        }
    }
}
