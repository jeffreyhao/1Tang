package com.github.bean.app;

import com.google.gson.annotations.SerializedName;

public class ProblemBean {
    @SerializedName("id")
    public String id;
    @SerializedName("typename")
    public String typename;
    @SerializedName("question")
    public String question;
    @SerializedName("answer")
    public String answer;


    public ProblemBean(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}