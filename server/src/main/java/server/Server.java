package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import service.RegisterRequest;
import service.RegisterResult;
import service.Service;
import spark.*;

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
//        Spark.post("/user", this::addUser);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object handleRegister(Request req, Response res) { // probably needs to throw an exception
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult registerResult = service.userService.register(registerRequest);
        return new Gson().toJson(registerResult);
    }

//    private Object addUser(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
//    }
}
