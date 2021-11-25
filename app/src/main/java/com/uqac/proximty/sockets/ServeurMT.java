package com.uqac.proximty.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurMT extends Thread{
	private int nb =0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ServeurMT().start();
	}
	
	@Override
	public void run() {
		try {
			System.out.println("demarrage du serveur");
			ServerSocket ss = new ServerSocket(8070);
			
			while(true) {
				Socket s = ss.accept();
				++nb;
				new Conversation(s,nb).start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class Conversation extends Thread{
		private Socket s;
		private int num;
		Conversation(Socket s,int n){
			this.s=s;
			num=n;
		}
		@Override
		public void run() {
			try {
				InputStream is = s.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				
				String ip = s.getRemoteSocketAddress().toString();
				System.out.println("connexion du client "+ip);
				
				OutputStream os = s.getOutputStream();
				PrintWriter pw = new PrintWriter(os,true);
					pw.println("vous etes le client n� "+num);
				
				
				while(true) {
					String s =br.readLine();
					System.out.println("requete du client "+ip+" message "+s);
					pw.println(s.length());
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
