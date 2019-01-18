package com.learnsockets.common;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestFileChooser extends JFrame {
	private JPanel panel = new JPanel();
	private JFileChooser jfc = new JFileChooser();
	private JButton b1 = new JButton("b1");
	private JButton button = new JButton("click me");
	
	
	public TestFileChooser() {
		this.setName("Chat");
		this.setSize(500, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setLayout(new BorderLayout());				
	
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int returnVal = jfc.showOpenDialog(panel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println(returnVal);
					System.out.println(jfc.getSelectedFile());	
				}				
			}
		});
		
		panel.add(b1);
		panel.add(button);
						
		this.setContentPane(panel);
		this.setVisible(true);		
	}

}
