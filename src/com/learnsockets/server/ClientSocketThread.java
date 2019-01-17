package com.learnsockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.learnsockets.common.Reception;
import com.learnsockets.common.SocketListener;
import com.learnsockets.common.SocketNotifier;

public class ClientSocketThread implements Runnable, SocketNotifier {
	private String name = "";
	
	private Socket socket = null;
	
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	private SocketListener sl = null;
	
	public void setSocketListener(SocketListener sl) {
		this.sl = sl;
	}
	
	public ClientSocketThread(String name, Socket socket) {
		this.name = name;
		this.socket = socket;		
	}
	
	public void run() {
		System.out.println("Server: Client connected!");
		
		try {
			System.out.println("You are now connected!");
			
//			out = new PrintWriter(socket.getOutputStream());			
//			out.println("You are now connected!");
//			out.flush();
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Reception r = new Reception(in);
			r.setSocket(socket);
			if (sl != null) {
				r.setSocketListener(sl);
			}			
			Thread receptionThread = new Thread(r);
			receptionThread.start();
			
		} catch (IOException e) {//			
			e.printStackTrace();
		}
		
//		client.close();
	}
}
