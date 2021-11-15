package com.itheima.test;

import com.itheima.domain.Items;
import com.itheima.impl.ItemsDaoImpl;
import org.junit.Test;

import java.util.List;

public class ItemsTest {
    @Test
    public  void  findAll() throws Exception{
        ItemsDaoImpl itemsDao = new ItemsDaoImpl();
        List<Items> list = itemsDao.findall();
        for(Items items: list){

            System.out.println(items.getName());
        }

    }
}
