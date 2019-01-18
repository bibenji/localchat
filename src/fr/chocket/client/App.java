package fr.chocket.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import fr.chocket.common.ChatMessage;
import fr.chocket.common.SocketListener;
import fr.chocket.server.Server;
import fr.chocket.tool.AdressageTool;
import fr.chocket.tool.ChatFile;
import fr.chocket.tool.FileSender;
import fr.chocket.view.ChatZone;
import fr.chocket.view.JoinServerDialog;
import fr.chocket.view.NewServerDialog;
import fr.chocket.view.Welcome;

@SuppressWarnings("serial")
public class App extends JFrame implements SocketListener {
	private Welcome welcomeView;
	private JButton createServer = new JButton("Créer un serveur sur cette machine");
	private JButton joinServer = new JButton("Rejoindre un serveur existant");
	private JFileChooser fileChooser = new JFileChooser();
	private File fileChoosen;
	
	private ChatZone cz = null;
	
	private String pseudo = "";
	
	private int serverPort;
	
	private String targetServerIp = "";
	private int targetServerPort;
	
	private boolean serverMode = true;
		
	private UserSocket socket = null;	

	private Map<String, ChatFile> files = new HashMap<String, ChatFile>();
	
	public App() {
		this.setName("Chat");
		this.setSize(500, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setLayout(new BorderLayout());
		
		handleWelcome();		
				
		this.setVisible(true);		
	}
	
	public void handleWelcome() {
		NewServerDialog nsd = new NewServerDialog(this, "Créer un nouveau serveur", true);
		createServer.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent event) {				
				String[] result = nsd.showDialog();
				if (result[0] != null && !result[0].equals("")) {
					pseudo = result[0];					
					if (!result[1].equals("")) {
						int tempServerPort = Integer.parseInt(result[1].toString());	
						serverPort = (tempServerPort != 0 ? tempServerPort : 7654);
					} else {
						serverPort = 7654;
					}
					serverMode = true;
					handleChat();
				}
			}
		});
		
		JoinServerDialog jsd = new JoinServerDialog(this, "Rejoindre un serveur existant", true);
		joinServer.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent event) {					
				String[] result = jsd.showDialog();
				if (result != null && !result.equals("")) {
					pseudo = result[0].toString();
					targetServerIp = result[1].toString();
					targetServerPort = Integer.parseInt(result[2].toString());
					serverMode = false;
					handleChat();
				}
			}
		});
		
		welcomeView = new Welcome(createServer, joinServer);
		
		this.setContentPane(welcomeView);
	}
	
	public void handleChat() {
		
		if (serverMode) {
			cz = new ChatZone(AdressageTool.getLocalIp(), serverPort);	
		} else {
			cz = new ChatZone();
		}		
		cz.setBackground(Color.YELLOW);
		this.setContentPane(cz);
						
//		this.getContentPane().remove(welcomeView);
		
		cz.addActionListenerToSend(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (cz.getNewMessage() != null && !cz.getNewMessage().equals("")) {
					String msg = pseudo + ": " + cz.getNewMessage();
					
					if (socket != null) {
						socket.sendMessage(msg);
					}
					
				}
			}
		});		

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		cz.addActionListenerToSendFile(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int returnVal = fileChooser.showOpenDialog(cz);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileChoosen = fileChooser.getSelectedFile();
					FileSender sender = new FileSender(fileChoosen, socket.getSocket());
					sender.sendFile();
				}				
			};
		});
				
//		cz.setContacts("Bernard;Alain;Gérard");
//		cz.addMessage("Test: test de message 2");
		validateAndRepaint();
		
		initSocket();
		
	}
	
	public void validateAndRepaint() {
		this.validate();
		this.repaint();
	}

	public void initSocket() {
		if (serverMode) {			
			Server s = new Server(serverPort, 20);
			
			try {
				socket = new UserSocket(InetAddress.getLocalHost(), serverPort);
				socket.setPseudo(pseudo);
				socket.addSocketListener(this);
				Thread socketThread = new Thread(socket);
				socketThread.start();			
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
						
			Thread serveurThread = new Thread(s);
			serveurThread.start();
		} else {
			InetAddress targetIp;
			
			try {
				targetIp = InetAddress.getByName(targetServerIp);
				socket = new UserSocket(targetIp, targetServerPort);
				socket.setPseudo(pseudo);
				socket.addSocketListener(this);
				Thread socketThread = new Thread(socket);
				socketThread.start();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
		}
	}
		
	public void receiveFromSocket(ChatMessage message) {
		
//		System.out.println(this.getClass() + ": message: " + message.toString());
		
//		SwingUtilities.invokeLater(new Runnable() {			
//			public void run() {
			
			if (message.getCanal() != null) {
				if (message.getCanal().equals("discussion")) {
					cz.addMessage(message.getContent());
					validateAndRepaint();		
				} else if (message.getCanal().equals("contacts") && message.getKey().equals(socket.getKey())) {				
					cz.handleContacts(message.getContent());
					validateAndRepaint();
				}
				else {
					// on considère que c'est un fichier
					ChatFile file = files.get(message.getCanal());
					
					if (file != null) {
						System.out.println(this.getClass() + ": FileChat found");
						if (!message.getContent().equals("")) {
							System.out.println(this.getClass() + ": adding bytes to FileChat");
							file.addBytes(Base64.getDecoder().decode(message.getContent()));
						} else {
							file.saveFile();
							System.out.println(this.getClass() + ": FileChat saved!");						
						}						
					} else {
						System.out.println(this.getClass() + ": creating a new ChatFile: " + message.getCanal());
						file = new ChatFile(message.getCanal(), System.getProperty("user.dir")+"\\"+message.getCanal());
//						file = new ChatFile(message.getCanal(), "F:\\www_java\\LearnSockets\\"+message.getCanal());
						
						files.put(message.getCanal(), file);
					}
				}	
			}			
					
//			}
//		});
			
	}
}
