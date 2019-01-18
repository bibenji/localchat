package com.learnsockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.learnsockets.common.ChatMessage;
import com.learnsockets.common.Reception;
import com.learnsockets.common.SocketListener;
import com.learnsockets.tools.Observable;
import com.learnsockets.tools.Observer;

public class MySocket implements Observable, Runnable, SocketListener {		
	
	private InetAddress ip;
	private int port;
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	private Socket socket = null;
	
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	private String pseudo = "";
	private String key;
	
	public MySocket() {				
//		Socket socket = new Socket(adresse_distante, port_distant, adresse_locale, port_locale);
	}
	
	public MySocket(InetAddress ip, int port) {
		System.out.println("Client: Création d'un client socket");
		
		this.ip = ip;
		this.port = port;
	}
	
	public void setPseudo(String p) {
		pseudo = p;
	}
	
	public void run() {
		try {			
//			socket = new Socket(InetAddress.getLocalHost(), 7654);			
			socket = new Socket(ip, port);					
			
			out = new PrintWriter(socket.getOutputStream());			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
			// envoi pseudo, réception key
			out.println(pseudo);
			out.flush();				
			String msg = in.readLine();			
			key = msg;
			out.println("ok");
			out.flush();
					
			Reception r = new Reception(in);
			r.setSocketListener(this);
			Thread receptionThread = new Thread(r);
			receptionThread.start();			
			
//			String distantMessage = in.readLine();
//			System.out.println("Client: " + distantMessage);
//			notifyObservers("Client: " + distantMessage);
//			socket.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	public void receiveFromSocket(ChatMessage message) {				
		notifyObservers(message);		
	}
	
	public void sendMessage(String str) {		
		if (out != null) {
			ChatMessage message = new ChatMessage();
			message.setCanal("discussion");
			message.setContent(str);
			out.println(message.toString());
			out.flush();			
		}
	}
	
	public void addObserver(Observer obs) {
		observers.add(obs);
	}
	
	public void clearObservers() {
		observers = new ArrayList<Observer>();
	}
	
	public void notifyObservers(ChatMessage message) {
		for (Observer obs : observers) {
			obs.update(message);
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getKey() {
		return key;
	}
}
