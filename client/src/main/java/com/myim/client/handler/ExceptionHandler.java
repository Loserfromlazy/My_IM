package com.myim.client.handler;

import com.myim.client.command.CommandController;
import com.myim.client.session.ClientSession;
import com.myim.common.exception.InvalidException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ExectionHandler
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/28
 */
@Slf4j
@Service
public class ExceptionHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    CommandController commandController;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof InvalidException) {
            log.error(cause.getMessage());
            ClientSession.close(ctx.channel());
        }
        log.error(cause.getMessage());
        ctx.close();

        //重连
        if (commandController == null) {
            return;
        }
        commandController.startServer();
    }
}
