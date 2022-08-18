package cn.xpleaf.spider.client;

import cn.xpleaf.spider.core.pojo.Page;

import java.sql.PreparedStatement;
import java.sql.*;
/**
 * @author hong
 * @date 2022年08月17日 22:47
 */
public class MysqlClient {
    private static String URL = "jdbc:mysql://localhost:3306/ispider?useSSL=false&serverTimezone=Hongkong&characterEncoding=utf-8&autoReconnect=true";
    private static String NAME = "root";
    private static String PASS = "123456";
    private static PreparedStatement ps;
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, NAME, PASS);
            ////stmt = conn.createStatement();
            //ps = conn.prepareStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, NAME, PASS);
            ////stmt = conn.createStatement();
            //ps = conn.prepareStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static int insert(Page page){
        int row = 0;
        String sql = "insert into phone(id, source, brand, title, price, comment_count, url, img_url, params) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1,page.getId());
            ps.setString(2,page.getSource());
            ps.setString(3,page.getBrand());
            ps.setString(4,page.getTitle());
            ps.setFloat(5,page.getPrice());
            ps.setInt(6,page.getCommentCount());
            ps.setString(7,page.getUrl());
            ps.setString(8,page.getImgUrl());
            ps.setString(9,page.getParams());

            row = ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return row;

    }

}
