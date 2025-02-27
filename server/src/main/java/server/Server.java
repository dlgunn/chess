package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import service.*;
import service.Service;
import spark.*;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    public Service service;

    public Server() {
        DataAccess dataAccess = new MemoryDataAccess();
        service = new Service(dataAccess);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::handleRegister);
        Spark.delete("/session", this::handleLogout);
        Spark.post("/session", this::handleLogin);
        Spark.exception(DataAccessException.class, this::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(new Gson().toJson(Map.of("message", ex.getMessage())));
    }

    private Object handleRegister(Request req, Response res) throws DataAccessException { // probably needs to throw an exception
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult registerResult = service.userService.register(registerRequest);
        return new Gson().toJson(registerResult);
    }

    private Object handleLogout(Request req, Response res) {
        String authToken = req.headers("authorization");
        service.userService.logout(authToken);
        return new JsonObject();
    }

    private Object handleLogin(Request req, Response res) {
        LoginRequest loginRequest = new Gson().fromJson(req.body(),LoginRequest.class);
        LoginResult loginResult = service.userService.login(loginRequest);
        return new Gson().toJson(loginResult);
    }

//    private Object addUser(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
//    }
}
