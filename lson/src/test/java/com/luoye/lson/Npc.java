package com.luoye.lson;

public class Npc extends MapObject{
    protected Skill skill;
    public Npc(){
        super();
    }
    public Npc(String name, double x, double y, int width, int height) {
        super(name, x, y, width, height);
    }

    public Npc(String name, double x, double y, int width, int height,Skill skill) {
        super(name, x, y, width, height);
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "Npc{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", skill=" + skill +
                '}';
    }
}
