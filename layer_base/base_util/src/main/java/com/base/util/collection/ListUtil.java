package com.base.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ListUtil {

    public static <E> boolean isNotEmpty(Collection<E> list) {
        return list != null && !list.isEmpty();
    }

    public static <E> boolean isEmpty(Collection<E> list) {
        return list == null || list.isEmpty();
    }

    public static <E> boolean isNullOrEmpty(Collection<E> list){
        return list == null || list.isEmpty();
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return map != null && !map.isEmpty();
    }

    public static int size(List<?> list){
        return list == null ? 0 : list.size();
    }

    public static int size(Map<?, ?> map){
        return map == null ? 0 : map.size();
    }



    public static<E> String toString(List<E> list){
        if(list == null || list.size() == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            E e = list.get(i);
            if(i != 0){
                sb.append(", ");
            }
            sb.append(e.toString());
        }
        return sb.toString();
    }

    public static<E> String toString(E[] array){
        if(array == null || array.length == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length; i++){
            E e = array[i];
            if(i != 0){
                sb.append(", ");
            }
            sb.append(e.toString());
        }
        return sb.toString();
    }


    public static <T extends Replicable<T>> List<T> copy(List<T> list){
        if(ListUtil.isEmpty(list)){
            return list;
        }
        List<T> newList = new ArrayList<>();
        for(T item : list){
            newList.add(item.copy());
        }
        return newList;
    }


    public static <T> void moveToFirst(List<T> list, int position) {
        if (list == null || position < 0 || position >= list.size()) return;
        T item = list.remove(position);
        list.add(0, item);
    }


    public static boolean inList(int code, List<Integer> list){
        if(list == null || list.size() == 0){
            return false;
        }
        for(Integer item: list){
            if(item != null && code == item){
                return true;
            }
        }
        return false;
    }

    public static<T> List<T> noNull(List<T> list) {
        return list == null ? new ArrayList<>() : list;
    }

    public static<T> ArrayList<T> nonNull(ArrayList<T> list) {
        return list == null ? new ArrayList<>() : list;
    }

}
