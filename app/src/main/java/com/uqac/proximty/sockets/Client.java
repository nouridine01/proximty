package com.uqac.proximty.sockets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.uqac.proximty.R;
import com.uqac.proximty.fragments.Scan_page;

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

    private boolean info =false;




    public Client(Socket serveur, Context context, boolean info){
        //this.client=client;
        this.serveur=serveur;
        this.context=context;
        this.info=info;
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
            String msg = br.readLine();
            Toast.makeText(context,"message = " + msg,Toast.LENGTH_LONG).show();
			/*if(info==true){

				//recup les donnees
				os.write(ServeurMT.INFO.getBytes(StandardCharsets.UTF_8));
				String pseudo = br.readLine();
				//Toast.makeText(context,"pseudo = " + pseudo,Toast.LENGTH_LONG).show();
				Scan_page.pseudo=pseudo;
				int byteSize=is.read();
				//Toast.makeText(context,"byteSize = " + byteSize,Toast.LENGTH_LONG).show();
				byte[] byteArray = new byte[byteSize];
				is.read(byteArray);
				int size = is.read();
				Scan_page.interests.clear();
				for (int i=0;i<size;i++){
					String interest= br.readLine();
					//Toast.makeText(context,"interest " + interest + " added",Toast.LENGTH_LONG).show();
					Scan_page.interests.add(interest);
				}

				//retrieve image after
				byte[] byteArrayBitmap = new byte[is.read()];
				Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayBitmap, 0, byteArrayBitmap.length);

				//Scan_page.bottomSheetView.findViewById(R.id.);
				return ;
			}


			while (true){

				System.out.println("nombre envoy�e au serveur");
				String message = br.readLine();
				//Toast.makeText(context,"identifiant invalide",Toast.LENGTH_LONG).show();
			}*/



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
