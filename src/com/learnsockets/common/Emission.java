package com.learnsockets.common;

import java.io.PrintWriter;
import java.util.Scanner;

public class Emission implements Runnable {
	private PrintWriter out;
	private String msg = null;
	private Scanner sc = null;

	public Emission(PrintWriter out) {
		this.out = out;
	}

	public void run() {
		sc = new Scanner(System.in);	
		
		while (true) {
			msg = sc.nextLine();
			System.out.println("msg in Emission : " + msg);
			out.println(msg);
			out.flush();
		}
	}
}