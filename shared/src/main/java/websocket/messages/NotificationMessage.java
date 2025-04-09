package websocket.messages;

public class NotificationMessage extends ServerMessage {
    private String message;

    public NotificationMessage(ServerMessageType type, String msg) {
        super(type);
        message = msg;
    }

    public String getMessage() {
        return this.message;
    }
}
