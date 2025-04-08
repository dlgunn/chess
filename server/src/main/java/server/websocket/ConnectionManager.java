package server.websocket;

import dataaccess.DataAccess;
import dataaccess.UserDAO;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();
//    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session, int gameID) {
        var connection = new Connection(authToken, session);
        ConcurrentHashMap<String, Connection> gameConnections = connections.get(gameID);
        if (gameConnections == null) {
            ConcurrentHashMap<String, Connection> map = new ConcurrentHashMap<>();
            map.put(authToken,connection);
            connections.put(gameID, map);
        } else {
            connections.get(gameID).put(authToken,connection);
        }
    }

    public void remove(String visitorName, int gameID) {
        ConcurrentHashMap<String, Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null) {
            gameConnections.remove(visitorName);
        }
    }

    public void broadcast(String excludeVisitorName, ServerMessage serverMessage, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        ConcurrentHashMap<String, Connection> gameConnections = connections.get(gameID);
        for (var c : gameConnections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeVisitorName)) {
                    c.send(serverMessage.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            gameConnections.remove(c.authToken);
        }
    }

}
