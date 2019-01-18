package fr.chocket.common;

public class ChatContact {
	public String id;
	public String pseudo;
	
	public ChatContact(String contactAsString) {		
		String[] split = contactAsString.split(":");
		this.id = split[0];
		this.pseudo = split[1];
	}
	
	public ChatContact(String id, String pseudo) {
		this.id = id;
		this.pseudo = pseudo;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPseudo() {
		return pseudo;
	}
	
	public String toString() {
		return id+":"+pseudo;
	}
}
