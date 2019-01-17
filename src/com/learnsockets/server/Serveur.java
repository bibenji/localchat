package com.learnsockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.learnsockets.common.ChatContact;
import com.learnsockets.common.ChatMessage;
import com.learnsockets.common.SocketListener;

public class Serveur implements Runnable, SocketListener {
	int portNumber = 7654;
	int maxConnexion = 4;
	
	private ServerSocket ss = null;
	private List<ChatClient> clients = new ArrayList<ChatClient>();
	
	public Serveur() {
		System.out.println("Server: Création du serveur");
		try {
			ss = new ServerSocket(7654);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Serveur(int portNumber, int maxConnexion) {
		try {
//			ServerSocket socketServeur = new ServerSocket();
//			ss = new ServerSocket(portNumber);
			ss = new ServerSocket(portNumber, maxConnexion);
//			ss = new ServerSocket(portNumber, maxConnexion, localAddress);
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	public void run() {
		System.out.println("Server: Serveur actif [run]");
		try {
			while (true) {
				Socket socket = ss.accept();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				
				String pseudo = in.readLine();
				if (pseudo != null) {
					ChatClient cc = new ChatClient(socket, pseudo);
					out.println(cc.getId());
					out.flush();
					
					String str = in.readLine();					
					if (str.equals("ok")) {
						clients.add(cc);
						ClientSocketThread cst = new ClientSocketThread(cc.getId(), socket);
						cst.setSocketListener(this);
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
	
	public void receiveFromSocket(String str) {
		System.out.println("receiveFromSocket in Serveur");
		
		ChatMessage message = new ChatMessage(str, "", "discussion", "");
		
		for (ChatClient client : clients) {
			try {				
				PrintWriter out = new PrintWriter(client.getSocket().getOutputStream());
				out.println(message.toString());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void writeContacts() {
		String contacts = "";
		for (ChatClient client : clients) {
			ChatContact contact = new ChatContact(client.getId(), client.getPseudo());
			contacts += contact.toString()+">";			
		}
		contacts = contacts.substring(0, contacts.length()-1);
				
		ChatMessage message = new ChatMessage(contacts, "", "contacts", "");
		
		for (ChatClient client : clients) {
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
