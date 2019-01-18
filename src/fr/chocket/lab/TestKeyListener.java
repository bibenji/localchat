package fr.chocket.lab;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestKeyListener extends JFrame {
	
	public TestKeyListener() {
		this.setName("Chat");
		this.setSize(500, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setLayout(new BorderLayout());
		
		JPanel pane = new JPanel();
		
		
		pane.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Coucou");
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Coucou");
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Coucou");
			}
			
		});
				
		this.setContentPane(pane);
		this.setVisible(true);		
	}

}
