package com.myim.client.command;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * MessageCommand
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/8/1
 */
@Slf4j
@Component
@Data
public class MessageCommand implements BaseCommand{

    public static final String KEY = "3";
    static String MES = "发消息";
    private String uid;
    private String content;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getMes() {
        return MES;
    }

    @Override
    public void exec(String input) {
        final String[] split = input.split(":");
        if (split.length!= 2) {
            log.error("格式不正确(用户id:消息内容)");
        } else {
            uid = split[0];
            content = split[1];
        }
    }
}
