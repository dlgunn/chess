package service;

import dataaccess.DataAccess;
import model.AuthData;
import model.UserData;

public class UserService {
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }


    public RegisterResult register(RegisterRequest registerRequest) {
        UserData userData = dataAccess.userDAO.getUser(registerRequest.username());
        if (userData != null) {
            return null;
        }
        userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        dataAccess.userDAO.createUser(userData);
        AuthData authData = dataAccess.authDAO.createAuth(new AuthData(null, userData.username()));
        return new RegisterResult(authData.username(), authData.authToken());
    }
    //public LoginResult login(LoginRequest loginRequest) {}
    //public void logout(LogoutRequest logoutRequest) {}
}
