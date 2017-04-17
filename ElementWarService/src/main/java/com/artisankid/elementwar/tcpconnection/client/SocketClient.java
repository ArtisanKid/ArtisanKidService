package com.artisankid.elementwar.tcpconnection.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by ws on 2017/4/15.
 */
public class SocketClient {

    public static void main(String[] args) throws Exception{
        //客户端
        //1、创建客户端Socket，指定服务器地址和端口
        Socket socket = new Socket("127.0.0.1", 5168);
        //2、获取输出流，向服务器端发送信息
        OutputStream os = socket.getOutputStream();//字节输出流
        os.write(("sssdddddddddddddddddddddddddddddddddddddddddddddd" ).getBytes());
        os.close();
        socket.close();
        System.out.println("finished");
//        //3、获取输入流，并读取服务器端的响应信息
//        InputStream is = socket.getInputStream();
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String info = null;
//        while ((info = br.readLine()) !=null){
//            System.out.println("Hello,我是客户端，服务器说：" + info);
//        }
//
//        //4、关闭资源
//        br.close();
//        is.close();
//        pw.close();
//        os.close();
//        socket.close();
    }
}
