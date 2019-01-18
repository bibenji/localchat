package com.learnsockets.server;

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

import com.learnsockets.common.ChatContact;
import com.learnsockets.common.ChatMessage;
import com.learnsockets.common.SocketListener;
import com.learnsockets.tools.ChatFile;

public class Serveur implements Runnable, SocketListener {
	int portNumber = 7654;
	int maxConnexion = 4;
	
	private ServerSocket ss = null;
	private List<ChatClient> clients = new ArrayList<ChatClient>();
	
	private Map<String, ChatFile> files = new HashMap<String, ChatFile>();
	
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
//		finally {
//			if (in != null) in.close();
//			if (out != null) out.close();
//			if (socket != null) socket.close();
//		}
	}
	
	public void receiveFromSocket(ChatMessage message) {
		System.out.println("receiveFromSocket in Serveur");
		
//		ChatMessage message = new ChatMessage(str, "", "discussion", "");
		
		if (message.getCanal().equals("dicussion")) {
			for (ChatClient client : clients) {
				try {				
					PrintWriter out = new PrintWriter(client.getSocket().getOutputStream());
					out.println(message.toString());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}			
		} else {			
				System.out.println(message.toString());
				
				ChatFile file = files.get(message.getCanal());
				
				if (file != null) {
					System.out.println("retrouve un filechat existant");
					if (!message.getContent().equals("")) {
						System.out.println("Ajout de byte dans un chatfile existant");
						file.addBytes(Base64.getDecoder().decode(message.getContent()));
					} else {
						file.saveFile();
						System.out.println("Traitement terminé !");						
					}						
				} else {
					System.out.println("Création d'un nouveau ChatFile");
					file = new ChatFile(message.getCanal(), "F:\\www_java\\LearnSockets\\"+message.getCanal());
					files.put(message.getCanal(), file);
				}
			
			
			
			// un array indexé par nom de fichier + uuid
			// dans cet array entité file
			// et à chaque réception on empile
			// quand finit on écrit sur le disque
			
			// ensuite foutre le module dans les clients
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
