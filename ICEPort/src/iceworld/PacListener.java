package iceworld;


import gui.LoginPage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class PacListener implements Runnable {
	 private volatile PrintWriter out;
	 private Scanner in;
	 
	public void run(){

		Socket socket;
		ServerSocket ss = null;
		try {
			System.out.println("establishing server at port :" +(LoginPage.me.getuid()+8000));
			ss = new ServerSocket(LoginPage.me.getuid() + 8000);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(!ICEWorldView.terminateThread){
			try{
				socket = ss.accept();
				in = new Scanner(socket.getInputStream());
	            out = new PrintWriter(socket.getOutputStream(), true);
	            System.out.println("PAC Request from "+socket.getInetAddress());
	            LoginPage.app.view.notifyIncomingPac((socket.getInetAddress())+"");
	            socket.close();
	            
			} catch (Exception e){
				System.err.println("SOL");
				e.printStackTrace();
			}
		}
	
	}
}
