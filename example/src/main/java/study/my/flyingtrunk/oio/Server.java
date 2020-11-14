package study.my.flyingtrunk.oio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author liuhaoxing
 * @date 2020-10-29
 **/
public class Server {


    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8328);
        //1. 一直阻塞，知道建立一个连接
        Socket clientSocket = serverSocket.accept();
        System.out.println("完成连接建立");
        //2. 获取输入流
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        String request, response;
        while ((request = in.readLine()) != null) {
            if ("Done".equals(request)) {
                break;
            }
            response = "xxx";
            // 处理响应
        }
    }

}
