package it.unito.sen6.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class ProtectedMethods {

    static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception rse) {
            rse.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception sse) {
            sse.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception cse) {
            cse.printStackTrace();
        }
    }

    static boolean toggleActive(int id, String query){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean canceled = false;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1,id);
            canceled = ps.executeUpdate()>0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, ps, rs);
        }
        return canceled;
    }

    static boolean alreadyExist(String attr, String query){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isPresent = false;
        try{
            conn= DAOFactory.createConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1,attr);
            rs = ps.executeQuery();
            if (rs.next()){
                isPresent = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, ps, rs);
        }
        return isPresent;
    }
}
