package com.huangjian.utilities;

import java.io.*;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ByteUtilities {

    // 压缩字节数组并返回Base64编码的字符串
    public static String encode(byte[] data) throws IOException {
        // 使用 GZIP 压缩数据
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(data);
        }

        // 获取压缩后的数据
        byte[] compressedData = byteArrayOutputStream.toByteArray();

        // 使用Base64编码压缩后的数据
        return Base64.getEncoder().encodeToString(compressedData);
    }

    // 解码并解压缩
    public static byte[] decode(String encodedData) throws IOException {
        // 使用Base64解码
        byte[] compressedData = Base64.getDecoder().decode(encodedData);

        // 解压缩数据
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            // 返回解压后的数据
            return outputStream.toByteArray();
        }
    }

//    public static void main(String[] args) throws IOException {
//        // 假设这是你的大数据
//        String largeData = "This is some really large data..."; // 替换为实际数据
//
//        // 压缩并编码数据
//        String encodedData = encode(largeData.getBytes());
//        System.out.println("Encoded and Compressed Data: " + encodedData);
//
//        // 解码并解压缩数据
//        byte[] decodedData = decode(encodedData);
//        System.out.println("Decoded Data: " + new String(decodedData));
//    }
}

