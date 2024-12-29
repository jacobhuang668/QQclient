package com.huangjian.utilities;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ManageSocketThreadCollection {
    private static final Map<String, Thread> socketThreadMap = new HashMap<>();


    public static void putThreadSocket(String userId, Thread thread) {

        System.out.println("Before putting: " + socketThreadMap);
        socketThreadMap.put(userId, thread);//如果你在调用时遇到并发问题，使用 putIfAbsent 来确保插入不被覆盖：
        System.out.println("Putting userId: " + userId + ", thread: " + thread.hashCode() + ",size:" + socketThreadMap.size());
        System.out.println("After putting: " + socketThreadMap);
        System.out.println("currentThread-hashcode:"+Thread.currentThread().hashCode()+",currentThread-name"+Thread.currentThread().getName());

    }



    public static void removeSocketThread(String userId) {
        System.err.println("Removing userId: " + userId);
        System.err.println("Before removing: " + socketThreadMap);
        socketThreadMap.remove(userId);
        System.err.println("After removing: " + socketThreadMap);
    }

    public static RetainSocketThread getThreadSocket(String userId) {
        return (RetainSocketThread)socketThreadMap.get(userId);
    }

}
