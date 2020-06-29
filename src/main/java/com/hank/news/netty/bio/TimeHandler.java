package com.hank.news.netty.bio;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-23 11:54
 */
public class TimeHandler implements Runnable {

    private Socket socket;

    public TimeHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("begin");
        BufferedReader in=null;
        PrintWriter out=null;
        try {
            //打开流
            InputStream input =this.socket.getInputStream();
            in=new BufferedReader(new InputStreamReader(input));
            out=new PrintWriter(
              socket.getOutputStream(),true
            );
            StringBuilder sb = new StringBuilder();
            String currentTime=null;
            System.out.println("ready read");
            //读取body
            while (true){
                String s = in.readLine();
                if (s==null){
                    break;
                }
                sb.append(s);
            }
            String b = sb.toString();
            System.out.println("receive body"+b);
            //判断参数
            currentTime=b.equalsIgnoreCase("QueryTime")? LocalDateTime.now()
                   .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")):"bad request";
           out.write(currentTime);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //释放输入流，输出流，socket句柄信息
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out!=null){
                out.close();
            }
            if (socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket=null;
        }

    }
}
