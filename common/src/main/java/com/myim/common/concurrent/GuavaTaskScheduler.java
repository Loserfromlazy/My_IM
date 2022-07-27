package com.myim.common.concurrent;

import com.google.common.util.concurrent.*;
import com.myim.common.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * GuavaTaskScheduler
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/27
 */
public class GuavaTaskScheduler {
    private static ListeningExecutorService gPoll;
    private static ThreadPoolExecutor mixedPoll;
    static {
        mixedPoll = ThreadUtil.getMixedThreadPoll();
        gPoll = MoreExecutors.listeningDecorator(mixedPoll);
    }

    private GuavaTaskScheduler() {
    }

    public static <T> void addTask(CallableTask<T> callableTask){
        ListenableFuture<T> submit = gPoll.submit(callableTask::run);
        Futures.addCallback(submit, new FutureCallback<T>() {
            @Override
            public void onSuccess(T t) {
                callableTask.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callableTask.onError(throwable);
            }
        });
    }
}
