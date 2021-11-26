package com.uqac.proximty.sockets;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client extends Thread{

	private Socket serveur;

	private Socket client;

	private Context context;


	public Client(Socket serveur, Context context){
		//this.client=client;
		this.serveur=serveur;
		this.context=context;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			System.out.println("connexion avec le serveur");


			InputStream is = serveur.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			OutputStream os = serveur.getOutputStream();
			PrintWriter pw = new PrintWriter(os,true);
			//recup les donnees

			os.write(ServeurMT.INFO.getBytes(StandardCharsets.UTF_8));
			while (true){

				System.out.println("nombre envoy�e au serveur");
				String message = br.readLine();
				Toast.makeText(context,"identifiant invalide",Toast.LENGTH_LONG).show();

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
