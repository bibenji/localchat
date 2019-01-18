package fr.chocket.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class NewServerDialog extends JDialog {
	
	JLabel labelPseudo = new JLabel("Pseudo : ");
	JTextField jtfPseudo = new JTextField();
	JLabel labelPort = new JLabel("Port (facultatif) : ");
	JTextField jtfPort = new JTextField();
	JButton ok = new JButton("Ok");
	String pseudo = "";
	String port = "";
	
	public NewServerDialog(JFrame parent, String title, boolean modal) {
		super(parent, title, modal);
		this.setSize(300, 300);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		labelPseudo.setPreferredSize(new Dimension(100, 30));
		jtfPseudo.setPreferredSize(new Dimension(150, 30));
		labelPort.setPreferredSize(new Dimension(100, 30));
		jtfPort.setPreferredSize(new Dimension(150, 30));
		
		this.setLayout(new FlowLayout());
		this.getContentPane().add(labelPseudo);
		this.getContentPane().add(jtfPseudo);
		this.getContentPane().add(labelPort);
		this.getContentPane().add(jtfPort);
		this.getContentPane().add(ok);
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pseudo = jtfPseudo.getText();
				port = jtfPort.getText();
				setVisible(false);
			}
		});
	}
	
	public String[] showDialog() {
		setVisible(true);
		String[] data = {jtfPseudo.getText(), jtfPort.getText()};
		return data;
	}
}
