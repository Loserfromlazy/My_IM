package com.myim.client.service;

import com.myim.client.command.BaseCommand;
import com.myim.client.command.LoginCommand;
import com.myim.client.command.MenuCommand;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * <p>
 * CommandController
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/6/9
 */
public class CommandService {
    @Autowired
    LoginCommand loginCommand;
    @Autowired
    MenuCommand menuCommand;

    private Map<Integer, BaseCommand> menuMap;

    private void initMap(){
        menuMap.put(loginCommand.getKey(), loginCommand);
        menuMap.put(menuCommand.getKey(), menuCommand);
        menuCommand.setMenu(menuMap);
    }

}
