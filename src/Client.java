import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declaring components
    private JLabel heading = new JLabel("CLIENT AREA");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("localhost", 7777);
            System.out.println("Connection gained");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        this.setTitle("Client Side Messenger");
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // component code
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        // setting layout of frame
        this.setLayout(new BorderLayout());

        // adding components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }
        });
    }

    // To read messages
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started....");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        // System.out.println("Server terminated the chat");
                        dispose();
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        // socket.close();
                        break;
                    }
                    // System.out.println("Server: " + msg);
                    messageArea.append("Server: " + msg + "\n");
                }
            } catch (Exception e) {
                // System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();
    }

    // To write messages
    // public void startWriting() {
    // Runnable r2 = () -> {
    // System.out.println("Writer started...");
    // try {
    // while (!socket.isClosed()) {
    // BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
    // String content = br1.readLine();
    // out.println(content);
    // out.flush();
    // if (content.equals("exit")) {
    // socket.close();
    // break;
    // }
    // }
    // System.out.println("Connection is closed");
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // };
    // new Thread(r2).start();
    // }

    public static void main(String[] args) {
        System.out.println("CLIENT AREA ");
        new Client();
    }
}