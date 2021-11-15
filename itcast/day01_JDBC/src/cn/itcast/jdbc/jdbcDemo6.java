package cn.itcast.jdbc;

import domain.Stu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/*
定义一个方法，查询stu表的数据 将其封装为对象，然后装载集合，返回。
 */
public class jdbcDemo6 {

    public static void main(String[] args) {

        List<Stu> list = new jdbcDemo6().findAll();
        System.out.println(list);
    }

    public List<Stu> findAll(){

        Connection conn =null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Stu> list =null;


        try {
            Class.forName("com.mysql.jdbc.Driver");

             conn = DriverManager.getConnection("jdbc:mysql:///db1", "root", "root");

            String sql = "select * from Stu";

             stmt = conn.createStatement();

             rs = stmt.executeQuery(sql);

             Stu stu = null;
             list = new ArrayList<Stu>();

            while (rs.next()){

                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                Double score = rs.getDouble("score");
                Date birthday = rs.getDate("birthday");

                stu = new Stu();
                stu.setId(id);
                stu.setAge(age);
                stu.setName(name);
                stu.setBirthday(birthday);
                stu.setScore(score);

                list.add(stu);

            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }


        return  list;
    }


}
