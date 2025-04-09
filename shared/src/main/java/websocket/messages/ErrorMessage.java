package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private final String errorMessage;
    public ErrorMessage(ServerMessageType type, String msg) {
        super(type);
        this.errorMessage = msg;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
