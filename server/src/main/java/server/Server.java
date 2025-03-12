package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SQLDataAccess;
import model.AuthData;
import service.*;
import service.Service;
import spark.*;

import java.util.Map;

public class Server {
    public Service service;

    public Server() {
        DataAccess dataAccess = null;
        try {
            dataAccess = new SQLDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        service = new Service(dataAccess);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::handleRegister);
        Spark.delete("/session", this::handleLogout);
        Spark.post("/session", this::handleLogin);
        Spark.post("/game", this::handleCreateGame);
        Spark.get("/game", this::handleListGames);
        Spark.put("/game", this::handleJoinGame);
        Spark.delete("/db", this::handleClear);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(new Gson().toJson(Map.of("message", ex.getMessage())));
    }

    private Object handleClear(Request req, Response res) throws DataAccessException {
        service.clear();
        return new JsonObject();
    }

    private Object handleRegister(Request req, Response res) throws DataAccessException { // probably needs to throw an exception
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult registerResult = service.userService.register(registerRequest);
        return new Gson().toJson(registerResult);
    }

    private Object handleLogout(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        service.userService.logout(authToken);
        return new JsonObject();
    }

    private Object handleCreateGame(Request req, Response res) throws DataAccessException {
        authorize(req);
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        CreateGameResult createGameResult = service.gameService.createGame(createGameRequest);
        return new Gson().toJson(createGameResult);

    }

    private Object handleListGames(Request req, Response res) throws DataAccessException {
        authorize(req);
        ListGamesResult listGamesResult = service.gameService.listGames();
        return new Gson().toJson(listGamesResult);
    }

    private Object handleJoinGame(Request req, Response res) throws DataAccessException {
        AuthData authData = authorize(req);
        JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
        service.gameService.joinGame(joinGameRequest, authData.username());
        return new JsonObject();
    }

    private Object handleLogin(Request req, Response res) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult loginResult = service.userService.login(loginRequest);
        return new Gson().toJson(loginResult);
    }

    private AuthData authorize(Request req) throws DataAccessException {
        String authToken = req.headers("authorization");
        AuthData authData = service.gameService.dataAccess.authDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException(401, "Error: unauthorized");
        } else {
            return authData;
        }
    }

}
