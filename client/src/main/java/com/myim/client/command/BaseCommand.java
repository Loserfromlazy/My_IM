package com.myim.client.command;

import java.util.Scanner;

/**
 * <p>
 * BaseCommand
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/6/9
 */
public interface BaseCommand {
    /**
     * Command类通用键值
     *
     * @author Yuhaoran
     * @date 2022/6/9 10:09
     */
    public Integer getKey();

    /**
     * Command类通用键值对应的信息
     *
     * @author Yuhaoran
     * @date 2022/6/9 10:09
     */
    public String getMes();

    /**
     * Command类通用执行命令方法
     *
     * @author Yuhaoran
     * @date 2022/6/9 10:09
     */
    public void exec(String input);
}
