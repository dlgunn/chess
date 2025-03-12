package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if (getUser(userData.username()) != null) {
            throw new DataAccessException(403, "Error: already taken");
        }
        var statement = "INSERT INTO userData (username, password, email, json) VALUES (?, ?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        var json = new Gson().toJson(new UserData(userData.username(),hashedPassword, userData.email()));
        SQLDataAccess.executeUpdate(statement, userData.username(), hashedPassword, userData.email(), json);
        return userData;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUserData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(500, e.getMessage());
        }
        return null;
    }

    @Override
    public boolean checkPassword(String givenPassword, String databasePassword) {
        return BCrypt.checkpw(givenPassword, databasePassword);
    }

    private UserData readUserData(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE userData";
        SQLDataAccess.executeUpdate(statement);
    }
}
