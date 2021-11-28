package at.htl.optician.websockets;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/api/{username}")
public class WebSocket {
    Map<String, Session> sessions = new ConcurrentHashMap<>();
    Random random;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) throws InterruptedException {
        random = new Random();
        if (!message.equalsIgnoreCase("_ready_")) {
            Thread.sleep(random.nextInt(5000));
            if (message.contains("glasses") || message.contains("contacts")) {
                sessions.get(username).getAsyncRemote().sendObject("Product " + message + " is now ready! You can get it now!");
            } else {
                sessions.get(username).getAsyncRemote().sendObject("We do not own such product! Sorry");
            }
        }
    }
}
