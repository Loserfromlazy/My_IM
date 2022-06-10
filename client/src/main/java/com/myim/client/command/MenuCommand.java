package com.myim.client.command;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * MenuCommand
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/6/9
 */
@Data
@Component
public class MenuCommand implements BaseCommand {
    static Integer KEY = 1;
    static String MES = "展示菜单";

    private String command;
    private String showMenu;


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
        this.command = input;
    }

    /**
     * 设置菜单
     *
     * @param commandMap 菜单Map
     */
    public void setMenu(Map<Integer, BaseCommand> commandMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer, BaseCommand> stringBaseCommandEntry : commandMap.entrySet()) {
            stringBuilder.append("请选择菜单: ")
                    .append(stringBaseCommandEntry.getKey())
                    .append("->")
                    .append(stringBaseCommandEntry.getValue().getMes()).append("\n");
        }
        this.showMenu = stringBuilder.toString();
    }
}
