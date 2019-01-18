package com.learnsockets.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

import com.learnsockets.common.ChatMessage;

public class FileSender {
	private File file;
	private Socket socket;
	private byte[] bytes;
//	private OutputStream os;
	
	public FileSender(File file, Socket socket) {
		this.file = file;
		this.socket = socket;
	}
	
	public void sendFile() {
		
		try {			
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
						
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			
			ChatMessage message = new ChatMessage();
			message.setCanal(file.getName());
			message.setContent(file.getName());
			
			out.println(message.toString());
			out.flush();
			
			int i = 0;
			while (i <= (int)file.length()*8) { // donne les octets
				// on lit 1 Mo
				if (i + 8000000 <= (int)file.length()*8) {
					bytes = new byte[8000000];
					bis.read(bytes, 0, 8000000);	
				} else {
					bytes = new byte[((int)file.length()*8-i)];
					bis.read(bytes, 0, ((int)file.length()*8)-i);
				}				
				
				// création du message
				message.setContent(Base64.getEncoder().encodeToString(bytes));
				out.println(message);
				out.flush();
				
				i += 8000000; // 1 Mo
			}
			
			message.setContent("");
			out.println(message);
			out.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}	
}
