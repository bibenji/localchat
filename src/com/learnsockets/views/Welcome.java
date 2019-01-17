package com.learnsockets.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Welcome extends JPanel {
	private JLabel welcomeWord = new JLabel("Bienvenue sur MSN 2 !");		
	private JLabel subWelcomeWord = new JLabel("Le chat qui déchire sa race et la chatte à sa grand-mère...");	
	
	public Welcome(JButton createServer, JButton joinServer) {
//		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(welcomeWord);
		this.add(subWelcomeWord);
		this.add(createServer);
		this.add(joinServer);
		
		this.setBackground(Color.CYAN);
				
		welcomeWord.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		welcomeWord.setHorizontalAlignment(JLabel.CENTER);
		subWelcomeWord.setHorizontalAlignment(JLabel.CENTER);
		
		welcomeWord.setPreferredSize(new Dimension(500, 100));
		subWelcomeWord.setPreferredSize(new Dimension(500, 50));
		
		createServer.setPreferredSize(new Dimension(400, 60));
		createServer.setMargin(new Insets(20, 50, 20, 50));
		joinServer.setPreferredSize(new Dimension(400, 60));
		joinServer.setMargin(new Insets(20, 50, 20, 50));
		
						

	}	
}
