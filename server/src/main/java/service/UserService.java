package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
    public final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }


    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        dataAccess.userDAO.createUser(userData);
        AuthData authData = dataAccess.authDAO.createAuth(new AuthData(null, userData.username()));
        return new RegisterResult(authData.username(), authData.authToken());
    }

    public void clear() {
        dataAccess.clear();
    }

    public LoginResult login(LoginRequest loginRequest) {
        UserData userData = dataAccess.userDAO.getUser(loginRequest.username());
        if (userData == null || !Objects.equals(userData.password(), loginRequest.password())) {
            return null;
        }
        AuthData authData = dataAccess.authDAO.createAuth(new AuthData(null, userData.username()));
        return new LoginResult(userData.username(), authData.authToken());
    }
    public void logout(String authToken) {
        AuthData authData = dataAccess.authDAO.getAuth(authToken);
        if (authData != null) {
            dataAccess.authDAO.deleteAuth(authData);
        }
    }
}
