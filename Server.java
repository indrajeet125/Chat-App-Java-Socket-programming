import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;

public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Server() {
        try {
            server = new ServerSocket(7777);

            System.out.println("server is ready to accept Connetion waiting ..........");
            socket = server.accept();
            System.out.println("accepted ");
            br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReadig();
            startWriting();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReadig() {
        Runnable r1 = () -> {
            System.out.println("reading  started  from server  ");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("client terminated the chat  ");
                        socket.close();
                        break;
                    }
                    System.out.println("Client : " + msg);
                }
            } catch (Exception e) {
                System.out.println("connection close");

            }

        };
        new Thread(r1).start();

    }

    private void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writting   started.......from server   ");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(
                            new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }

            } catch (Exception e) {
                System.out.println("connection close");

            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Server");
        new Server();
    }
}