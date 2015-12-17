package com.nerdery.smartlocker.test.gui;

import com.nerdery.smartlocker.test.TestClient;
import com.nerdery.smartlocker.util.Command;
import com.nerdery.smartlocker.util.Network;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class TestFrame extends JFrame implements Observer {

    /** Minimum size dimensions */
    public static final int FRAME_WIDTH = 600;
    public static final int FRAME_HEIGHT = 300;

    /** The client we use to communicate with the server */
    private TestClient client;

    public TestFrame() {
        initComponents();
        try {

            client = new TestClient();
            client.addObserver(this);
            output.append("Client Channel Established \n");
        } catch (IOException e) {
            e.printStackTrace();
            output.append("Oh shit client channel broke \n");
        }
    }

    private void initComponents() {

        // Setup frame
        setTitle("SmartLocker Test Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        JPanel argumentFieldPanel = new JPanel();
        argumentFieldPanel.setLayout(new BoxLayout(argumentFieldPanel, BoxLayout.Y_AXIS));

        JPanel commandListPanel = new JPanel();
        commandListPanel.setLayout(new BoxLayout(commandListPanel, BoxLayout.Y_AXIS));

        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.X_AXIS));
        fieldPanel.setMaximumSize(new Dimension(FRAME_WIDTH - 200, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JPanel textPanel = new JPanel();
        textPanel.setSize(new Dimension(FRAME_WIDTH - 10, 200));

        // Initialize components
        commandList = new JComboBox(Command.values());

        argumentField = new JTextField();

        JLabel argumentLabel = new JLabel("Arguments:");
        JLabel commandLabel = new JLabel("Commands:");

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
                sendButtonPushed();
        });

        output = new JTextArea(12, 40);
        output.setWrapStyleWord(true);
        output.setLineWrap(true);

        JScrollPane sp = new JScrollPane();
        sp.setViewportView(output);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        argumentFieldPanel.add(argumentLabel);
        argumentFieldPanel.add(argumentField);

        commandListPanel.add(commandLabel);
        commandListPanel.add(commandList);

        fieldPanel.add(commandListPanel);
        fieldPanel.add(argumentFieldPanel);

        buttonPanel.add(sendButton);

        textPanel.add(sp);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(fieldPanel);
        getContentPane().add(buttonPanel);
        getContentPane().add(textPanel);

        pack();

        setLocationByPlatform(true);
    }

    @Override
    public void update(Observable observable, Object obj) {
        ByteBuffer messageBuffer = (ByteBuffer) obj;

        output.append("Message received: " + Arrays.toString(messageBuffer.array()) + "\n");
    }

    private void sendButtonPushed() {
        Command c = (Command) commandList.getSelectedItem();
        String arguments = argumentField.getText();
        int[] args = new int[0];
        if(arguments.length() > 0) {
            String[] argArray = arguments.split(" ");
            args = new int[argArray.length];

            for(int i = 0; i < argArray.length; i++) {
                try {
                    args[i] = Integer.parseInt(argArray[i]);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Nice try");
                }
            }
        }

        try {
            client.send(Network.packageCommand(c, args));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Sending of command failed.");
        }
    }

    //////////////////////
    // START COMPONENTS //
    //////////////////////

    private JTextArea output;

    private JTextField argumentField;

    private JComboBox commandList;

    //////////////////////
    //  END COMPONENTS  //
    //////////////////////
}
