package fr.chocket.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reception implements Runnable, SocketNotifier {	
	private BufferedReader in = null;
	private String msg = null;
		
	private List<SocketListener> listeners = new ArrayList<SocketListener>();	
	
	public Reception(BufferedReader in) {
		this.in = in;
	}
	
	public void run() {
		while (true) {
			try {
				
				msg = in.readLine();
				if (msg != null) {
					
					ChatMessage message = new ChatMessage(msg);
					
					for (SocketListener listener : listeners)
						listener.receiveFromSocket(message);
										
					msg = null;
					
				}				
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {				
					e.printStackTrace();
				}				

			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
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
