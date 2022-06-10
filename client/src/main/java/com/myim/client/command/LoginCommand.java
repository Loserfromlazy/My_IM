package com.myim.client.command;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * <p>
 * LoginCommand
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/6/9
 */
@Data
@Slf4j
@Component
public class LoginCommand implements BaseCommand{

    static Integer KEY = 2;
    static String MES = "登录";
    private String username;
    private String password;

    @Override
    public Integer getKey() {
        return KEY;
    }

    @Override
    public String getMes() {
        return MES;
    }

    @Override
    public void exec(String input) {
        final String[] split = input.split("@");
        if (split.length!= 2) {
            log.error("格式不正确(id@password):");
        } else {
            username = split[0];
            password = split[1];
        }
    }
}
