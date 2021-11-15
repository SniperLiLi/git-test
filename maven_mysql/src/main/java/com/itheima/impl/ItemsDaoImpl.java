package com.itheima.impl;

import com.itheima.dao.ItemsDao;
import com.itheima.domain.Items;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemsDaoImpl implements ItemsDao {
    public List<Items> findall() throws Exception{

        List<Items> list = new ArrayList<Items>();
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            //加载驱动类
            Class.forName("com.mysql.jdbc.Driver");

            //先获取contection对象
            connection = DriverManager.getConnection("jdbc:mysql:///db6","root","root");
            //获取真正操作数据库的对象
             pst = connection.prepareCall("select * from student");
            //执行数据库查询操作
            rs = pst.executeQuery();
            //把数据库结果集转成java的List集合
            while (rs.next()){
                Items items = new Items();
                items.setId(rs.getInt("id"));
                items.setName(rs.getString("name"));
                list.add(items);
        }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.close();
            pst.close();
            rs.close();


        }
        return list;




    }
}
