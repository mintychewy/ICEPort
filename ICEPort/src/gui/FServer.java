package gui;

import java.net.*;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FServer {
	// GUI part JFileChooser
	// Server part
	protected File fileToSend;

	public FServer() {
		this(null);
	}

	public FServer(File fileName) {
		fileToSend = fileName;
	}

	protected void chooseFile() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			fileToSend = fileChooser.getSelectedFile();
			System.out.println(fileToSend.getName());
		}
	}

	protected void sendFile() {
		while(true) {
			BufferedOutputStream outToClient = null;
			Socket connectionSocket = null;
			ServerSocket welcomeSocket = null;
			try {
				welcomeSocket = new ServerSocket(12345);
				chooseFile();
				connectionSocket = welcomeSocket.accept();
				outToClient = new BufferedOutputStream(
						connectionSocket.getOutputStream());				
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(new JPanel(),
						"Cannot Transfer File");
				return;
			}
			if (outToClient != null) {
			
				byte[] mybytearray = new byte[(int) fileToSend.length()];

				FileInputStream fis = null;

				try {
					fis = new FileInputStream(fileToSend);
				} catch (FileNotFoundException ex) {
					JOptionPane.showMessageDialog(new JPanel(),
							"The selected file is not found.");
				}
				BufferedInputStream bis = new BufferedInputStream(fis);

				try {
					bis.read(mybytearray, 0, mybytearray.length);
					outToClient.write(mybytearray, 0, mybytearray.length);
					outToClient.flush();
					outToClient.close();
					connectionSocket.close();

					// File sent, exit the method
					welcomeSocket.close();
					return;
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(new JPanel(),
							"File Transfer Failed");
				}
			}
		}
	}

	
}
