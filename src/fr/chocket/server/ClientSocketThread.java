package fr.chocket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import fr.chocket.common.ChatMessage;
import fr.chocket.common.Reception;
import fr.chocket.common.SocketListener;
import fr.chocket.common.SocketNotifier;

public class ClientSocketThread implements Runnable, SocketNotifier {
	private String name = "";
	
	private Socket socket = null;
	
	private BufferedReader in = null;	
		
	private List<SocketListener> listeners = new ArrayList<SocketListener>();	
	
	public ClientSocketThread(String name, Socket socket) {
		this.name = name;
		this.socket = socket;		
	}
	
	public void run() {
		System.out.println(this.getClass() + ": ClientSocketThread is running");
		
		try {
			System.out.println(this.getClass() + ": connected to server as client");
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Reception r = new Reception(in);			
			
			// SocketListeners transfert
			if (!listeners.isEmpty()) {
				for (SocketListener listener : listeners)
					r.addSocketListener(listener);				
			}
			
			Thread receptionThread = new Thread(r);
			receptionThread.start();
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
//		client.close();
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
}
