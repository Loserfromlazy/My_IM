package com.myim.common.util;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * <p>
 * ThreadUtil
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/27
 */
public class ThreadUtil {

    private static final int KEEP_ALIVE_SECONDS = 30;

    private static final String MIXED_THREAD_AMOUNT = "mixed.thread.amount";
    private static final int MAX_THREADS = 128;
    private static final int QUEUE_SIZE = 128;

    private static class MixedThreadPoolHolder {
        private static int max = System.getProperty(MIXED_THREAD_AMOUNT) == null ? MAX_THREADS : Integer.parseInt(System.getProperty(MIXED_THREAD_AMOUNT));
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                max,
                max,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(QUEUE_SIZE),
                Executors.defaultThreadFactory()
        );

        static {
            EXECUTOR.allowCoreThreadTimeOut(true);
            Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("关闭线程池", () -> {
                shutdownThreadPoolGracefully(EXECUTOR);
                return null;
            }));
        }

    }
    public static ThreadPoolExecutor getMixedThreadPoll(){
        return MixedThreadPoolHolder.EXECUTOR;
    }

    private static void shutdownThreadPoolGracefully(ExecutorService pool) {
        //已经关闭返回
        if (pool.isTerminated()){
            return;
        }
        try{
            pool.shutdown();
        }catch (SecurityException | NullPointerException e){
            return;
        }
        //等待关闭
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)){
                pool.shutdownNow();
            }
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)){
                System.err.println("线程池任务未正常执行结束");
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }
        //还没关闭，循环关闭1000次
        try {
            for (int i = 0; i < 1000; i++) {
                if (pool.awaitTermination(10, TimeUnit.MILLISECONDS)) {
                    break;
                }
                pool.shutdownNow();
            }
        } catch (Throwable e){
            System.err.println(e.getMessage());
        }

    }


    private static class ShutdownHookThread extends Thread{
        private Callable callable;

        public ShutdownHookThread(String name,Callable callable){
            super("JVM退出钩子-"+name);
            this.callable = callable;
        }

        @Override
        public void run() {
            final long startTime = System.currentTimeMillis();
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            final long endTime = System.currentTimeMillis();
            System.out.println("执行时间="+(endTime-startTime));
        }
    }
}
