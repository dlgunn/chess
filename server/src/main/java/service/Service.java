package service;

import dataaccess.DataAccess;

public class Service {
    private final DataAccess dataAccess;
    public UserService userService;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
        userService = new UserService(dataAccess);
    }


}
