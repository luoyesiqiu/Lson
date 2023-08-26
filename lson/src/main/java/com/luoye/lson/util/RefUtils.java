package com.luoye.lson.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RefUtils {
    public static List<Field> getAllFieldsByClass(Class klass){
        List<Field> allField = new ArrayList<>();
        for(Field field : klass.getDeclaredFields()){
            allField.add(field);
        }
        while(true) {
            Class superClass = klass.getSuperclass();
            if(superClass != null && !superClass.getName().equals("java.lang.Object")) {
                klass = superClass;
                allField.addAll(Arrays.asList(superClass.getDeclaredFields()));
            }
            else{
                break;
            }
        }
        return allField;
    }
}
