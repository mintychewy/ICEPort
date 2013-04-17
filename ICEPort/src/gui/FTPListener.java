package gui;

import iceworld.ICEWorldView;

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
			ss = new ServerSocket(LoginPage.me.getListeningPort());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(!ICEWorldView.terminateThread){
			System.out.println("!ICEWorldView.terminateThread");
			try{
				socket = ss.accept();
				in = new Scanner(socket.getInputStream());
	            out = new PrintWriter(socket.getOutputStream(), true);
	            System.out.println("FTP Request from "+socket.getInetAddress().getHostAddress());
	            LoginPage.app.view.notifyIncomingFTP((socket.getInetAddress().getHostAddress())+"");
	            socket.close();
	            
			} catch (Exception e){
				System.err.println("SOL2");
				e.printStackTrace();
			}
		}
	
	}
}
