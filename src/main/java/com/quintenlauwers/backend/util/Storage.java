package com.quintenlauwers.backend.util;

import com.quintenlauwers.entity.DnaEntity;

import java.util.HashMap;

/**
 * Created by quinten on 10/08/16.
 */
public class Storage {
    HashMap<String, DnaEntity> entityHashMap;

    public Storage() {
        entityHashMap = new HashMap<String, DnaEntity>();
    }

    public void addEntity(int ID, DnaEntity entity) {
        entityHashMap.put(Integer.toString(ID), entity);
    }

    public DnaEntity getById(int ID){
        return entityHashMap.get(Integer.toString(ID));
    }

    public DnaEntity removeById(int ID){
        return entityHashMap.remove(Integer.toString(ID));
    }
}
