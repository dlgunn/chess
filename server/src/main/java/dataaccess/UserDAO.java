package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData createUser(UserData userdata) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear();


}
