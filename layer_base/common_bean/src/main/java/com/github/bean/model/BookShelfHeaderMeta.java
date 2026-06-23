package com.github.bean.model;

public class BookShelfHeaderMeta {
    private boolean attendanced;
    private String background;
    private String link;
    private int readTime;
    private int redEnvelopeAmount;
    private boolean showFirstReward;

    public BookShelfHeaderMeta(boolean attendanced, String background, String link,
                               int readTime, int redEnvelopeAmount, boolean showFirstReward) {
        this.attendanced = attendanced;
        this.background = background;
        this.link = link;
        this.readTime = readTime;
        this.redEnvelopeAmount = redEnvelopeAmount;
        this.showFirstReward = showFirstReward;
    }

    public boolean isAttendanced() {
        return attendanced;
    }

    public void setAttendanced(boolean attendanced) {
        this.attendanced = attendanced;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getReadTime() {
        return readTime;
    }

    public void setReadTime(int readTime) {
        this.readTime = readTime;
    }

    public int getRedEnvelopeAmount() {
        return redEnvelopeAmount;
    }

    public void setRedEnvelopeAmount(int redEnvelopeAmount) {
        this.redEnvelopeAmount = redEnvelopeAmount;
    }

    public boolean isShowFirstReward() {
        return showFirstReward;
    }

    public void setShowFirstReward(boolean showFirstReward) {
        this.showFirstReward = showFirstReward;
    }
}
