package com.luoye.lson;

public class MapObject {
    protected String name;
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    public MapObject(){
    }

    public MapObject(String name, double x, double y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "MapObject{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
