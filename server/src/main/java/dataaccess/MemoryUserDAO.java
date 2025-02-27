package dataaccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String, UserData> allUserData = new HashMap<>();
    @Override
    public UserData createUser(UserData userData) {
        allUserData.put(userData.username(),userData);
        return userData;
    }

    @Override
    public UserData getUser(String username) {
        return allUserData.get(username);
    }

    @Override
    public void clear() {
        allUserData.clear();
    }
}
