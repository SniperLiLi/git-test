package cn.itcast.jdbc;

import java.sql.*;

public class jdbcDemo5 {


    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql:///db6", "root", "root");

            String sql = "select *from student ";

            stmt = conn.createStatement();

             rs = stmt.executeQuery(sql);

             while(rs.next()){

                 //循环判断游标是否有下一行
                 int id = rs.getInt(1);
                 String name = rs.getString("name");
                 int age = rs.getInt(3);
                 String classes = rs.getString("classes");

                 System.out.println(id + "---"+ name + "---" + age + "---" + classes);




             }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
