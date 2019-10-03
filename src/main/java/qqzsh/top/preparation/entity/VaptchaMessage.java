package qqzsh.top.preparation.entity;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-20 15:03
 * @description Vaptcha服务器端验证返回消息封装
 */
public class VaptchaMessage {

    //验证结果，1为通过，0为失败
    private Integer success;

    //可信度，区间[0, 100]
    private Integer score;

    //信息描述
    private String msg;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}

