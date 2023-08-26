package com.luoye.lson;

import org.junit.Test;

import java.util.*;


public class LsonTest {

    @Test
    public void toJson() {
        Npc boss = new Npc("Boss",135.0f,758.51f,20,20,new Skill("fire",12));
        List<MapObject> mapObjectList = new ArrayList<>();
        mapObjectList.add(boss);
        for (int i = 0; i < 5; i++) {
            Npc npc = new Npc("npc" + i, i * 10 + 2,233.23f,40,20,new Skill("venom",4));
            mapObjectList.add(npc);
        }
        GameMap gameMap = new GameMap("village", 1024, 512, mapObjectList, new int[]{1,0,2,1,1,1,1,2});

        String json = Lson.toJson(gameMap);


        assert json != null;
        assert json.length() != 0;

        System.out.println(json);
    }

    @Test
    public void toObject() {
        String json = "{\"nullSet\":null,\"data\":[1,0,2,1,1,1,1,2],\"nullArray\":null,\"w\":1024,\"h\":512,\"mapObjects\":[{\"skill\":{\"name\":\"fire\",\"hurt\":12},\"name\":\"Boss\",\"x\":135,\"width\":20,\"y\":758,\"height\":20},{\"skill\":{\"name\":\"venom\",\"hurt\":4},\"name\":\"npc0\",\"x\":2,\"width\":40,\"y\":233,\"height\":20},{\"skill\":{\"name\":\"venom\",\"hurt\":4},\"name\":\"npc1\",\"x\":12,\"width\":40,\"y\":233,\"height\":20},{\"skill\":{\"name\":\"venom\",\"hurt\":4},\"name\":\"npc2\",\"x\":22,\"width\":40,\"y\":233,\"height\":20},{\"skill\":{\"name\":\"venom\",\"hurt\":4},\"name\":\"npc3\",\"x\":32,\"width\":40,\"y\":233,\"height\":20},{\"skill\":{\"name\":\"venom\",\"hurt\":4},\"name\":\"npc4\",\"x\":42,\"width\":40,\"y\":233,\"height\":20}],\"nullList\":null}";
        GameMap gameMap =  Lson.toObject(json, GameMap.class);
        assert gameMap != null;
        assert gameMap.getMapObjects() != null;
    }

}
