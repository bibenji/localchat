package com.learnsockets.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.learnsockets.common.ChatMessage;
import com.learnsockets.tools.Observer;

public class Fenetre extends JFrame implements Observer {
	private JFrame mainFrame = new JFrame();
	private JPanel content = new JPanel();
	private JPanel contentMessages;
	private JScrollPane contentMessagesScroll = new JScrollPane();
	private List<String> messages = new ArrayList<String>();
	private JTextField message;
	
	private MySocket socket = null;
	
	public Fenetre() {
		this.setName("Chat");
		this.setSize(500, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		content.setLayout(new BorderLayout());
		
		messages.add("Test: message test 1");
		messages.add("Test: message test 2");
		handleContentMessages();
		
//		contentMessagesScroll.add(contentMessages);
//		content.add(contentMessagesScroll, BorderLayout.CENTER);
		
		JPanel bottomPane = new JPanel();
		message = new JTextField("New message");
		message.setPreferredSize(new Dimension(250, 50));
		
		message.setAlignmentX(JTextField.CENTER_ALIGNMENT); // marche pas !?
		
		bottomPane.add(message);
		JButton send = new JButton("Send");
		send.setPreferredSize(new Dimension(100, 50));
		send.addActionListener(new SendButtonListener());
		bottomPane.add(send);			
		
		content.add(bottomPane, BorderLayout.SOUTH);
		
		this.setContentPane(content);
		this.setVisible(true);
	}
	
	public void initSocket() {
		socket = new MySocket();
		socket.addObserver(this);
		Thread socketThread = new Thread(socket);
		socketThread.start();
	}
	
	private void sendMessageToSocket(String str) {
		if (socket != null) {
			socket.sendMessage(str);
		}
	}
	
	private void handleContentMessages() {	
		System.out.println("handleContentMessages in Fenetre");
		contentMessages = new JPanel();
		contentMessages.setLayout(new BoxLayout(contentMessages, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel("Messages");
		contentMessages.add(label);		
		for (String mess : messages) {
			JLabel labelMess = new JLabel(mess);
			contentMessages.add(labelMess);
		}
		content.add(contentMessages, BorderLayout.CENTER);
	}
		
	class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {			
			String msg = "You: " + message.getText();
			System.out.println(msg);			
			messages.add(msg);
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JLabel labelMess = new JLabel(msg);
					contentMessages.add(labelMess);
					content.validate();
					content.repaint();		
				}
			});
			
			if (socket != null) {
				socket.sendMessage(msg);
			}
		}
	}
	
	public void update(ChatMessage message) {
		String str = message.toString();
		System.out.println("update in Fenetre, str : " + str);
		
		
		messages.add(str);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JLabel labelMess = new JLabel(str);
				contentMessages.add(labelMess);
				content.validate();
				content.repaint();		
			}
		});						
	}
}
