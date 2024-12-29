package com.huangjian.utilities;

import com.alibaba.fastjson2.JSON;
import com.huangjian.qqcommon.Message;
import com.huangjian.qqcommon.MessageType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;

public class RetainSocketThread extends Thread {
    private Socket socket;
    public RetainSocketThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //与服务器保持持久连接（接收服务器信息）
            while (true) {
                //block code
                byte[] bytes = ByteUtilies.readBytes(socket);
                System.out.println("client:"+bytes.length);
                Message message = JSON.parseObject(new String(bytes), Message.class);
                if (MessageType.ONLINE_USER_COUNT_SUCCEED.equals(message.getMsgType())) {
                    System.out.println(message);
                } else if (MessageType.PRIVATE_CHAT_SUCCEED.equals(message.getMsgType())) {
                    System.out.println("用户：" + message.getSender() + ",向你发送的消息：" + message.getContent());
                } else if (MessageType.PRIVATE_CHAT_FAIL.equals(message.getMsgType())) {
                    System.out.println("用户：" + message.getSender() + ",向你发送的消息失败");
                } else if (MessageType.SEND_FILE_SUCCEED.equals(message.getMsgType())) {
                    System.out.println("用户：" + message.getSender() + ",向你发送文件");
                    System.out.println("1：接受，0:拒绝");
                    String s = Utility.readString(1);
                    if("1".equals(s)){
                        String defaultFilePath="/Users/huanghuangjian/Downloads/filecopy/";
                        String content = message.getContent();
                        String[] realContent = content.split("$");
                        String encodeFile = realContent[0];
                        String filename = realContent[1];
                        FileOutputStream fos=new FileOutputStream(defaultFilePath);
                        byte[] decode = ByteUtilities.decode(encodeFile);
                        File file=new File(defaultFilePath+filename);
                        if(!file.exists()){
                           file.createNewFile();
                        }else{
                            file.delete();
                        }
                        fos.write(decode);
                        fos.close();
                    }else{
                        Message clientMsg=new Message();
                        clientMsg.setContent("用户:"+message.getReceiver()+"拒绝接收");
                        clientMsg.setSender(message.getReceiver());
                        clientMsg.setReceiver(message.getSender());
                        clientMsg.setSendTime(LocalDate.now());
                        String jsonString = JSON.toJSONString(clientMsg);
                        ByteUtilies.writeBytes(this.socket,jsonString.getBytes());
                    }
                }else if(MessageType.SEND_FILE_FAIL.equals(message.getMsgType())){
                    System.err.println(message.getContent());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
