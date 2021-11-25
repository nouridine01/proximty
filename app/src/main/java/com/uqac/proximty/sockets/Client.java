package com.uqac.proximty.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			System.out.println("connexion avec le serveur");
			Socket s = new Socket("localhost", 8080);
			
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			
			System.out.println("donner un nombre");
			Scanner sc = new Scanner(System.in);
			
			
			int n = sc.nextInt();
			
			System.out.println("nombre envoy�e au serveur");
			os.write(n);
			
			System.out.println("reponse du serveur");
			n= is.read();
			System.out.println(n);
			s.close();
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
