package com.huangjian.utilities;

import com.alibaba.fastjson.JSONObject;
import com.github.luben.zstd.Zstd;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ByteUtilies {
    // 将整数转化为4字节
    public static byte[] intToBytes(int value) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (value >> (i * 8));
        }
        return bytes;
    }

    public ByteUtilies() {
        System.err.println("init ByteUtilies........");

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
    public static void writeBytes(Socket socket, byte[] bytes,String fileName) throws Exception {
        OutputStream outputStream = socket.getOutputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytesLength = intToBytes(bytes.length+fileName.getBytes().length);// 假设长度字段占4字节
        bos.write(bytesLength);//写入数据长度
        bos.write(bytes);// 写入数据包
        bos.write(fileName.getBytes());//写入文件名称
        outputStream.write(bos.toByteArray());
    }
    // 压缩字节数组并返回Base64编码的字符串
    public static String encode(byte[] data) throws IOException {
        return Base64.getEncoder().encodeToString(data);
    }

    // 解码并解压缩
    public static byte[] decode(String encodedData) throws IOException {
        // 使用Base64解码
         return Base64.getDecoder().decode(encodedData);
    }

    // 压缩 JSON 中的 "content" 字段
    public static JSONObject compressFileField(JSONObject json) {
        // 获取 "content" 字段
        String fileBase64 = json.getString("content");

        // 解码 base64 字符串为字节数组（假设file字段是base64编码的）
        byte[] fileData = Base64.getDecoder().decode(fileBase64);

        // 使用 Zstandard 压缩数据
        byte[] compressedData = Zstd.compress(fileData);

        // 将压缩后的数据编码为 base64 字符串，以便插入到 JSON 中
        String compressedBase64 = Base64.getEncoder().encodeToString(compressedData);

        // 更新 JSON 中的 "content" 字段为压缩后的数据
        json.put("content", compressedBase64);

        return json;
    }

    // 解压 JSON 中的 "content" 字段
    public static  byte[] decompressFileField(String compressedBase64) {

        // 解码 base64 字符串为压缩后的字节数组
        byte[] compressedData = Base64.getDecoder().decode(compressedBase64);

        // 使用 Zstandard 解压缩数据
        byte[] decompressedData = Zstd.decompress(compressedData, 1024 * 4);  // 预估解压后的最大大小

        return decompressedData;
    }

    public static byte[] mergeByteArrays(byte[] array1, byte[] array2) {
        // 创建一个新数组，其大小是两个原数组大小之和
        byte[] mergedArray = new byte[array1.length + array2.length];

        // 复制第一个数组到新数组
        System.arraycopy(array1, 0, mergedArray, 0, array1.length);

        // 复制第二个数组到新数组
        System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);

        return mergedArray;
    }

}