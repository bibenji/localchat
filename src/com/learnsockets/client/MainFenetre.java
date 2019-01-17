package com.learnsockets.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.learnsockets.common.ChatMessage;
import com.learnsockets.server.Serveur;
import com.learnsockets.tools.AdressageTool;
import com.learnsockets.tools.Observer;
import com.learnsockets.views.ChatZone;
import com.learnsockets.views.JoinServerDialog;
import com.learnsockets.views.NewServerDialog;
import com.learnsockets.views.Welcome;

public class MainFenetre extends JFrame implements Observer {
	private Welcome welcomeView;
	private JButton createServer = new JButton("Créer un serveur sur cette machine");
	private JButton joinServer = new JButton("Rejoindre un serveur existant");
	
	private String ChatNewMessage = "";
	private ChatZone cz = null;
	
	private String pseudo = "";
	private int serverPort;
	private String targetServerIp = "";
	private int targetServerPort;
	private boolean serverMode = true;
		
	private MySocket socket = null;
	
	public MainFenetre() {
		this.setName("Chat");
		this.setSize(500, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setLayout(new BorderLayout());
		
		handleWelcome();		
				
		this.setVisible(true);		
	}
	
	public void handleWelcome() {
		NewServerDialog nsd = new NewServerDialog(this, "New Server Dialog", true);
		createServer.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent event) {				
				String[] result = nsd.showDialog();
				if (result[0] != null && !result[0].equals("")) {
					pseudo = result[0];					
					if (!result[1].equals("")) {
						int tempServerPort = Integer.parseInt(result[1].toString());	
						serverPort = (tempServerPort != 0 ? tempServerPort : 7654);
					} else {
						serverPort = 7654;
					}
					serverMode = true;
					handleChat();
				}
			}
		});
		
		JoinServerDialog jsd = new JoinServerDialog(this, "Join Server Dialog", true);
		joinServer.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent event) {					
				String[] result = jsd.showDialog();
				if (result != null && !result.equals("")) {
					pseudo = result[0].toString();
					targetServerIp = result[1].toString();
					targetServerPort = Integer.parseInt(result[2].toString());
					serverMode = false;
					handleChat();
				}
			}
		});
		
		welcomeView = new Welcome(createServer, joinServer);
		
		this.setContentPane(welcomeView);
	}
	
	public void handleChat() {
		
		if (serverMode) {
			cz = new ChatZone(AdressageTool.getLocalIp(), serverPort);	
		} else {
			cz = new ChatZone();
		}		
		cz.setBackground(Color.YELLOW);
		this.setContentPane(cz);
						
//		this.getContentPane().remove(welcomeView);
		
		cz.addActionListenerToSend(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (cz.getNewMessage() != null && !cz.getNewMessage().equals("")) {
					String msg = pseudo + ": " + cz.getNewMessage();
					
//					SwingUtilities.invokeLater(new Runnable() {
//						public void run() {							
//							cz.addMessage(msg);
//							validateAndRepaint();	
//						}
//					});
					
					if (socket != null) {
						socket.sendMessage(msg);
					}					
				}
			}
		});
				
		cz.setContacts("Bernard;Alain;Gérard");
		cz.addMessage("Test: test de message 2");
		validateAndRepaint();
		
		initSocket();
		
	}
	
	public void validateAndRepaint() {
		this.validate();
		this.repaint();
	}

	public void initSocket() {
		if (serverMode) {			
			Serveur s = new Serveur(serverPort, 20);
			
			try {
				socket = new MySocket(InetAddress.getLocalHost(), serverPort);
				socket.setPseudo(pseudo);
				socket.addObserver(this);
				Thread socketThread = new Thread(socket);
				socketThread.start();			
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
						
			Thread serveurThread = new Thread(s);
			serveurThread.start();
		} else {
			InetAddress targetIp;
			
			try {
				targetIp = InetAddress.getByName(targetServerIp);
				socket = new MySocket(targetIp, targetServerPort);
				socket.setPseudo(pseudo);
				socket.addObserver(this);
				Thread socketThread = new Thread(socket);
				socketThread.start();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
		}
	}
		
	public void update(String str) {
		
//		SwingUtilities.invokeLater(new Runnable() {			
//			public void run() {		
			
			ChatMessage cm = new ChatMessage();
			cm.recoverFromString(str);
			
			System.out.println("---");
			System.out.println("getContent : " + cm.getContent());
			System.out.println("getCanal : " + cm.getCanal());
			System.out.println("getKey : " + cm.getKey());
			System.out.println("socket key : " + socket.getKey());
			System.out.println(cm.getCanal().toString() == "contacts");
			System.out.println(cm.getCanal().equals("contacts"));
			System.out.println("---");
			
			
			if (cm.getCanal() != null && cm.getCanal().equals("discussion")) {
				cz.addMessage(cm.getContent());
				validateAndRepaint();		
			} else if (cm.getCanal() != null && cm.getCanal().equals("contacts") && cm.getKey().equals(socket.getKey())) {				
				cz.handleContacts(cm.getContent());
				validateAndRepaint();
			}
					
//			}
//		});
			
	}
}

