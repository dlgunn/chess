package dataaccess;

import java.sql.SQLException;

public class MySqlDataAccess extends DataAccess {

    public MySqlDataAccess() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            
            """
    };

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, String.format("Error: %s", ex.getMessage()));
        }
    }
}
