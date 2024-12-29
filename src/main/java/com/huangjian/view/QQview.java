package com.huangjian.view;

import com.huangjian.dto.Login;
import com.huangjian.service.ClientConnectService;
import com.huangjian.utilities.Utility;

public class QQview {

    public static void main(String[] args) {
        new QQview().mainMenu();
        System.out.println("退出系统");
    }

    private void mainMenu() {
        boolean loop = true;
        ClientConnectService clientConnectService = new ClientConnectService();
        System.out.println("==========welcome to QQview==========");
        System.out.println("\t\t 1:用户登录");
        System.out.println("\t\t 9:退出系统");
        String key = Utility.readString(1);
        while (loop) {
            switch (key) {
                case "1":
                    System.out.println("请输入用户名:");
                    String useId = Utility.readString(10);
                    System.out.println("请输入密  码:");
                    String pwd = Utility.readString(10);
                    //users login with their credential
                    Login islogin = clientConnectService.islogin(useId, pwd);
                    if (islogin.isLogin()) {
                        System.out.println("======欢迎用户(" + useId + ")=======");
                        while (loop) {
                            System.out.println("======用户(" + useId + ")进入二级菜单=======");
                            System.out.println("\t\t 1:显示在线用户列表");
                            System.out.println("\t\t 2:群发消息");
                            System.out.println("\t\t 3:发起私聊");
                            System.out.println("\t\t 4:发送文件");
                            System.out.println("\t\t 9:退出系统");
                            System.out.println("请选择:");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    clientConnectService.getOnlineUserCount(useId);
                                    break;
                                case "2":
                                    System.out.println("群发消息");
                                    break;
                                case "3":
                                    System.out.println("请输入对方userId");
                                    String recipient = Utility.readString(10);
                                    System.out.println("请输入消息内容:");
                                    String content = Utility.readString(100);
                                    clientConnectService.privateChat(useId,recipient,content);
                                    break;
                                case "4":
                                    System.out.println("请输入对方userId");
                                    String receiver = Utility.readString(10);
                                    System.out.println("请输入文件路径:");
                                    String filePath = Utility.readString(100);
                                    clientConnectService.sendFile(useId,receiver,filePath);
                                    break;
                                case "9":
                                    loop = false;
                                    clientConnectService.exit(useId);
                                    System.exit(0);

                            }
                        }
                    } else {
                        System.out.println("登录失败");
                    }

                case "9":
                    loop = false;
                    break;
            }
        }
    }
}
