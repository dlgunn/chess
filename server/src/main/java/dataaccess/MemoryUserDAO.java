package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String, UserData> allUserData = new HashMap<>();

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if (getUser(userData.username()) != null) {
            throw new DataAccessException(403, "Error: already taken");
        }
        allUserData.put(userData.username(), userData);
        return userData;
    }

    @Override
    public UserData getUser(String username) {
        return allUserData.get(username);
    }

    @Override
    public boolean checkPassword(String givenPassword, String databasePassword) {
        return givenPassword.equals(databasePassword);
    }

    @Override
    public void clear() {
        allUserData.clear();
    }
}
