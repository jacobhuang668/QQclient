package com.huangjian.utilities;

import com.huangjian.dto.PublicPrivateKey;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAEncryption {

    public static final String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo1MIcRnRvvF9aMLgqahkKGg3RbTVJbZGOqepE3qX1o42QcFuNgmBmK1JLmGb1Fl+qc1HMZLhTyRfZDDDJb1EHjtp0w3Wy2KZm6ZOgwfLuu7O1SLVvN878fCOQ1V4akeZZtYOb+D8/wYNUDVt+Mm0L79RY/i5kSGPe20ibASyG5iJSqvOArRKDPyRFZegjU93aK/brVoXbrr2OhyoW1tqJt1tglUuap73P/3sJzK8AUyTZC+K91N4koUXC1jnGclx43mEDvlZDU/mGUjJP8N0msT9j1eLlH2RbyNpRHl9R466/e1bhBpeqVwgcutNe3WmQ4h/sBTQIHugX8qMIWhFYwIDAQAB";
    public static final String privateKeyStr = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCjUwhxGdG+8X1owuCpqGQoaDdFtNUltkY6p6kTepfWjjZBwW42CYGYrUkuYZvUWX6pzUcxkuFPJF9kMMMlvUQeO2nTDdbLYpmbpk6DB8u67s7VItW83zvx8I5DVXhqR5lm1g5v4Pz/Bg1QNW34ybQvv1Fj+LmRIY97bSJsBLIbmIlKq84CtEoM/JEVl6CNT3dor9utWhduuvY6HKhbW2om3W2CVS5qnvc//ewnMrwBTJNkL4r3U3iShRcLWOcZyXHjeYQO+VkNT+YZSMk/w3SaxP2PV4uUfZFvI2lEeX1Hjrr97VuEGl6pXCBy6017daZDiH+wFNAge6BfyowhaEVjAgMBAAECggEAJ/VOoW0xtcebaSAUmy2Jo6ErtLAzXdCkigEvCob50xJkD95lOm9E97edqe1TlLZ4mM6SVnybByPc+sQ7WmZd3g+nKm2+WXA8KKqdls03ePqoO9kT9euf3Od/xtWIamguDczdJ14iR7qRU0hfKXkfzna+UPkvXWSamBXxUAAuGzXoZO0JEupYSMRi5Dn9iYyr5z1lVR+pTL7qxjWdCHfGknYErZNXJeZXaso+JIRXqhCubYEnr7dHws2oGKswxENId2GpXuPIIOucvwe9jHxe61psuWfuYYBiGxVjjK+P//t+yN//b+Hdg9/lD+r8hvWT5mqrHm0Xcro0KnMwkMSuMQKBgQDCy+7HABgG4L7qlgWQnVotm1BPvhtiLwCYpSsfNwz6DVMWEpVA8RW8SRCznddBL3hYWu5AB68oacSYb7QBH5URwnS4UFG1e5VEbiYb1yN/wJ5h8oke2V+qIrlzC7SfrB6MUmFqGa47JGjw5lrqz82yZwaxpeNwfPaDweW3qfesRQKBgQDWo7FncjDKpKVvcAFp1WtRd6WIYbzdehizZb3Zg4PgLYU/A9GbQvjgQlsVn2H9s/eKKTnNNJFivgtD6VivcixAyv3SmfJHsHOvAYEPPsNYxbFaDvw4BR+R+/wjFdFGvC09gzxc9xAOWjvxsxLKCSbWUv+Hc8oC4PaFP3sWHqYJhwKBgHibjYaWz0mK44oZQu7MDiaIsEv/N7MtyN+5/B4Clr1HfLcA4HpqCnwefQHI65rHimqiOjtYxqFuAzQijT/YzxJE5MGtsTl9f+jzjhLUvHz/xNB+O1nnWqzbfRj3GWl3Rx64buojZlXl8DefWJFkAkbANUvOmyhtpq/jYJi72sZFAoGAbK42BcssKW901RhBnvURgg6zIntaxkosd6nsl1agT8K81Ut5tsQSgHsY47nwRCX8EVkOi3kypGzWB20AGtYU0jn2i8yngOU3rkV0s+Nzxl9TjeZIj5UIh0KwSJOjg82EmsD4r5np51qJP7kMqoA3yg38lPlsMEj4ij+6c1jRecECgYAGWnPG1J7jPhH8GpsGXyESkZsXeYq+txHi6UeobR7p1XImKpYB5tI6dBwpXb+WmIj7UNYTwLxVFEipxg9mahSwVfJGMBqlbMmrNyfatJSYnZr6/wFzb1f5sZMdfDJcj/Mr134426UyFvrE1YvnNM9QNVPHem7BmRiBz8RR5Fpk+A==";

    // 生成密钥对
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // 可以根据需要调整密钥长度
        return keyGen.generateKeyPair();
    }

    // 公钥加密
    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 私钥解密
    public static String decrypt(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, "UTF-8");
    }

    // 从字符串中获取公钥
    public static PublicKey getPublicKeyFromString(String publicKeyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    // 从字符串中获取私钥
    public static PrivateKey getPrivateKeyFromString(String privateKeyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    // 示例用法
    public static void main(String[] args) {
        try {
            // 生成密钥对
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 将密钥转换为字符串（Base64编码）
            String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            System.out.println("publicKeyStr:" + publicKeyStr);
            System.out.println("privateKeyStr:" + privateKeyStr);
            // 原始文本
            String originalText = "Hello, RSA Encryption!";

            // 加密
            String encryptedText = encrypt(originalText, publicKey);
            System.out.println("Encrypted Text: " + encryptedText);

            // 解密
            String decryptedText = decrypt(encryptedText, privateKey);
            System.out.println("Decrypted Text: " + decryptedText);


            PublicKey publicKeyFromString = getPublicKeyFromString(publicKeyStr);
            String encryptedContent = encrypt(originalText, publicKeyFromString);

            PrivateKey privateKeyFromString = getPrivateKeyFromString(privateKeyStr);
            String decrypt = decrypt(encryptedText, privateKeyFromString);
            System.out.println("Encrypted Text: " + encryptedContent);
            System.out.println("Decrypted Text: " + decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getEncryptedContent(String originalContent) {

        try {
            PublicKey publicKeyFromString = getPublicKeyFromString(publicKeyStr);
            String encryptedContent = encrypt(originalContent, publicKeyFromString);
            return encryptedContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDecryptedContent(String encryptedContent) {
        try {
            PrivateKey privateKeyFromString = getPrivateKeyFromString(privateKeyStr);
            String decrypt = decrypt(encryptedContent, privateKeyFromString);
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}