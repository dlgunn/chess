package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData createUser(UserData userdata);
    UserData getUser(String username);


}
