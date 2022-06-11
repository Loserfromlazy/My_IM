package com.myim.client;

import com.alibaba.fastjson.JSONObject;
import com.myim.client.command.BaseCommand;
import com.myim.client.command.LoginCommand;
import com.myim.client.command.MenuCommand;
import com.myim.client.entity.User;
import com.myim.client.session.ClientSession;
import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.AttributeKey;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootTest
class ClientApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testLoginCommand() {
        LoginCommand loginCommand = new LoginCommand();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入用户名密码(username@password)：");
            String nextLine = scanner.nextLine();
            loginCommand.exec(nextLine);
            System.out.println("本次输入的 username 为：" + loginCommand.getUsername());
        }
    }

    MenuCommand menuCommand = new MenuCommand();

    @Test
    public void testMenuCommand() {
        initMap();
        LoginCommand loginCommand = new LoginCommand();
        System.out.println(menuCommand.getShowMenu());
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入菜单选项");
            String nextLine = scanner.nextLine();
            switch (nextLine) {
                case "1":
                    System.out.println(menuCommand.getShowMenu());
                    break;
                case "2":
                    System.out.println("请输入用户名密码(username@password)：");
                    Scanner input = new Scanner(System.in);
                    loginCommand.exec(input.nextLine());
                    System.out.println("本次输入的 username 为：" + loginCommand.getUsername());
                    break;
            }
        }

    }

    public void initMap() {
        LoginCommand loginCommand = new LoginCommand();
        Map<Integer, BaseCommand> menuMap = new HashMap<>();
        menuMap.put(loginCommand.getKey(), loginCommand);
        menuMap.put(menuCommand.getKey(), menuCommand);
        menuCommand.setMenu(menuMap);
    }


    static final AttributeKey<ClientSession> attributeKey = AttributeKey.valueOf("SESSION");
    @Test
    public void testClientSession(){
        ClientSession clientSession = new ClientSession(new EmbeddedChannel());
        User user = new User();
        user.setUsername("111");
        user.setPassword("111");
        System.out.println(JSONObject.toJSONString(user));
        clientSession.setUser(user);
        System.out.println("正向导航");
        Channel channel = clientSession.getChannel();
        System.out.println();
        System.out.println("反向导航");
        User getUser =channel.attr(attributeKey).get().getUser();
        System.out.println(JSONObject.toJSONString(getUser));

    }

}