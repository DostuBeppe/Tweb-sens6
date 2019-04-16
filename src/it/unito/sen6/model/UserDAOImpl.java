package it.unito.sen6.model;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static final String QUERY_INSERT_USER = "INSERT INTO user(username, email, password, admin) VALUES (?,?,?,?)";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM user";
    private static final String QUERY_SELECT_BY_ID = "SELECT * FROM user WHERE id=?";
    private static final String QUERY_SELECT_BY_EMAIL = "SELECT * FROM user WHERE email=?";
    private static final String QUERY_SELECT_BY_USERNAME = "SELECT * FROM user WHERE username=?";
    private static final String QUERY_SELECT_BY_EMAIL_PASSWORD = "SELECT * FROM user WHERE email=? AND password=?";
    private static final String QUERY_CANCEL_BY_ID = "UPDATE user SET active=FALSE WHERE id=?";
    private static final String QUERY_ACTIVATE_BY_ID = "UPDATE user SET active=TRUE WHERE id=?";

    /**
     * Inserisce un utente nel database.
     * @param user (oggetto utente contenente tutto tranne gli id)
     * @return id dell'utente appena inserito.
     */
    @Override
    public int insertUser(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,user.getUsername());
            ps.setString(2,user.getEmail());
            String encryptedPassword = encryptPassword(user.getPassword());
            ps.setString(3,encryptedPassword);
            ps.setBoolean(4,user.isAdmin());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }

        return result;
    }

    @Override
    public User selectUserById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_BY_ID);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next()){
                user = new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        rs.getBoolean(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return user;
    }

    @Override
    public boolean emailRegistered(String email) {
        return ProtectedMethods.alreadyExist(email, QUERY_SELECT_BY_EMAIL);
    }

    @Override
    public boolean usernameRegistered(String username) {
        return ProtectedMethods.alreadyExist(username, QUERY_SELECT_BY_USERNAME);
    }

    @Override
    public User selectUserByEmailPassword(String email, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_BY_EMAIL_PASSWORD);
            ps.setString(1,email);
            String encryptedPassword = encryptPassword(password);
            ps.setString(2,encryptedPassword);
            rs = ps.executeQuery();
            if (rs.next()){
                user = new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        rs.getBoolean(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_ALL);
            rs = ps.executeQuery();
            while (rs.next()){
                users.add(new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        rs.getBoolean(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return users;
    }

    @Override
    public boolean cancelUserById(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_ID);
    }

    @Override
    public boolean activateUserById(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_ACTIVATE_BY_ID);
    }

    private String encryptPassword(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (digest != null) {
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexBin.encode(hash);
        }
        return null;
    }

}
