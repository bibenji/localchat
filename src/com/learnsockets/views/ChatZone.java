package com.learnsockets.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.learnsockets.common.ChatContact;

public class ChatZone extends JPanel {
	private JPanel chat = new JPanel();
	private JPanel chatMessages = new JPanel();	
	private JScrollPane chatMessagesScroll = new JScrollPane();	
	private JTextField newMessage;
	private JPanel contacts = new JPanel();
//	private JSplitPane split;
	private JPanel split;
	private boolean hasSplit = false;
	private JButton send = new JButton("Send");
	private JButton sendFile = new JButton("File");
	
	private String serverIp;
	private int serverPort;
	
	private List<ChatContact> contactsList = new ArrayList<ChatContact>();
	
	public ChatZone() {		
		initComponent();			
	}
	
	public ChatZone(String serverIp, int serverPort) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		initComponent();
	}
	
	public void initComponent() {
		chat.setLayout(new BorderLayout());
		
		chatMessages.setLayout(new BoxLayout(chatMessages, BoxLayout.PAGE_AXIS));		
		addMessage("Test: test de message 1");		
		chat.add(chatMessages, BorderLayout.CENTER);
		
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BorderLayout());		
		newMessage = new JTextField("New message");
		newMessage.setPreferredSize(new Dimension(160, 40));		
//		newMessage.setAlignmentX(JTextField.CENTER_ALIGNMENT); // marche pas !?		
		bottomPane.add(newMessage, BorderLayout.CENTER);
		
		send.setPreferredSize(new Dimension(100, 40));		
		sendFile.setPreferredSize(new Dimension(100, 40));
		
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new BorderLayout());
		buttonsPane.add(send, BorderLayout.WEST);
		buttonsPane.add(sendFile, BorderLayout.EAST);
		bottomPane.add(buttonsPane, BorderLayout.EAST);
		
//		bottomPane.add(sendFile, BorderLayout.EAST);
		
		chat.add(bottomPane, BorderLayout.SOUTH);			
				
		setContacts("Benjamin;Toto;Vincent");
		
		initSplit();
		
		chat.setPreferredSize(new Dimension(360, 600));
		contacts.setPreferredSize(new Dimension(140, 600));	
	}
	
	private void initSplit() {	
		if (hasSplit)			
			this.remove(split);		
		
//		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chat, contacts);
//		split.setDividerLocation(350);
////		split.setBackground(Color.BLUE);
////		split.setPreferredSize(new Dimension(470, 550));
//		this.add(split, BorderLayout.CENTER);
		
		split = new JPanel();
		split.setLayout(new BorderLayout());
		
		if (serverIp != null) {
			JLabel infosServer = new JLabel("Infos server, ip: " + serverIp + ", port: " + serverPort);
			infosServer.setHorizontalAlignment(JLabel.CENTER);
			infosServer.setForeground(Color.RED);
			infosServer.setBackground(Color.GREEN);
			split.add(infosServer, BorderLayout.NORTH);	
		}		
		
		split.add(chat, BorderLayout.CENTER);
		split.add(contacts, BorderLayout.EAST);
		this.setLayout(new BorderLayout());
		this.add(split, BorderLayout.CENTER);
		
		hasSplit = true;
	}
	
	public void handleContacts(String contactsAsString) {
		contactsList = new ArrayList<ChatContact>();
		String[] contactsAsArray = contactsAsString.split(">");
		for (String contactAsString : contactsAsArray) {					
			ChatContact contact = new ChatContact(contactAsString);
			contactsList.add(contact);					
		}
		
		contacts = new JPanel();
		contacts.setBackground(Color.CYAN);
		BoxLayout bl = new BoxLayout(contacts, BoxLayout.PAGE_AXIS);		
		contacts.setLayout(bl);		
//		contacts.setPreferredSize(new Dimension(100, 550));		
		
		JPanel contactPanel = new JPanel();
		JLabel contactLabel = new JLabel("Contacts");
		contactLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		contactLabel.setHorizontalAlignment(JLabel.CENTER);
//		contactLabel.setPreferredSize(new Dimension(100, 30));		
		contactPanel.add(contactLabel);
		contacts.add(contactPanel);
		
		for (ChatContact contact : contactsList) {			
			JCheckBox contactCheck = new JCheckBox();
			contactCheck.setSelected(true);			
			contactCheck.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					
				}
			});
			contactLabel = new JLabel(contact.getPseudo());
			contactPanel = new JPanel();
			contactPanel.setLayout(new BorderLayout());
			contactPanel.add(contactCheck, BorderLayout.WEST);
			contactPanel.add(contactLabel, BorderLayout.CENTER);			
//			contactPanel.setPreferredSize(new Dimension(100, 20));
			contacts.add(contactPanel);
		}	
		
		initSplit();
	}
	
	public void setContacts(String str) {		
		contacts = new JPanel();		
		contacts.setLayout(new BoxLayout(contacts, BoxLayout.PAGE_AXIS));
//		contacts.setPreferredSize(new Dimension(100, 550));
		JLabel contactLabel = new JLabel("Contacts");
		contactLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		contactLabel.setHorizontalAlignment(JLabel.CENTER);
//		contactLabel.setPreferredSize(new Dimension(100, 30));
		
		contacts.add(contactLabel);
		
		String[] contactsArray = str.split(";");
		for (String contact : contactsArray) {
			contactLabel = new JLabel(contact);
//			contactLabel.setPreferredSize(new Dimension(100, 20));
			contacts.add(contactLabel);
		}	
		
		initSplit();
	}
		
	public void setMessages() {
		
	}
	
	public void addMessage(String msg) {
		JLabel newMessage = new JLabel(msg);
		chatMessages.add(newMessage);
//		chatMessages.validate();
//		chatMessages.repaint();		
	}
	
	public void addActionListenerToSend(ActionListener ac) {
		send.addActionListener(ac);
	}
	
	public void addActionListenerToSendFile(ActionListener ac) {
		sendFile.addActionListener(ac);
	}
	
	public String getNewMessage() {
		return newMessage.getText();
	}
}
