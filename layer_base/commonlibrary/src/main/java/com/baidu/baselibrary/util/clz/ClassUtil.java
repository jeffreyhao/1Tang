package com.baidu.baselibrary.util.clz;

import android.text.TextUtils;

import com.baidu.baselibrary.util.sys.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

/**
 * Created by haojiangfeng on 2023/7/14.
 */
public class ClassUtil {


    public static Field getFieldInClass(Class cl, String paramString) {
        if (cl == null) return null;
        Field field = null;
        for (; field == null && cl != null; ) {
            try {
                field = cl.getDeclaredField(paramString);
                if (field != null) {
                    field.setAccessible(true);
                }
            } catch (Exception e) {

            }
            if (field == null) {
                cl = cl.getSuperclass();
            }
        }
        return field;
    }

    public static Object getField(Object paramClass, String paramString) {
        if (paramClass == null) return null;
        Field field = null;
        Object object = null;
        Class cl = paramClass.getClass();
        for (; field == null && cl != null; ) {
            try {
                field = cl.getDeclaredField(paramString);
                if (field != null) {
                    field.setAccessible(true);
                }
            } catch (Exception e) {

            }
            if (field == null) {
                cl = cl.getSuperclass();
            }
        }
        try {
            if (field != null)
                object = field.get(paramClass);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void setField(Object paramClass, String paramString,
                                Object newClass) {
        if (paramClass == null || TextUtils.isEmpty(paramString)) return;
        Field field = null;
        Class cl = paramClass.getClass();
        for (; field == null && cl != null; ) {
            try {
                field = cl.getDeclaredField(paramString);
                if (field != null) {
                    field.setAccessible(true);
                }
            } catch (Throwable e) {

            }
            if (field == null) {
                cl = cl.getSuperclass();
            }
        }
        if (field != null) {
            try {
                field.set(paramClass, newClass);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            System.err.print(paramString + " is not found in " + paramClass.getClass().getName());
        }
        return;
    }

    public static void setFieldAllClass(Object paramClass, String paramString,
                                        Object newClass) {
        if (paramClass == null || TextUtils.isEmpty(paramString)) return;
        Field field;
        Class cl = paramClass.getClass();
        for (; cl != null; ) {
            try {
                field = cl.getDeclaredField(paramString);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(paramClass, newClass);
                }
            } catch (Throwable e) {

            }
            cl = cl.getSuperclass();
        }

        return;
    }



    public static boolean instanceOfClass(Class a, Class b) {
        Class classA = a;
        while (classA != null) {
            if (classA.equals(b)) return true;
            classA = classA.getSuperclass();
        }
        return false;
    }


    public static Method getMethod(Class cl, String name, Class... parameterTypes) {
        Method method = null;
        for (; method == null && cl != null; ) {
            try {
                method = cl.getDeclaredMethod(name, parameterTypes);
                if (method != null) {
                    method.setAccessible(true);
                }
            } catch (Exception e) {

            }
            if (method == null) {
                cl = cl.getSuperclass();
            }
        }
        return method;
    }


    public static boolean invokeMethod(Object object, String methodName){
        Method clearMethod = getMethod(object.getClass(), methodName);
        if (clearMethod != null) {
            try {
                clearMethod.invoke(object);
                return true;
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
        return false;
    }



    public static Class<?> getClass(Type type) {
        if (type instanceof Class) {
            // Type is a class, return it directly
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            // Type is a parameterized type, get the raw type
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<?>) parameterizedType.getRawType();
        } else if (type instanceof TypeVariable) {
            // Type is a type variable, get the first bound
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            Type[] bounds = typeVariable.getBounds();
            if (bounds.length > 0) {
                return getClass(bounds[0]);
            }
        }
        // If the type is not a class, parameterized type, or type variable with a bound,
        // it may be a generic array type or a wildcard type, which do not have a Class representation.
        // You may need to handle these cases as well, depending on your requirements.
        return null;
    }


    public static  Type getClassType(Object obj, int index) {
        Type genType = obj.getClass().getGenericSuperclass();
        Type[] args = ((ParameterizedType) genType).getActualTypeArguments();
        if(index < args.length){
            return args[index];
        }
        return null;
    }

    public static <T> Class<T> getFirstGenericityClass(Object obj) {
        Class<?> clazz = getClass(getClassType(obj, 0));
        return (Class<T>) clazz;
    }


    /**
     * @return List<T>.class
     */
    public static <T> Type getListClassType(Object obj){
        Class<?> clazz = getClass(getClassType(obj, 0));
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return type;
    }


    public static Object methodInvoke(Object object, String methodName){
        Method clearMethod = getMethod(object.getClass(), methodName);
        if (clearMethod != null) {
            try {
                return clearMethod.invoke(object);
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
        return null;
    }


    public static boolean isClass(Object object, Class clazz){
        if(object == null || clazz == null){
            return false;
        }
        return object.getClass().getSimpleName().equals(clazz.getSimpleName());
    }

    public static boolean inClass(Object object, Class... classes){
        if(object == null || classes == null){
            return false;
        }
        for(Class clazz : classes){
            if(isClass(object, clazz)){
                return true;
            }
        }
        return false;
    }


    public static boolean inClassList(Object object, List<Class> classList){
        if(object == null || classList == null){
            return false;
        }
        for(Class clazz : classList){
            if(isClass(object, clazz)){
                return true;
            }
        }
        return false;
    }
}
