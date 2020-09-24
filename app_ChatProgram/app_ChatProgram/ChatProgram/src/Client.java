
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import javax.swing.text.DefaultCaret;

public class Client extends JFrame implements Runnable {

    private static JButton bt_send;
    private static JButton bt_connect;
    private static JButton bt_disconnect;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private static JLabel lb_username;
    private List list1;
    private static JTextArea tf_area;
    private static JTextArea tf_txt;
    private static JTextField tf_username;
    private Socket client;
    private DataOutputStream clientToServer;
    private BufferedReader serverToClient;
    private boolean isClosed = true;

    public Client() {
        super("Chat Application");
        setIconImage(new ImageIcon("client.jpg").getImage());
        list1 = new List();
        bt_send = new JButton("Send");
        bt_connect = new JButton("Connect");
        bt_disconnect = new JButton("Disconnect");
        tf_username = new JTextField();
        lb_username = new JLabel("Username");
        jScrollPane1 = new JScrollPane();
        tf_area = new JTextArea();
        jScrollPane2 = new JScrollPane();
        tf_txt = new JTextArea();

        tf_area.setColumns(20);
        tf_area.setRows(5);
        tf_area.setEditable(false);
        jScrollPane1.setViewportView(tf_area);

        tf_txt.setColumns(20);
        tf_txt.setRows(3);
        tf_txt.setEditable(false);
        jScrollPane2.setViewportView(tf_txt);

        bt_send.setEnabled(false);
        bt_disconnect.setEnabled(false);
        setBounds(700, 300, 400, 500);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap() //add space ฝั่งซ้าย
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE) //pref ขนาดความกว้าง
                                        .addComponent(jScrollPane2))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE) //add space ฝั่งซ้ายของกล่องขวา
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addComponent(bt_connect)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(bt_disconnect))
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addGap(40, 40, 40)
                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(lb_username, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                        .addComponent(tf_username, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(32, Short.MAX_VALUE)) //add space ฝั่งขวา
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(61, 61, 61)
                                                .addComponent(bt_send, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(lb_username, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tf_username, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(27, 27, 27)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(bt_connect)
                                                        .addComponent(bt_disconnect))
                                                .addGap(27, 27, 27)
                                                .addComponent(bt_send, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(16, 16, 16)))
                                .addContainerGap())
        );

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        bt_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bt_sendActionPerformed(evt);
            }
        });
        bt_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bt_connectActionPerformed(evt);
            }
        });
        bt_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bt_disconnectActionPerformed(evt);
            }
        });
        DefaultCaret caret = (DefaultCaret) tf_area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    }

    private void bt_sendActionPerformed(ActionEvent evt) {
        String msg = tf_txt.getText();
        if (msg.length() == 0) {
            tf_txt.setText("");
        } else {
            try {
                clientToServer.writeBytes(msg);
                clientToServer.writeBytes("\n");
                clientToServer.flush();
                msg = tf_username.getText() + " : " + msg;
                tf_txt.setText("");
                String alltxt = tf_area.getText();
                alltxt = alltxt + "\n" + msg;
                tf_area.setText(alltxt);
            } catch (Exception e) {
                try {
                    clientToServer.close();
                    serverToClient.close();
                    client.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void bt_connectActionPerformed(ActionEvent evt) {
        if (tf_username.getText().length() == 0) {
            tf_username.setText("Client");
        }
        isClosed = false;
        tf_txt.setEditable(true);
        bt_connect.setEnabled(false);
        bt_disconnect.setEnabled(true);
        tf_username.setEditable(false);
        bt_send.setEnabled(true);
    }

    private void bt_disconnectActionPerformed(ActionEvent evt) {
        try {
            isClosed = true;
            bt_connect.setEnabled(true);
            bt_disconnect.setEnabled(true);
            bt_send.setEnabled(false);
            tf_area.setText("");
            tf_txt.setText("");
            tf_username.setText("");
            tf_username.setEditable(true);
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            System.out.println("Waiting for user press 'Connect' button");
            sleep(100);
            if (!isClosed) {
                connect();
            }
        }
    }

    private void connect() {
        try {
            client = new Socket("localhost", 8080);
            clientToServer = new DataOutputStream(client.getOutputStream());
            serverToClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            clientToServer.writeBytes(tf_username.getText());
            clientToServer.writeBytes("\n");
            clientToServer.flush();
            readMessages();
        } catch (ConnectException e1) {
            tf_area.setText("Server is unreachable!");
        } catch (IOException e) {
            tf_area.setText("Server isn't open now");

        }
    }

    private void disconnect() throws IOException {
        clientToServer.close();
        serverToClient.close();
        client.close();
        tf_area.setText("");
    }

    private void readMessages() {
        try {
            while (!isClosed) {
                if (tf_area.getText().equals("Server is unreachable!")) {
                    tf_area.setText("");
                }
                String message = serverToClient.readLine(); //read a message from the connection
                if (message == null) {
                    tf_area.setText("Server is closed.");
                    bt_send.setEnabled(false);
                    disconnect();
                } else {
                    String allText = tf_area.getText(); //get all the text
                    allText = allText + "\n" + message; //append the new message
                    tf_area.setText(allText); //add all text
                }

            }
        } catch (Exception e) {
            System.out.println(e.toString() + "in readIncomingMessages");
            try {
                disconnect(); //if an error occurs, close the connection
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Client user = new Client();
        user.setVisible(true);
        Thread thread = new Thread(user);
        thread.start();
    }
}
