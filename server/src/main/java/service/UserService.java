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

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = dataAccess.userDAO.getUser(loginRequest.username());

        if (userData == null || !dataAccess.userDAO.checkPassword(loginRequest.password(), userData.password())) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
        AuthData authData = dataAccess.authDAO.createAuth(new AuthData(null, userData.username()));
        return new LoginResult(userData.username(), authData.authToken());
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = dataAccess.authDAO.getAuth(authToken);
        if (authData != null) {
            dataAccess.authDAO.deleteAuth(authData);
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }
}
