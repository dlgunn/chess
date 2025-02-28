package service;

import dataaccess.DataAccess;

public class Service {
    public final DataAccess dataAccess;
    public UserService userService;
    public GameService gameService;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);
    }

    public void clear() {
        dataAccess.clear();
    }


}
