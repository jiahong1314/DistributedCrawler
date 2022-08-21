package cn.xpleaf.spider;

/**
 * @author yang
 * @date 2022年08月21日
 */

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.*;

public class Query {
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

    public static void main(String[] args) {
        try {
            query();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public static void query() throws IOException {
        Scanner scan = new Scanner(System.in); //声明一个Scanner对象，初始输入流为控制台
        Connection connection = getConnection();

        while (true) {
            Map<String, String> column_map = new HashMap<String, String>();
            column_map.put("商品Id", "id");
            column_map.put("商品来源", "source");
            column_map.put("商品品牌", "brand");
            column_map.put("商品标题", "title");
            column_map.put("商品价格", "price");
            column_map.put("商品评论数", "comment_count");
            column_map.put("商品好评率", "good_rate");
            column_map.put("商品地址", "url");
            column_map.put("商品图片地址", "img_url");
            column_map.put("商品主体", "main");
            column_map.put("电池信息", "battery");
            column_map.put("数据接口", "interface");
            column_map.put("网络支持", "network");
            column_map.put("操作系统", "operating_system");
            column_map.put("基本信息", "basic_info");
            column_map.put("摄像头", "camera");
            column_map.put("屏幕", "screen");

            String cnt_sql_all = "select count(*) from phone;";  //当前数据库条目
            Statement statement = null;
            String count = "";
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(cnt_sql_all);
                while (resultSet.next()) {
                    count  = resultSet.getString(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.println("欢迎来到爬虫系统查询模块！当前数据库内包含" + count + "条数据，其中每一条数据包含的可查询条目如下：");
            int cnt = 0;
            for (Map.Entry<String, String> entry : column_map.entrySet()) {
                String mapKey = entry.getKey();
                cnt += 1;
                System.out.print(cnt + " " + mapKey + "; ");
            }

            System.out.println("\n请输入查询条目：");
            String column = column_map.get(scan.nextLine());
            System.out.println("已接收查询条目：" + column);
            String sql = "SELECT * FROM phone WHERE " + column + " LIKE ?;";
            System.out.println("请输入查询关键词：");
            String keyword = "%" + scan.nextLine() + "%";
            System.out.println("已接收查询关键词：" + keyword);

            String cnt_sql = "select count(*) from phone where " + column + " LIKE '" + keyword + "';";;  //查询结果数量
            String res_count = "";
            try {
                ResultSet resultSet = statement.executeQuery(cnt_sql);
                while (resultSet.next()) {
                    res_count  = resultSet.getString(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.println("查询结束！已找到" + res_count + "条数据，请输入需要显示的条目（以空格分开，默认包括商品标题，价格和详细地址）：");
            String[] col_set = scan.nextLine().split(" ");

            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1,keyword);
                ResultSet res = ps.executeQuery();
                // 判断结果是否有值
                while (res.next()) {
                    System.out.println("商品标题: " + res.getString("title"));
                    System.out.println("商品价格: " + res.getString("price"));
                    System.out.println("商品详细信息地址: " + res.getString("url"));
                    for (String col: col_set) {
                        System.out.println(col + res.getString(column_map.get(col)));
                    }
                }
                System.out.println("输出结束！");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
