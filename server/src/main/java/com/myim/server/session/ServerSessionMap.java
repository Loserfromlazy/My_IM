package com.myim.server.session;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

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


    //饿汉式单例模式
    private static ServerSessionMap sessionMap = new ServerSessionMap();

    private ServerSessionMap() {
    }

    public static ServerSessionMap getServerSessionMap() {
        return sessionMap;
    }

    public void addSession(ServerSession session) {
        concurrentMap.put(session.getSessionId(), session);
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
     * 根据用户Id获取Session列表（待优化）
     *
     * @author Yuhaoran
     * @date 2022/7/25 13:58
     */
    public List<ServerSession> getSessionByUserId(String userId) {
        List<ServerSession> collect = concurrentMap.values().stream()
                .filter(session -> Objects.equals(session.getUser().getUid(), userId))
                .collect(Collectors.toList());
        return collect;
    }


}
