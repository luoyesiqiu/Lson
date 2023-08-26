package com.luoye.lson;

import com.luoye.lson.annotation.Alias;
import com.luoye.lson.annotation.Ignore;
import com.luoye.lson.util.RefUtils;
import com.luoye.lson.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Lson {
    public static <T> T toObject(String json, Class<T> klass) {
        if (StringUtils.isEmpty(json)) {
            throw new IllegalArgumentException("Json is empty.");
        }
        if (klass == null) {
            throw new IllegalArgumentException("Class is null.");
        }
        try {
            if (klass.isArray()) {
                JSONArray jsonArray = new JSONArray(json);
                return (T) parseArray(jsonArray, klass);
            } else if (isList(klass)) {
                JSONArray jsonArray = new JSONArray(json);
                return parseList(jsonArray, klass);
            } else if (isSet(klass)) {
                JSONArray jsonArray = new JSONArray(json);
                return parseSet(jsonArray, klass);
            } else {
                JSONObject jsonObject = new JSONObject(json);
                return parseJSONObject(jsonObject, klass);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T parseJSONObject(JSONObject jsonObject, Class<T> klass) {
        List<Field> allFieldsByClass = RefUtils.getAllFieldsByClass(klass);

        try {
            Object obj = klass.newInstance();
            for (Field field : allFieldsByClass) {
                field.setAccessible(true);
                boolean isFinal = (field.getModifiers() & Modifier.FINAL) == Modifier.FINAL;
                String fieldName = field.getName();
                Class fieldType = field.getType();
                Alias alias = field.getAnnotation(Alias.class);
                Ignore ignore = field.getAnnotation(Ignore.class);

                if (alias != null) {
                    fieldName = alias.value();
                }

                if (isFinal || ignore != null || !jsonObject.has(fieldName)) {
                    continue;
                }

                try {
                    if (isSimpleType(fieldType)) {
                        Object value = jsonObject.get(fieldName);
                        field.set(obj, value);

                    } else if (isSet(fieldType)) {
                        JSONArray value = jsonObject.getJSONArray(fieldName);
                        field.set(obj, parseSet(value, fieldType));
                    } else if (isList(fieldType)) {
                        JSONArray value = jsonObject.getJSONArray(fieldName);
                        field.set(obj, parseList(value, fieldType));
                    } else if (fieldType.isArray()) {
                        JSONArray value = jsonObject.getJSONArray(fieldName);
                        field.set(obj, parseArray(value, fieldType));
                    } else {
                        JSONObject value = jsonObject.getJSONObject(fieldName);
                        field.set(obj, parseJSONObject(value, fieldType));
                    }
                }
                catch (JSONException e){
                    //出现异常字段设为null
                    field.set(obj, null);
                }

            }
            return (T) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T parseSet(JSONArray jsonArray, Class<T> klass) throws JSONException {
        Set list = null;
        if (klass.getName().equals(Set.class.getName())) {
            list = new HashSet<T>();
        } else {
            try {
                list = (Set<T>) klass.newInstance();
            }
            catch (Exception e){
                String msg = String.format(Locale.US, "'%s' cannot cast to '%s'",klass.getName(),Set.class.getName());
                throw new IllegalArgumentException(msg);
            }

        }

        for (int i = 0; i < jsonArray.length(); i++) {
            Object o = jsonArray.get(i);
            list.add(o);
        }
        return (T) list;
    }

    private static <T> Object parseArray(JSONArray jsonArray, Class<T> klass) throws JSONException{
        Object[] t = null;
        if (klass.equals(Integer[].class)) {
            t = new Integer[jsonArray.length()];
        } else if (klass.equals(Double[].class)) {
            t = new Double[jsonArray.length()];
        } else if (klass.equals(Float[].class)) {
            t = new Float[jsonArray.length()];
        } else if (klass.equals(String[].class)) {
            t = new String[jsonArray.length()];
        } else if (klass.equals(Short[].class)) {
            t = new Short[jsonArray.length()];
        } else if (klass.equals(Byte[].class)) {
            t = new Byte[jsonArray.length()];
        } else if (klass.equals(Boolean[].class)) {
            t = new Boolean[jsonArray.length()];
        } else if (klass.equals(Long[].class)) {
            t = new Long[jsonArray.length()];
        } else if (klass.equals(int[].class) || klass.equals(byte[].class) || klass.equals(short[].class)) {
            int[] array = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                array[i] = jsonArray.getInt(i);
            }
            return array;
        } else if (klass.equals(double[].class) || klass.equals(float[].class)) {
            double[] array = new double[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                array[i] = jsonArray.getDouble(i);
            }
            return array;
        }  else if (klass.equals(long[].class)) {
            long[] array = new long[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                array[i] = jsonArray.getLong(i);
            }
            return array;
        }  else if (klass.equals(boolean[].class)) {
            boolean[] array = new boolean[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                array[i] = jsonArray.getBoolean(i);
            }
            return array;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            t[i] = jsonArray.get(i);
        }
        return t;
    }

    private static <T> T parseList(JSONArray jsonArray, Class<T> klass) throws JSONException{
        List list = null;
        if (klass.getName().equals(List.class.getName())) {
            list = new ArrayList<T>();
        } else {
            try {
                list = (List<T>) klass.newInstance();
            }catch (Exception e){
                String msg = String.format("'%s' cannot cast to '%s'",klass.getName(),List.class.getName());
                throw new IllegalArgumentException(msg);
            }

        }

        for (int i = 0; i < jsonArray.length(); i++) {
            Object o = jsonArray.get(i);
            list.add(o);
        }
        return (T) list;
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            if (obj.getClass().isArray() || isList(obj)) {
                return toJSONArray(obj).toString();
            }
            return toJSONObject(obj).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject toJSONObject(Object obj) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        try {
            Class<?> aClass = obj.getClass();
            List<Field> allFieldsByClass = RefUtils.getAllFieldsByClass(aClass);
            for (Field declaredField : allFieldsByClass) {
                declaredField.setAccessible(true);
                String fieldName = declaredField.getName();
                Object fieldValue = declaredField.get(obj);

                Alias aliasAnnotation = declaredField.getAnnotation(Alias.class);
                Ignore ignoreAnnotation = declaredField.getAnnotation(Ignore.class);

                String fieldAlias = null;
                boolean ignore = false;
                if (aliasAnnotation != null) {
                    fieldAlias = aliasAnnotation.value();
                }
                if (ignoreAnnotation != null) {
                    ignore = ignoreAnnotation.value();
                }

                if (ignore) {
                    continue;
                }

                String key = fieldAlias == null ? fieldName : fieldAlias;
                if (fieldValue != null) {
                    try {
                        Class filedClass = fieldValue.getClass();
                        if (isSimpleType(fieldValue)) {
                            jsonObject.put(key, declaredField.get(obj));
                        } else if (filedClass.isArray() || isList(fieldValue)) {
                            JSONArray jsonArray = toJSONArray(fieldValue);
                            jsonObject.put(key, jsonArray);
                        } else {
                            jsonObject.put(key, toJSONObject(fieldValue));
                        }
                    }
                    catch (JSONException e){
                        String msg = String.format(Locale.US, "Cause exception when put field name '%s' of %s.",fieldName,fieldValue.getClass().getName());
                        throw new IllegalArgumentException(msg);
                    }
                } else {
                    jsonObject.put(key, JSONObject.NULL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private static boolean isSimpleType(Class klass) {
        if (klass.equals(Integer.class)
                || klass.equals(Double.class)
                || klass.equals(Float.class)
                || klass.equals(String.class)
                || klass.equals(Short.class)
                || klass.equals(Byte.class)
                || klass.equals(Boolean.class)
                || klass.equals(Long.class)
                || klass.equals(int.class)
                || klass.equals(double.class)
                || klass.equals(float.class)
                || klass.equals(short.class)
                || klass.equals(byte.class)
                || klass.equals(boolean.class)
                || klass.equals(long.class)
        ) {
            return true;
        }
        return false;
    }

    private static boolean isSimpleType(Object obj) {
        if (obj instanceof Integer
                || obj instanceof Double
                || obj instanceof Float
                || obj instanceof String
                || obj instanceof Short
                || obj instanceof Byte
                || obj instanceof Boolean
                || obj instanceof Long) {
            return true;
        }
        return false;
    }

    private static boolean isList(Object obj) {
        if (obj instanceof List) {
            return true;
        }
        return false;
    }

    private static boolean isSet(Object obj) {
        if (obj instanceof Set) {
            return true;
        }
        return false;
    }

    private static boolean isList(Class klass) {
        if (klass == null) {
            return false;
        }
        if (klass.getName().equals(List.class.getName())) {
            return true;
        }
        return isImplInterface(klass, List.class);
    }

    private static boolean isSet(Class klass) {
        if (klass == null) {
            return false;
        }
        if (klass.getName().equals(Set.class.getName())) {
            return true;
        }

        return isImplInterface(klass, Set.class);
    }

    private static boolean isImplInterface(Class klass, Class interfaceClass) {
        if (klass == null) {
            return false;
        }
        Class currentClass = klass;
        while (!currentClass.getName().equals(Object.class.getName())) {
            Class[] implInterface = currentClass.getInterfaces();
            for (Class anInterface : implInterface) {
                if (anInterface.getName().equals(interfaceClass.getName())) {
                    return true;
                }
            }

            currentClass = currentClass.getSuperclass();
            if (currentClass == null) {
                break;
            }
        }
        return false;
    }

    private static JSONArray toJSONArray(Object array) throws JSONException{
        JSONArray jsonArray = new JSONArray();
        if (array instanceof Integer[]) {
            Integer[] typeArray = (Integer[]) array;
            for (Integer item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof Double[]) {
            Double[] typeArray = (Double[]) array;
            for (Double item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof Float[]) {
            Float[] typeArray = (Float[]) array;
            for (Float item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof String[]) {
            String[] typeArray = (String[]) array;
            for (String item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof Short[]) {
            Short[] typeArray = (Short[]) array;
            for (Short item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof Byte[]) {
            Byte[] typeArray = (Byte[]) array;
            for (Byte item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof Boolean[]) {
            Boolean[] typeArray = (Boolean[]) array;
            for (Boolean item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof Long[]) {
            Long[] typeArray = (Long[]) array;
            for (Long item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof int[]) {
            int[] typeArray = (int[]) array;
            for (Integer item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof double[]) {
            double[] typeArray = (double[]) array;
            for (Double item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof float[]) {
            float[] typeArray = (float[]) array;
            for (Float item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof short[]) {
            short[] typeArray = (short[]) array;
            for (Short item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof byte[]) {
            byte[] typeArray = (byte[]) array;
            for (Byte item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof boolean[]) {
            boolean[] typeArray = (boolean[]) array;
            for (Boolean item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof long[]) {
            long[] typeArray = (long[]) array;
            for (Long item : typeArray) {
                jsonArray.put(item);
            }
        } else if (array instanceof List) {
            List listNew = (List) array;
            for (int i = 0; i < listNew.size(); i++) {
                Object obj = listNew.get(i);
                if (isSimpleType(obj)) {
                    jsonArray.put(obj);
                } else {
                    JSONObject jsonObject = toJSONObject(obj);
                    jsonArray.put(jsonObject);
                }

            }
        }

        return jsonArray;
    }

}
