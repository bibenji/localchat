package com.learnsockets.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class Reception implements Runnable, SocketNotifier {
	private Socket socket = null;
	private BufferedReader in = null;
	private String msg = null;
	
	private SocketListener sl = null;
	
	public void setSocketListener(SocketListener sl) {
		this.sl = sl;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public Reception(BufferedReader in) {
		this.in = in;
	}
	
	public void run() {
		while (true) {
			try {
				
				msg = in.readLine();
				if (msg != null) {
					if (sl != null) {
						ChatMessage message = new ChatMessage(msg);						
						sl.receiveFromSocket(message);
					}
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
}