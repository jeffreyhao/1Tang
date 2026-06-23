package com.benefit.novelverse.event;

/**
 * eventbus事件
 * Sticky事件因为处理完要根据事件class类型移除，一般需要单独定义
 */
public class Event<T> {
    /**
     * 事件码，指定不同类型事件
     */
    public int code;
    /**
     * 事件优先级
     * 值越小优先级越低
     */
    public int priority;
    /**
     * 传递的数据
     */
    public T data;

    public Event() {
    }

    public Event(int code, int priority, T data) {
        this.code = code;
        this.priority = priority;
        this.data = data;
    }

    public Event(int code) {
        this.code = code;
    }

    public Event(int code, int priority) {
        this.code = code;
        this.priority = priority;
    }
}
