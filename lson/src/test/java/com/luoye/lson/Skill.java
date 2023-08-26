package com.luoye.lson;

public class Skill {
    public Skill(){
    }
    public Skill(String name, int hurt) {
        this.name = name;
        this.hurt = hurt;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "name='" + name + '\'' +
                ", hurt=" + hurt +
                '}';
    }

    private String name;
    private int hurt;
}
