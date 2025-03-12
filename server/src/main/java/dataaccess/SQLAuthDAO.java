package dataaccess;

import com.google.gson.Gson;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        authData = new AuthData(authToken, authData.username());
        var statement = "INSERT INTO authData (authToken, username, json) VALUES (?, ?, ?)";
        var json = new Gson().toJson(authData);
        var id = SqlDataAccess.executeUpdate(statement, authData.authToken(), authData.username(), json);
        return authData;
    }



    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM authdata WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuthData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(500, e.getMessage());
        }
        return null;
    }


    private AuthData readAuthData(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, AuthData.class);
    }

    @Override
    public void deleteAuth(AuthData authdata) throws DataAccessException {
        var statement = "DELETE FROM authdata WHERE authToken=?";
        SqlDataAccess.executeUpdate(statement, authdata.authToken());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE authdata";
        SqlDataAccess.executeUpdate(statement);

    }
}
