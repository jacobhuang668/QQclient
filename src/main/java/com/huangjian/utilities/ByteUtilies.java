package com.huangjian.utilities;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class ByteUtilies {
    // 将整数转化为4字节
    public static byte[] intToBytes(int value) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (value >> (i * 8));
        }
        return bytes;
    }


    // 将4字节数组转换为整数
    public static int bytesToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value |= (bytes[i] & 0xFF) << (i * 8);
        }
        return value;
    }

    // 将对象转化为字节数组
    public static byte[] toByteArray(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);  // 写入对象
        return byteArrayOutputStream.toByteArray();  // 获取字节数组
    }

    // 将字节数组转换回对象
    public static Object fromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();  // 反序列化为对象

    }

    public static byte[] readBytes(Socket socket) throws Exception {
        InputStream inputStream = socket.getInputStream();
        byte[] lengthBytes = new byte[4];  // java int 长度字段占4字节
        inputStream.read(lengthBytes);
        int length = ByteUtilies.bytesToInt(lengthBytes);  // 获取数据包长度
        byte[] messageBytes = new byte[length];
        inputStream.read(messageBytes);
        return messageBytes;
    }

    public static void writeBytes(Socket socket, byte[] bytes) throws Exception {
        OutputStream outputStream = socket.getOutputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytesLength = intToBytes(bytes.length);// 假设长度字段占4字节
        bos.write(bytesLength);//写入数据长度
        bos.write(bytes);// 写入数据包
        outputStream.write(bos.toByteArray());
    }

    // 编码方法：将二进制数据编码为Base64字符串
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    // 解码方法：将Base64字符串解码为二进制数据
    public static byte[] decode(String encodedData) {
        return Base64.getDecoder().decode(encodedData);
    }

    // 获取文件扩展名的方法
    public static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // 如果没有扩展名，则返回空字符串
        }
        return fileName.substring(lastDotIndex + 1); // 获取扩展名
    }

}