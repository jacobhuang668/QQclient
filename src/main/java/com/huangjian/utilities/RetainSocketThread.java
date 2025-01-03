package com.huangjian.utilities;

import com.alibaba.fastjson2.JSON;
import com.huangjian.qqcommon.Message;
import com.huangjian.qqcommon.MessageType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class RetainSocketThread extends Thread {
    private Socket socket;
    private   byte[] accumulatedBytes;
    public RetainSocketThread(Socket socket) {
        this.socket = socket;
        this.accumulatedBytes = new byte[0]; // 初始状态为空数组
    }

    private StringBuffer sb = new StringBuffer();

    @Override
    public void run() {
        try {
            //与服务器保持持久连接（接收服务器信息）
            while (true) {
                //block code
                byte[] bytes = ByteUtilies.readBytes(socket);
                byte[] decode = ByteUtilies.decode(new String(bytes));
                Message message = JSON.parseObject(new String(decode), Message.class);
                if (MessageType.ONLINE_USER_COUNT_SUCCEED.equals(message.getMsgType())) {
                    System.out.println(message);
                } else if (MessageType.PRIVATE_CHAT_SUCCEED.equals(message.getMsgType())) {
                    System.out.println("用户：" + message.getSender() + ",向你发送的消息：" + message.getContent());
                } else if (MessageType.PRIVATE_CHAT_FAIL.equals(message.getMsgType())) {
                    System.out.println("用户：" + message.getSender() + ",向你发送的消息失败");
                } else if (MessageType.SEND_FILE_SUCCEED.equals(message.getMsgType())) {
                    String contentEncode = message.getContent();
                    byte[] contentDecode = ByteUtilies.decode(contentEncode);
                    //二进制不可以字符串
                    String content = new String(contentDecode);
                    //最后一次接受客户端消息
                    if (content.contains("END_OF_FILE")) {
                        String filePath="/Users/huanghuangjian/Downloads/filecopy/";
                        System.out.println("用户：" + message.getSender() + ",向你发送文件");
                        String fileName = content.replace("END_OF_FILE", "");
                        File file = new File(filePath + fileName);
                        if(file.exists()){
                            file.delete();
                        }
                        String defautFilePath = filePath + fileName;
                        //会自动生成文件
                        FileOutputStream fos = new FileOutputStream(defautFilePath);
                        fos.write(accumulatedBytes);
                        fos.close();
                        sb.setLength(0);
                    } else {
                        //持续接受客户端消息
                        byte[] accumulate = accumulate(contentDecode);
                        accumulatedBytes=accumulate;
                    }

                } else if (MessageType.SEND_FILE_FAIL.equals(message.getMsgType())) {
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

    /**
     * 累加字节数组并返回新的累加结果
     *
     * @param inputBytes 传入的字节数组
     * @return 新的累加字节数组
     */
    public  byte[] accumulate(byte[] inputBytes){
        if(inputBytes.length==0||inputBytes==null){
            return Arrays.copyOf(accumulatedBytes,accumulatedBytes.length);
        }
        // 创建新的数组用于存储累加结果
        byte[] newAccumulatedBytes=new byte[accumulatedBytes.length+inputBytes.length];
        // 拷贝已有数据
        System.arraycopy(accumulatedBytes,0,newAccumulatedBytes,0,accumulatedBytes.length);
        // 添加新的数据
        System.arraycopy(inputBytes,0,newAccumulatedBytes,accumulatedBytes.length,inputBytes.length);
        // 更新累加状态
        accumulatedBytes = newAccumulatedBytes;
        return Arrays.copyOf(accumulatedBytes, accumulatedBytes.length); // 返回累加结果
    }

    public Socket getSocket() {
        return socket;
    }

}
