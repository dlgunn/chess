package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(AuthData authdata) throws DataAccessException;

    void clear() throws DataAccessException;

}
