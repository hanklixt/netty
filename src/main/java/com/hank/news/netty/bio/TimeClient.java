package com.hank.news.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-23 13:55
 */
public class TimeClient {

    public static void main(String[] args) {
        int port=6111;
        BufferedReader in=null;
        PrintWriter out=null;
        Socket socket=null;
        try {
             socket = new Socket("127.0.0.1",port);
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream(),true);
            String queryTime = "QueryTime";
            System.out.println("send"+queryTime);
            out.write(queryTime);
            String resp = in.readLine();
            System.out.println("client get" + resp);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
