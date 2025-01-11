package com.huangjian.qqcommon;

public interface MessageType {
    String LOGIN_SUCCEED = "1";
    String LOGIN_FAIL= "2";

    //在线用户数量
    String ONLINE_USER_COUNT_COMMAND="3";
    String ONLINE_USER_COUNT_SUCCEED = "4";
    String ONLINE_USER_COUNT_FAIL = "5";
    //用户退出指令
    String USER_QUIT_COMMAND="6";
    //私聊指令
    String PRIVATE_CHAT_COMMAND="7";
    String PRIVATE_CHAT_SUCCEED="8";
    String PRIVATE_CHAT_FAIL="9";

    String HEART_BEAT_COMMAND="10";
    String SEND_FILE_COMMAND="11";
    String SEND_FILE_SUCCEED="12";
    String SEND_FILE_FAIL="13";

    //服务器群发消息
    String RECIVER_SERVER_MESSAGE="14";
    String RECIVER_SERVER_MESSAGE_SUCCESS="15";
    String RECIVER_SERVER_MESSAGE_FAIL="16";

}
