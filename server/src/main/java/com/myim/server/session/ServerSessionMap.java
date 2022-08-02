package com.myim.server.session;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * ServerSessionMap
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/25
 */
@Slf4j
public class ServerSessionMap {
    private ConcurrentMap<String, ServerSession> concurrentMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, List<ServerSession>> userMap = new ConcurrentHashMap<>();


    //饿汉式单例模式
    private static ServerSessionMap sessionMap = new ServerSessionMap();

    private ServerSessionMap() {
    }

    public static ServerSessionMap getServerSessionMap() {
        return sessionMap;
    }

    public void addSession(ServerSession session) {
        concurrentMap.put(session.getSessionId(), session);
        String uid = session.getUser().getUid();
        if (userMap.get(uid) == null || userMap.get(uid).size() == 0) {
            List<ServerSession> sessionList = new ArrayList<>();
            sessionList.add(session);
            userMap.put(session.getUser().getUid(), sessionList);
        } else {
            userMap.get(uid).add(session);
        }
        log.info("uid={}上线", session.getUser().getUid());
    }

    public void removeSession(String sessionId) {
        if (!concurrentMap.containsKey(sessionId)) {
            return;
        }
        final ServerSession session = concurrentMap.get(sessionId);
        concurrentMap.remove(sessionId);
        log.info("uid={}下线", session.getUser().getUid());

    }

    public ServerSession getSession(String sessionId) {
        if (concurrentMap.containsKey(sessionId)) {
            return concurrentMap.get(sessionId);
        }
        return null;
    }

    /**
     * 根据用户Id获取Session列表
     *
     * @author Yuhaoran
     * @date 2022/7/25 13:58
     */
    public List<ServerSession> getSessionByUserId(String userId) {
        return userMap.get(userId);
    }


}
