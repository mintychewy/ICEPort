package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FTPListener implements Runnable {
	 private volatile PrintWriter out;
	 private Scanner in;
	 
	public void run(){

		Socket socket;
		ServerSocket ss = null;
		try {
			//ss = new ServerSocket(LoginPage.me.getListeningPort());
			ss = new ServerSocket(9000);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(true){
			try{
				socket = ss.accept();
				in = new Scanner(socket.getInputStream());
	            out = new PrintWriter(socket.getOutputStream(), true);
	            System.out.println("FTP Request from "+socket.getInetAddress());
	            LoginPage.app.view.notifyIncomingFTP((socket.getInetAddress())+"");
	            socket.close();
	            
			} catch (Exception e){
				System.err.println("SOL");
				e.printStackTrace();
			}
		}
	
	}
}
