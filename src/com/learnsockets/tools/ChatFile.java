package com.learnsockets.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatFile {
	private String name;
	private String destination;
	private List<byte[]> listBytes = new ArrayList<byte[]>();
	
	public ChatFile(String name, String destination) {
		this.name = name;
		this.destination = destination;
	}
	
	public void addBytes(byte[] b) {
		listBytes.add(b);
	}
	
	public void saveFile() {		
		try {
						
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destination)));
			
			for (byte[] buf : listBytes) {
				System.out.println("Tableau de bytes !!!");
				for (byte subBuf : buf)
					bos.write(subBuf);
			}
				
			
			bos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
