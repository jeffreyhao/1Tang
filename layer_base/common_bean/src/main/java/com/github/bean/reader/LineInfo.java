package com.github.bean.reader;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;


public class LineInfo {


    public static LineInfo create(LineInfo lineInfo) {
        LineInfo result = new LineInfo(
                lineInfo.getLineText(),
                lineInfo.getParagraphId(),
                lineInfo.getStartCharIndex(),
                lineInfo.getEndCharIndex(),
                lineInfo.isPeriodFirst(),
                lineInfo.isPeriodLast()
        );
        result.setWhiteSpaceOffset(lineInfo.getWhiteSpaceOffset());
        result.setAreaRect(new Rect(lineInfo.getAreaRect()));
        return result;
    }


    /**
     * 单行文本
     */
    private final String lineText;

    /**
     * 想法/笔记索引
     */
    private final int paragraphId;

    /**
     *  选择文本的 起始位置/结束位置
     */
    private final int startCharIndex, endCharIndex;

    /**
     *  是否 段首/段尾
     */
    public boolean isPeriodFirst, isPeriodLast;

    /**
     * 行首的空格偏移量，即行文本中第一个非空格字符的位置索引
     */
    private int whiteSpaceOffset = 0;

    /**
     * 记录行内每个字符的详细信息，包括字符内容和绘制坐标
     */
    private final List<CharElement> content = new ArrayList<>();

    private Rect areaRect = new Rect();


    /**
     * 待绘制文本（单行）
     *
     * @param lineStr           单行文本
     * @param paragraphId       想法/笔记索引
     * @param startCharIndex    选择文本起始位置
     * @param endCharIndex      选择文本结束位置
     * @param isPeriodFirst     是否段首
     * @param isPeriodLast      是否段尾
     */
    public LineInfo(String lineStr,
                    int paragraphId,
                    int startCharIndex,
                    int endCharIndex,
                    boolean isPeriodFirst,
                    boolean isPeriodLast) {

        this.lineText = lineStr;
        this.paragraphId = paragraphId;
        this.startCharIndex = startCharIndex;
        this.endCharIndex = endCharIndex;
        this.isPeriodFirst = isPeriodFirst;
        this.isPeriodLast = isPeriodLast;

        String trim = lineStr.replaceAll("^\\s+", "");
        this.whiteSpaceOffset = lineStr.length() - trim.length();
    }

    public String getLineText() {
        return lineText;
    }

    public int getParagraphId() {
        return paragraphId;
    }

    public int getStartCharIndex() {
        return startCharIndex;
    }

    public int getEndCharIndex() {
        return endCharIndex;
    }

    public boolean isPeriodFirst() {
        return isPeriodFirst;
    }

    public boolean isPeriodLast() {
        return isPeriodLast;
    }

    public List<CharElement> getContent() {
        return content;
    }

    public Rect getAreaRect() {
        return areaRect;
    }

    public void setAreaRect(Rect areaRect) {
        this.areaRect = areaRect;
    }

    public int getWhiteSpaceOffset() {
        return whiteSpaceOffset;
    }

    public void setWhiteSpaceOffset(int whiteSpaceOffset) {
        this.whiteSpaceOffset = whiteSpaceOffset;
    }

}
