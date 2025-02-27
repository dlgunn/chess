package dataaccess;

import java.util.HashMap;
import java.util.UUID;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> allAuthData = new HashMap<>();
    public AuthData createAuth(AuthData authData) {
        String authToken = generateToken();
        authData = new AuthData(authToken, authData.username());
        allAuthData.put(authData.authToken(), authData);
        return authData;
    }
    public AuthData getAuth(String authToken) {
        return allAuthData.get(authToken);
    }
    public AuthData deleteAuth(AuthData authData) {
        allAuthData.remove(authData.authToken());
        return authData;
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void clear() {
        allAuthData.clear();
    }
}
