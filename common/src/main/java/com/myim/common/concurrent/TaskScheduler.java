package com.myim.common.concurrent;

import com.myim.common.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * TaskScheduler
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/27
 */
public class TaskScheduler {
    private static final ThreadPoolExecutor mixedPoll;

    static {
        mixedPoll = ThreadUtil.getMixedThreadPoll();
    }

    private TaskScheduler() {
    }

    public static void addTask(ExecuteTask taskFuture){
        mixedPoll.submit(taskFuture::execute);

    }
}
