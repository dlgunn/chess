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
        throw new UnsupportedOperationException("Not implemented");
    }
    public AuthData deleteAuth(AuthData authdata) {
        throw new UnsupportedOperationException("Not implemented");

    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
