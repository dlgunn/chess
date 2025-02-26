package dataaccess;
import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(AuthData authData); // maybe add error handling like it has in Pet shop
    AuthData getAuth(String authToken);
    AuthData deleteAuth(AuthData authdata);
}
