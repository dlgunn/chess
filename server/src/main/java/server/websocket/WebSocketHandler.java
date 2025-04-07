package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SQLDataAccess;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final DataAccess dataAccess;

    public WebSocketHandler() {
        {
            try {
                dataAccess = new SQLDataAccess();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
//            case MAKE_MOVE -> pass;
//            case LEAVE -> ;
//            case RESIGN -> ;
        }
    }

    private void connect(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException {
        connections.add(userGameCommand.getAuthToken(), session);
        GameData gameData = dataAccess.gameDAO.getGame(userGameCommand.getGameID());
        if (gameData == null) {
            String message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "This game doesn't exist").toString();
            session.getRemote().sendString(message);
            return;
        }
        AuthData authData = dataAccess.authDAO.getAuth(userGameCommand.getAuthToken());
        if (authData == null) {
            String message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not authorized").toString();
            session.getRemote().sendString(message);
            return;
        }

        String msg = new Gson().toJson(new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData));
        session.getRemote().sendString(msg);
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "fix later");
        //probably fix this next line
        connections.broadcast(userGameCommand.getAuthToken(), notificationMessage);
    }
}
