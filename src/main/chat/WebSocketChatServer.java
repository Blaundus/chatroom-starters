package lab.io.java.nano.chat;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */

    private Session session;
    private static Set<WebSocketChatServer> chatEndpoints
            = new CopyOnWriteArraySet<WebSocketChatServer>();


    private static HashMap<String, String> users = new HashMap<>();
    private static Map<String, Session>  sessionMap = new ConcurrentHashMap<>();



    private static void sendMessageToAll(Session session, Message message) throws IOException, EncodeException {
        message.setSender(users.get(session.getId()));
        broadcast(message);
    }
    private static void sendMessageToAll(Message message) throws IOException, EncodeException {
        message.setSender(message.getSender());
        broadcast(message);
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username );

        sessionMap.put(session.getId(), session);

        Message message = new Message();
        message.setSender(username);
        message.setContent("Connected!");
        message.setContent("Disconnected!");

        broadcast(message);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session,  String  message) throws IOException, EncodeException {
        Message messages = new Message(message);
       sendMessageToAll(messages);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        //TODO: add close connection.
        chatEndpoints.remove(this);
        sessionMap.remove(session.getId());
        Message message = new Message();
        message.setSender(users.get(session.getId()));
        message.setContent("Disconnected!");
        broadcast(message);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    private static void broadcast(Message message)
            throws IOException, EncodeException {

        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                            sendObject(message);
                } catch (EncodeException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
