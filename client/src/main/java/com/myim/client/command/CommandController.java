package com.myim.client.command;

import com.myim.client.converter.LoginRequestConverter;
import com.myim.client.sender.LoginSender;
import com.myim.client.session.ClientSession;
import com.myim.client.start.ChatClient;
import com.myim.common.concurrent.TaskScheduler;
import com.myim.common.entity.User;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * commandController
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/28
 */
@Component("CommandController")
@Slf4j
public class CommandController {

    @Autowired
    LoginCommand loginCommand;

    @Autowired
    MenuCommand menuCommand;

    @Autowired
    LoginSender loginSender;

    @Autowired
    ChatClient client;

    ClientSession clientSession;

    private boolean isConnect = false;

    public void startServer() {
        while (true) {
            while (!isConnect) {
                TaskScheduler.addTask(() -> {
                    client.setChannelFutureListener(channelFutureListener);
                    client.connect();
                });
                waitConnect();
            }

            System.out.println(menuCommand.getShowMenu());
            while (clientSession != null) {
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
                        startLogin();
                        break;
                }
            }
        }

    }

    private void startLogin() {
        User user = new User();
        user.setName(loginCommand.getUsername());
        user.setToken(loginCommand.getPassword());
        clientSession.setUser(user);
        loginSender.send(user,clientSession);
    }

    public void initMap() {
        Map<Integer, BaseCommand> menuMap = new HashMap<>();
        menuMap.put(loginCommand.getKey(), loginCommand);
        menuMap.put(menuCommand.getKey(), menuCommand);
        menuCommand.setMenu(menuMap);
    }

    /**
     * 连接回调监听器
     *
     * @author Yuhaoran
     * @date 2022/7/28 13:13
     */
    ChannelFutureListener channelFutureListener = channelFuture -> {
        if (channelFuture.isSuccess()) {
            this.isConnect = true;
            log.info("连接成功");
            clientSession = new ClientSession(channelFuture.channel());
            notifyConnect();
        } else {
            this.isConnect = false;
            log.error("连接失败");
            EventLoop eventLoop = channelFuture.channel().eventLoop();
            //10秒后重连
            eventLoop.schedule(() -> client.connect(), 10L, TimeUnit.SECONDS);
        }
    };

    /**
     * 阻塞当前线程，等待后续通知
     *
     * @author Yuhaoran
     * @date 2022/7/28 13:06
     */
    private synchronized void waitConnect() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知被阻塞的当前线程
     *
     * @author Yuhaoran
     * @date 2022/7/28 13:07
     */
    private synchronized void notifyConnect() {
        this.notify();
    }

}
