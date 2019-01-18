package fr.chocket.server;

import java.net.Socket;
import java.util.UUID;

public class ClientSocket {
	private Socket socket;
	private String uuid;
	private String pseudo;
	
	public ClientSocket(Socket socket, String pseudo) {
		this.socket = socket;
		this.pseudo = pseudo;
		this.uuid = UUID.randomUUID().toString();
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getPseudo() {
		return pseudo;
	}
	
	public String getId() {
		return uuid;
	}
}
