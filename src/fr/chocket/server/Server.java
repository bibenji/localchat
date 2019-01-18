package fr.chocket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.chocket.common.ChatContact;
import fr.chocket.common.ChatMessage;
import fr.chocket.common.SocketListener;
import fr.chocket.tool.ChatFile;

public class Server implements Runnable, SocketListener {
	int portNumber = 7654;
	int maxConnexion = 4;
	
	private ServerSocket ss = null;
	private List<ClientSocket> clients = new ArrayList<ClientSocket>();	
	
	private Map<String, ChatFile> files = new HashMap<String, ChatFile>();
	
	public Server() {
		System.out.println(this.getClass() +": server creation");
		try {
			ss = new ServerSocket(7654);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Server(int portNumber, int maxConnexion) {
		try {
			ss = new ServerSocket(portNumber, maxConnexion);
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	public void run() {
		System.out.println(this.getClass() + ": server is running");
		
		try {
			while (true) {
				Socket socket = ss.accept();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				
				// récupération du pseudo et envoi de la clé client
				String pseudo = in.readLine();
				if (pseudo != null) {
					ClientSocket cc = new ClientSocket(socket, pseudo);
					out.println(cc.getId());
					out.flush();
					
					String str = in.readLine();					
					if (str.equals("ok")) {
						clients.add(cc);
						ClientSocketThread cst = new ClientSocketThread(cc.getId(), socket);
						cst.addSocketListener(this);
						Thread t = new Thread(cst);
						t.start();
						writeContacts();
					}					
				}
			}			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void receiveFromSocket(ChatMessage message) {
//		System.out.println(this.getClass() + ": receiveFromSocket(ChatMessage message): " + message.toString());
				
//		if (message.getCanal().equals("discussion")) {
			for (ClientSocket client : clients) {
				try {				
					PrintWriter out = new PrintWriter(client.getSocket().getOutputStream());
					out.println(message.toString());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}			
//		}
	}
	
	public void writeContacts() {
		String contacts = "";
		for (ClientSocket client : clients) {
			ChatContact contact = new ChatContact(client.getId(), client.getPseudo());
			contacts += contact.toString()+">";			
		}
		contacts = contacts.substring(0, contacts.length()-1);
				
		ChatMessage message = new ChatMessage(contacts, "", "contacts", "");
		
		for (ClientSocket client : clients) {
			message.setKey(client.getId());
			try {
				PrintWriter out = new PrintWriter(client.getSocket().getOutputStream());
				out.println(message.toString());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
