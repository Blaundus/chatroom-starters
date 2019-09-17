package lab.io.java.nano.chat;

import com.google.gson.Gson;

import javax.websocket.EncodeException;


/**
 * WebSocket message model
 */
public class Message {
    private String type;
    private String content;
    private String sender;
    private int onlineUserCount;

    private static Gson gson = new Gson();


    public Message(String message) {
    }

    public Message(String type, String content, String sender, int onlineUserCount) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.onlineUserCount = onlineUserCount;
    }

    public Message() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getOnlineCount() {
        return onlineUserCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineUserCount = onlineCount;
    }

    public String encode(Message message) throws EncodeException {
        return gson.toJson(message);
    }
    /*
    public String toJsonStr() {
        return "{"
                + "\"Content\": \"" + this.content + "\", "
                + "\"sender\": \"" + this.sender + "\", "
                + "\"type\": \"" + this.type + "\", "
                + "\"onlineUserCount\": \"" + this.onlineUserCount
                + "\" }";
    }

     */
}
