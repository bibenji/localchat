package com.learnsockets.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Adressage {
	InetAddress LocaleAdresse;
	InetAddress ServeurAdresse;
	
	public Adressage() {
		try {
			LocaleAdresse = InetAddress.getLocalHost();
			System.out.println("L'adresse locale est : " + LocaleAdresse);
			
			
			ServeurAdresse = InetAddress.getByName("www.openclassrooms.com");
			System.out.println("L'adresse du serveur de oc est : " + ServeurAdresse);		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			  InetAddress localhost = InetAddress.getLocalHost();
			  System.out.println(" IP Addr: " + localhost.getHostAddress()); // my ip address
			  // Just in case this host has multiple IP addresses....
			  InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
			  if (allMyIps != null && allMyIps.length > 1) {
				  System.out.println(" Full list of IP addresses:");
				  for (int i = 0; i < allMyIps.length; i++) {
					  System.out.println("    " + allMyIps[i]);
				  }
			  }
		} catch (UnknownHostException e) {
			System.out.println(" (error retrieving server host name)");
		}

		try {
			System.out.println("Full list of Network Interfaces:");
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
			    NetworkInterface intf = en.nextElement();
			    System.out.println("    " + intf.getName() + " " + intf.getDisplayName());
			    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
			    	System.out.println("        " + enumIpAddr.nextElement().toString());
			    }
			  }
			} catch (SocketException e) {
				System.out.println(" (error retrieving network interface list)");
			}
	}	
}