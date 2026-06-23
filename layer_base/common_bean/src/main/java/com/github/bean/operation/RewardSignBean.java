package com.github.bean.operation;

import android.text.TextUtils;

import com.fold.recyclyerview.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RewardSignBean {
    @SerializedName("sign")
    public SignBean sign;
    @SerializedName("task")
    public TaskBean task;
    public static class SignBean {
        @SerializedName("is_sign")
        public int isSign;
        @SerializedName("auto_sign_show")
        public int autoSignShow; // 0 不弹提示框 1 弹出提示框
        @SerializedName("sign_list")
        public List<SignListBean> signList;
        @SerializedName("sign_advertise")
        public SignAdvertise sign_advertise;


        public static class SignAdvertise {
            /**
             * task_id : 131
             * task_name : Watch video ads
             * task_desc : $ @ per video ad watched,completed
             * task_data : 10
             * specific_type : 5
             * prize_amount : 10
             * prize_type : 0
             * task_type : 3
             * reward : 10
             * total : 1
             */
            @SerializedName("task_id")
            public int task_id;
            @SerializedName("task_name")
            public String task_name;
            @SerializedName("task_desc")
            public String task_desc;
            @SerializedName("task_data")
            public String task_data;
            @SerializedName("specific_type")
            public int specific_type;
            @SerializedName("prize_amount")
            public int prize_amount;
            @SerializedName("prize_type")
            public int prize_type;
            @SerializedName("task_type")
            public int task_type;
            @SerializedName("reward")
            public int reward;
            @SerializedName("total")
            public int total;
        }

        public static class SignListBean {
            @SerializedName("date")
            public String date;
            @SerializedName("week")
            public int week;
            @SerializedName("is_sign")
            public int isSign;//0漏签1已签2未签
            @SerializedName("sign_rule_id")
            public String signRuleId;
            @SerializedName("sign_prize_id")
            public String signPrizeId;
            @SerializedName("sign_prize_amount")
            public String signPrizeAmount;
            @SerializedName("sign_prize_type")
            public int signPrizeType;

            @Override
            public String toString() {
                return "SignListBean{" +
                        "date='" + date + '\'' +
                        ", week=" + week +
                        ", isSign=" + isSign +
                        ", signRuleId='" + signRuleId + '\'' +
                        ", signPrizeId='" + signPrizeId + '\'' +
                        ", signPrizeAmount='" + signPrizeAmount + '\'' +
                        ", signPrizeType=" + signPrizeType +
                        '}';
            }
        }
    }

    public static class TaskBean {
        @SerializedName("novice_task_is_show")
        public int noviceTaskIsShow;
        @SerializedName("novice_task_list")
        public List<TaskItemBean> noviceTaskList;

        @SerializedName("daily_task_is_show")
        public int dailyTaskIsShow;
        @SerializedName("daily_task_list")
        public List<TaskItemBean> dailyTaskList;

        public static class TaskItemBean implements MultiItemEntity {
            @SerializedName("is_finish")
            public int isFinish; // 0 未完成 1 完成(未领取) 2 完成(已领取)
            @SerializedName("task_id")
            public String taskId;
            @SerializedName("task_type")
            public int taskType;
            @SerializedName("task_name")
            public String taskName;
            @SerializedName("task_desc")
            public String taskDesc;
            @SerializedName("task_data")
            public double taskData;
            @SerializedName("specific_type")
            public int specificType;
            @SerializedName("prize_amount")
            public String prizeAmount;
            @SerializedName("prize_type")
            public int prizeType;


            @SerializedName("already_used")
            public int already_used;

            @SerializedName("total_config")
            public int total_config;

            @SerializedName("reward")
            public int reward;

            @SerializedName("key_str")
            public String key_str;


            @Override
            public String toString() {
                return "TaskItemBean{" +
                        "isFinish=" + isFinish +
                        ", taskId='" + taskId + '\'' +
                        ", taskType=" + taskType +
                        ", taskName='" + taskName + '\'' +
                        ", taskDesc='" + taskDesc + '\'' +
                        ", taskData=" + taskData +
                        ", specificType=" + specificType +
                        ", prizeAmount='" + prizeAmount + '\'' +
                        ", prizeType=" + prizeType +
                        ", already_used=" + already_used +
                        ", total_config=" + total_config +
                        ", reward=" + reward +
                        ", key_str='" + key_str + '\'' +
                        '}';
            }

            public boolean isBeyondLimit(){
                return already_used >= total_config;
            }

            @Override
            public int getItemType() {
                if(!TextUtils.isEmpty(key_str) && key_str.equals("daily_task")){
                    return RewardTaskType.DailyAdvTask;
                }
                return RewardTaskType.CommonTask;
            }

        }

        @Override
        public String toString() {
            return "TaskBean{" +
                    "noviceTaskIsShow=" + noviceTaskIsShow +
                    ", noviceTaskList=" + noviceTaskList +
                    ", dailyTaskIsShow=" + dailyTaskIsShow +
                    ", dailyTaskList=" + dailyTaskList +
                    '}';
        }
    }



}
