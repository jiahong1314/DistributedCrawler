package cn.xpleaf.spider.client;

import cn.xpleaf.spider.core.pojo.Page;

import java.sql.PreparedStatement;
import java.sql.*;
import java.util.Map;

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
        String sql = "insert into phone(id, source, brand, title, price, comment_count, good_rate, url, img_url, main, battery, interface, network, operating_system, basic_info, camera, screen) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1,page.getId());
            ps.setString(2,page.getSource());
            ps.setString(3,page.getBrand());
            ps.setString(4,page.getTitle());
            ps.setFloat(5,page.getPrice());
            ps.setString(6,page.getCommentCount());
            ps.setFloat(7,page.getGoodRate());
            ps.setString(8,page.getUrl());
            ps.setString(9,page.getImgUrl());
//            ps.setString(9,page.getParams());
            Map paramDetails =  page.getParamDetails();
            ps.setString(10, (String) paramDetails.get("主体"));
            ps.setString(11, (String) paramDetails.get("电池信息"));
            ps.setString(12, (String) paramDetails.get("数据接口"));
            ps.setString(13, (String) paramDetails.get("网络支持"));
            ps.setString(14, (String) paramDetails.get("操作系统"));
            ps.setString(15, (String) paramDetails.get("基本信息"));
            ps.setString(16, (String) paramDetails.get("摄像头"));
            ps.setString(17, (String) paramDetails.get("屏幕"));

            row = ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return row;

    }

}
