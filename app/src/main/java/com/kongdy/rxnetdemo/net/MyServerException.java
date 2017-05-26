package com.kongdy.rxnetdemo.net;

/**
 * @author kongdy
 *         on 2016/8/4
 * 服务器错误
 */
public class MyServerException extends RuntimeException {

    private final String state;
    private String displayMessage;
    public static final String RETRY_FLAG = "retryFlag";
    /**
     *  不进行任何作为
     */
    public static final String DO_NOTHING = "doNothing";

    public MyServerException(Throwable throwable, String state, String displayMessage) {
        super(displayMessage,throwable);
        this.state = state;
        this.displayMessage = displayMessage;
    }

    public MyServerException(String state, String displayMessage) {
        this.state = state;
        this.displayMessage = displayMessage;
    }

    public String getState() {
        return state;
    }
    public String getDisplayMessage() {
        return displayMessage;
    }
    public void setDisplayMessage(String msg) {
        this.displayMessage = msg + "(code:" + state + ")";
    }
}
