package it.unito.sen6.model;

import java.util.List;

public interface UserDAO {
    int insertUser(User user);
    User selectUserById(int id);
    boolean emailRegistered(String email);
    boolean usernameRegistered(String username);
    User selectUserByEmailPassword(String email, String password);
    List<User> selectAllUsers();
    boolean cancelUserById(int id);
    boolean activateUserById(int id);
}
