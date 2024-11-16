package xianhhh.WebUi.Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class WebUiSocket {

    public static void send(){
        try {
            Socket socket = new Socket("127.0.0.1",8888);
            //2.可以从连接通道中获取输出流（OutputStream是引用型数据类型）
            OutputStream os = socket.getOutputStream();
            Scanner sc = new Scanner(System.in);
            while (true) {
                String str = sc.nextLine();
                if ("ClientFinal".equals(str)) {
                    break;
                }
                //写出数据
                os.write(str.getBytes());
            }
            //3.释放资源
            socket.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void accept(){
        try{
            ServerSocket ss = new ServerSocket(8888);

            Socket socket = ss.accept();

            InputStreamReader br = new InputStreamReader(socket.getInputStream());
            int b;
            while ((b = br.read()) != -1) {
                System.out.print((char) b);
            }
            //4.释放资源
            ss.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void start(){
        Thread t = new Thread(){
            @Override
            public void run() {
                accept();
            }
        };

        t.start();
    }

}
