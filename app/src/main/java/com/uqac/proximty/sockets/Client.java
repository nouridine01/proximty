package com.uqac.proximty.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread{

	private Socket serveur;

	private Socket client;



	public Client(Socket serveur){
		//this.client=client;
		this.serveur=serveur;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			System.out.println("connexion avec le serveur");

			
			InputStream is = serveur.getInputStream();
			OutputStream os = serveur.getOutputStream();

			//recup les donnees

			while (true){

				System.out.println("nombre envoy�e au serveur");
				//os.write();

				System.out.println("reponse du serveur");
				//is.read();

			}

			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				serveur.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
