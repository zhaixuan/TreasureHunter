package com.zhuoxin.treasurehunter.treasurehunter.user.register;

/**
 * Created by Dionysus on 2017/8/29.
 */

public class RegisterResult {

    /**
     * errcode : 1
     * errmsg : 登录成功！
     * tokenid : 171
     */

    private int errcode;
    private String errmsg;
    private int tokenid;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getTokenid() {
        return tokenid;
    }

    public void setTokenid(int tokenid) {
        this.tokenid = tokenid;
    }
}
