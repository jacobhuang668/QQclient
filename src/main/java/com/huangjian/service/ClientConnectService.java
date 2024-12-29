package com.huangjian.service;

import com.alibaba.fastjson.JSON;
import com.huangjian.dto.Login;
import com.huangjian.qqcommon.Message;
import com.huangjian.qqcommon.MessageType;
import com.huangjian.qqcommon.User;
import com.huangjian.utilities.ByteUtilies;
import com.huangjian.utilities.ByteUtilities;
import com.huangjian.utilities.ManageSocketThreadCollection;
import com.huangjian.utilities.RetainSocketThread;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@SuppressWarnings({"all"})
public class ClientConnectService {
    private User user;

    // 登录方法，确保每次创建新的Socket连接
    public Login islogin(String userId, String password) {
        Login login = new Login();
        try {
            user = new User(userId, password);
            Socket socket = new Socket("127.0.0.1", 8083);  // 每次都创建新的Socket

            // 发送登录信息
            //这样做是为了解决粘包和拆包问题
            String jsonString = JSON.toJSONString(user);
            byte[] jsonArray = jsonString.getBytes();
            int length = jsonArray.length;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(ByteUtilies.intToBytes(length));  // 先写入数据长度
            outputStream.write(jsonArray);// 然后写入消息体
            socket.getOutputStream().write(outputStream.toByteArray());

            // 接收服务器返回消息
            byte[] bytes = ByteUtilies.readBytes(socket);
            Message message = JSON.parseObject(new String(bytes), Message.class);
            // 服务器返回登录成功消息
            if (MessageType.LOGIN_SUCCEED.equals(message.getMsgType())) {
                // 创建线程并启动
                RetainSocketThread retainSocketThread = new RetainSocketThread(socket);
                retainSocketThread.setName(userId + "-client-thread");
                retainSocketThread.start();
                System.out.println("client created new thread with hashCode: " + System.identityHashCode(retainSocketThread));
                // 将线程保存到集合中
                ManageSocketThreadCollection.putThreadSocket(userId, retainSocketThread);
                login.setLogin(true);
                login.setSocket(socket);
            } else {
                login.setLogin(false);
                System.out.println("信息校验错误!");
            }
            login.setSocket(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return login;
    }
    public void getOnlineUserCount(String userId) {
        RetainSocketThread retainSocketThread = ManageSocketThreadCollection.getThreadSocket(userId);
        Message message = new Message();
        message.setMsgType(MessageType.ONLINE_USER_COUNT_COMMAND);
        message.setSender(userId);
        message.setSendTime(LocalDate.now());
        message.setReceiver("服务器");
        String messageJsonString = JSON.toJSONString(message);
        try {
            ByteUtilies.writeBytes(retainSocketThread.getSocket(),messageJsonString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                retainSocketThread.getSocket().close();
            } catch (IOException ex) {
               ex.printStackTrace();
            }
        }
    }

    public void exit(String userId){
        RetainSocketThread retainSocketThread = ManageSocketThreadCollection.getThreadSocket(userId);
        try {
            Message message=new Message();
            message.setMsgType(MessageType.USER_QUIT_COMMAND);
            message.setSendTime(LocalDate.now());
            message.setSender(userId);
            message.setReceiver("server");
            String jsonString = JSON.toJSONString(message);
            ByteUtilies.writeBytes(retainSocketThread.getSocket(),jsonString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void privateChat(String senderUserId,String recipientUserId,String content){
        RetainSocketThread retainSocketThread = ManageSocketThreadCollection.getThreadSocket(senderUserId);
        try {
            Message message=new Message();
            message.setReceiver(recipientUserId);
            message.setSender(senderUserId);
            message.setContent(content);
            message.setSendTime(LocalDate.now());
            message.setMsgType(MessageType.PRIVATE_CHAT_COMMAND);
            String jsonString = JSON.toJSONString(message);
            ByteUtilies.writeBytes(retainSocketThread.getSocket(),jsonString.getBytes());

        } catch (Exception e) {
           e.printStackTrace();
        }

    }

    public void sendFile(String senderUserId,String receiver,String filePath){

        //通过文件路径获取文件
        try {
            RetainSocketThread threadSocket = ManageSocketThreadCollection.getThreadSocket(senderUserId);
            ByteArrayOutputStream bos =new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(filePath);
            int readIndex=0;
            byte[] bytes=new byte[1024];
            while((readIndex=fis.read(bytes))!=-1){
                bos.write(bytes,0,readIndex);
            }
            Path path = Paths.get(filePath);
            Message message=new Message();
            message.setMsgType(MessageType.SEND_FILE_COMMAND);
            message.setSender(senderUserId);
            message.setReceiver(receiver);
            message.setSendTime(LocalDate.now());
            message.setContent(ByteUtilities.encode(bos.toByteArray())+"$"+path.getFileName().toString());
            String jsonString = JSON.toJSONString(message);
            ByteUtilies.writeBytes(threadSocket.getSocket(),jsonString.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

///Users/huanghuangjian/Downloads/index.png
    }

}


