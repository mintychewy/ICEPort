package gui;

import java.awt.FileDialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import objects.ICEtizen;

public class FileServerClient3 extends FTPButtonListener {
	File myfile;
	Frame myFrame = new Frame();
	//ICEtizen host;
	JDialog AorD;
	Socket sock = null;
	
	public void receiveFileFromServer(String host, int port)
			throws ClassNotFoundException,// overwrite frm no parameter -> 2
											// parameters
			IOException {
		

		// overwrite

		host = use.getIPAddress();
		//host = "124.120.18.16";
		port = use.getListeningPort();
		try {
			System.out.print("yoyo");
			sock = new Socket(host, port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Connection made (clientSide)");
		// recieve the file
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/* file from server is deserialized */
		myfile = (File) ois.readObject();
		/* deserialized file properties */
		System.out.println("AbsolutePath: " + myfile.getAbsolutePath());
		System.out.println("FileName:" + myfile.getName());
		System.out.println("lenght" + myfile.length());

		AorD = new JDialog(myFrame, "You've got a message.");
		JPanel pan = new JPanel();

		JButton acceptButton = new JButton("Accept");
		pan.add(acceptButton);
		AcceptEvent a = new AcceptEvent();
		acceptButton.addActionListener(a);

		JButton declineButton = new JButton("Decline");
		DeclineEvent d = new DeclineEvent();
		declineButton.addActionListener(d);

		AorD.add(pan);

	}

	public class AcceptEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			FileDialog choo = new FileDialog(myFrame,
					"Choose File Destination", FileDialog.SAVE);
			choo.setDirectory(null);
			choo.setFile("enter file name here");
			choo.setVisible(true);
			String targetFileName = choo.getDirectory() + choo.getFile();
			System.out.println("File will be saved to: " + targetFileName);
			try {
				copyBytes(myfile, targetFileName);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}//

		private void copyBytes(File originalFile, String targetFileName)
				throws IOException {
			FileInputStream in = null;
			FileOutputStream out = null;
			in = new FileInputStream(originalFile);
			out = new FileOutputStream(targetFileName);
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			out.close();
			in.close();
		}//
	}

	public class DeclineEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				sock.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			AorD.dispose();
		}
	}
}

/*
 * public static void main(String[] args) { FileServerClient3 client = new
 * FileServerClient3(); String host = "110.168.42.153"; int port = 8799;
 * 
 * try { client.receiveFileFromServer(host, port); } catch (UnknownHostException
 * e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
 * (ClassNotFoundException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } catch (Exception e) { e.printStackTrace(); } }
 */
// /