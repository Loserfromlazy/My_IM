package com.myim.common.concurrent;

/**
 * <p>
 * CallableTask
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/27
 */
public interface CallableTask<T> {

    public T run()throws Exception;
    public void onSuccess(T t);
    public void onError(Throwable t);
}
