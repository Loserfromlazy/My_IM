package com.myim.server.process;

import com.myim.common.pojo.ProtoMsgOuterClass;
import com.myim.server.session.ServerSession;

/**
 * <p>
 * BaseProcessor
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/8/1
 */
public interface BaseProcessor {
    boolean action(ServerSession session, ProtoMsgOuterClass.ProtoMsg.Message message);
}
