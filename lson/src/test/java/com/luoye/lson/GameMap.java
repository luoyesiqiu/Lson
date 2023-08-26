package com.luoye.lson;

import com.luoye.lson.annotation.Alias;
import com.luoye.lson.annotation.Ignore;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GameMap {
    @Ignore
    private String name;
    @Alias("w")
    private Integer width;
    @Alias("h")
    private Integer height;

    public String getName() {
        return name;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public List<MapObject> getMapObjects() {
        return mapObjects;
    }

    public int[] getData() {
        return data;
    }

    private List<MapObject> mapObjects;
    private int[] data;

    private List<Integer> nullList;
    private String[] nullArray;
    private Set<Integer> nullSet;

    public GameMap(){

    }
    public GameMap(String name, Integer width, Integer height, List<MapObject> mapObjects, int[] data) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.mapObjects = mapObjects;
        this.data = data;
    }

    @Override
    public String toString() {
        return "GameMap{" +
                "name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", mapObjects=" + mapObjects +
                ", data=" + Arrays.toString(data) +
                ", nullList=" + nullList +
                ", nullArray=" + Arrays.toString(nullArray) +
                ", nullSet=" + nullSet +
                '}';
    }
}
