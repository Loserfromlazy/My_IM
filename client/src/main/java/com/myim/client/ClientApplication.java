package com.myim.client;

import com.myim.client.command.CommandController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.myim")
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ClientApplication.class, args);
        CommandController commandController = context.getBean(CommandController.class);
        commandController.initMap();
        commandController.startServer();
    }

}
