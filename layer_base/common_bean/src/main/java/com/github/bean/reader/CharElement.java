package com.github.bean.reader;

public class CharElement {
    private final char content;
    private final int xStart;
    private final int yStart;
    private final int xEnd;
    private final int yEnd;
    private final int startEle;

    public CharElement(char content, int xStart, int yStart, int xEnd, int yEnd, int startEle) {
        this.content = content;
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.startEle = startEle;
    }

    public char getContent() {
        return content;
    }

    public int getXStart() {
        return xStart;
    }

    public int getYStart() {
        return yStart;
    }

    public int getXEnd() {
        return xEnd;
    }

    public int getYEnd() {
        return yEnd;
    }

    public int getStartEle() {
        return startEle;
    }
}
