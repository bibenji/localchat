package fr.chocket.tool;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AdressageTool {

	public static String getLocalIp() {
		try {
			String localAddress = InetAddress.getLocalHost().toString();
			String[] split = localAddress.split("/");
			return split[1];
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "error";
	}
	
}
