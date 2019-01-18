package fr.chocket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import fr.chocket.common.ChatMessage;
import fr.chocket.common.Reception;
import fr.chocket.common.SocketListener;
import fr.chocket.common.SocketNotifier;

public class UserSocket implements Runnable, SocketListener, SocketNotifier {		
	
	private InetAddress ip;
	private int port;
	
	private List<SocketListener> listeners = new ArrayList<SocketListener>();
	
	private Socket socket = null;
	
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	private String pseudo = "";
	private String key;
		
	public UserSocket(InetAddress ip, int port) {
		System.out.println(this.getClass() + ": client's socket creation");
		
		this.ip = ip;
		this.port = port;
	}
	
	public void setPseudo(String p) {
		pseudo = p;
	}
	
	public void run() {
		try {
			socket = new Socket(ip, port);					
			
			out = new PrintWriter(socket.getOutputStream());			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
			// envoi pseudo, réception key client
			out.println(pseudo);
			out.flush();				
			String msg = in.readLine();			
			key = msg;
			out.println("ok");
			out.flush();
					
			Reception r = new Reception(in);
			r.addSocketListener(this);
			Thread receptionThread = new Thread(r);
			receptionThread.start();			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
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
	
	public void receiveFromSocket(ChatMessage message) {				
		notifyListeners(message);		
	}
	
	public void addSocketListener(SocketListener listener) {
		listeners.add(listener);
	}
	
	public void notifyListeners(ChatMessage message) {
		for (SocketListener listener : listeners)
			listener.receiveFromSocket(message);
	}
	
	public void clearListeners() {
		listeners = new ArrayList<SocketListener>();
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getKey() {
		return key;
	}
}
