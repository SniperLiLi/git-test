package com.itheima.dao;

import com.itheima.domain.Items;

import java.util.List;

public interface ItemsDao  {

    public List<Items> findall () throws Exception;
}
