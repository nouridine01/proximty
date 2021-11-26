package com.uqac.proximty.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//a demarrer lors du lancement du scannage
public class ServeurMT extends Thread{
	public static final int port = 8988;
	private int nb =0;
	List<Conversation> conversationList = new ArrayList<>();
	
	@Override
	public void run() {
		try {
			System.out.println("demarrage du serveur");
			ServerSocket ss = new ServerSocket(port);
			
			while(true) {
				Socket s = ss.accept();
				++nb;
				Conversation c = new Conversation(s,nb);
				conversationList.add(c);
				c.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class Conversation extends Thread{
		private Socket s;
		private int num;
		private String message ="";



		public Socket getS() {
			return s;
		}

		public void setS(Socket s) {
			this.s = s;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

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


					//tranmission des info perso du profil envoi du non pseudo et interet

					while(true) {
						String s =br.readLine();
						//ajout du message dans la liste de message du chat

						if(!message.equals("")){
							os.write(message.getBytes(StandardCharsets.UTF_8));
							message = "";
						}

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
	}

}
