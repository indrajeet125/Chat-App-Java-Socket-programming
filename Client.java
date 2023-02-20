import java.io.*;
import java.net.Socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // declare component

    JLabel heading = new JLabel("Clinet area");
    JTextArea messageArea = new JTextArea();
    JTextField messageInput = new JTextField();
    Font font = new Font("Robot", Font.PLAIN, 20);

    public Client() {

        // contructor
        try {
            System.out.println("sending request to server ...");
            socket = new Socket("localhost", 7777);
            System.out.println("connected ");

            br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleInput();
            startReadig();
            // startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleInput() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    messageArea.append("me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    System.out.println("you have pressed enter button \n");
                }

            }

        });

    }

    private void createGUI() {
        // gui code

        this.setTitle("Clinet message [END]");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // setting layout

        this.setLayout(new BorderLayout());
        JScrollPane jScrollPane = new JScrollPane(messageArea);

        this.add(heading, BorderLayout.NORTH);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    private void startReadig() {
        Runnable r1 = () -> {
            System.out.println("reading  started  from client ");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        // System.out.println("Server terminated the Chat ");
                        JOptionPane.showMessageDialog(this, "Server terminated the Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server :" + msg);
                    messageArea.append("server: " + msg + "\n");
                }
            } catch (Exception e) {
                System.out.println("connection close");
            }

        };
        new Thread(r1).start();

    }

    private void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writting   started  from client.......");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(
                            new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        System.out.println("self terminated the chat  ");
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
        System.out.println("client");
        new Client();
    }

}
