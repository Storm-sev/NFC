package com.efeiyi;

import com.efeiyi.utils.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public abstract class Entity {
    public Entity() {

    }

    public HashMap<String, String> toHashMap() throws java.lang.Exception {
        Field[] fields = getClass().getDeclaredFields();
        HashMap<String, String> map = new HashMap<>();
        for (Field field : fields) {
            if (!Util.isBaseType(field.getType())) {
                continue;
            }
            String fieldName = field.getName();
            String getMethodName = "get" + Util.firstUpperCase(fieldName);
            Method fieldMethod = getClass().getMethod(getMethodName);
            Object object = fieldMethod.invoke(this);
            String s = Util.baseTypeToString(object);
            map.put(fieldName, s);
        }
        return map;
    }



    public void parseHashMap(Map<String, String> map) throws java.lang.Exception {
        String className = (String) map.get("ClassName");
        Iterator iter = map.keySet().iterator();
        while(iter.hasNext()) {
            String key = (String) iter.next();
            if(key.equals("ClassName")) {
                continue;
            }
            String val = (String) map.get(key);
            Field field = null;
            try {
                field = getClass().getDeclaredField(key);
            } catch (java.lang.Exception e) {
                continue;
            }
            field.setAccessible(true);
            Type type = field.getGenericType();
            if(type.equals(int.class)) {
                field.setInt(this, Integer.parseInt(val));
            } else if(type.equals(long.class)) {
                field.setLong(this, Long.parseLong(val));
            } else if(type.equals(Short.class)) {
                field.setShort(this, Short.parseShort(val));
            } else if(type.equals(String.class)) {
                field.set(this, val);
            } else if(type.equals(Date.class)) {
                field.set(this, new Date(Long.parseLong(val)));
            } else if(type.equals(float.class)) {
                field.set(this, Float.parseFloat(val));
            } else if(type.equals(double.class)) {
                field.set(this, Double.parseDouble(val));
            }
        }
    }




    public Object getFieldValue(String fieldName) {
        String getMethodName = "get" + Util.firstUpperCase(fieldName);
        try {
            Method getMethod = getClass().getMethod(getMethodName);
            return getMethod.invoke(this);
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    public void setIdentify(String id) throws java.lang.Exception {
        String methodName = "setId";
        Method method = getClass().getMethod(methodName, String.class);
        method.invoke(this, id);
    }

    public String identity() {
        String getMethodName = "getId";
        try {
            Method getMethod = getClass().getMethod(getMethodName);
            return (String) getMethod.invoke(this);
        } catch (java.lang.Exception e) {
            return null;
        }
    }

}
