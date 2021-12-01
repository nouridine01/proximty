package com.uqac.proximty.sockets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.uqac.proximty.PrefManager;
import com.uqac.proximty.R;
import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.entities.UserWithInterests;
import com.uqac.proximty.repositories.UserRepository;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
	public static final String INFO = "info";
	private int nb =0;
	List<Conversation> conversationList = new ArrayList<>();
	private static UserRepository userRepository;
	private PrefManager prefManager;
	AppDatabase appDatabase;
	UserDao userDao;
	Context context;

	public ServeurMT(Context context){
		userRepository = new UserRepository(context);
		//prefManager = new PrefManager(context);
		userDao=AppDatabase.getDatabase(context).userDao();
		this.context=context;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("demarrage du serveur");
			ServerSocket ss = new ServerSocket(port);
			
			while(true) {
				Socket s = ss.accept();
				++nb;
				Conversation c = new Conversation(s,nb,context);
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

		Context context;




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

		Conversation(Socket s,int n,Context context){
			this.s=s;
			num=n;
			this.context=context;
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
					Toast.makeText(context,"conv lancee",Toast.LENGTH_LONG).show();

					//tranmission des info perso du profil envoi du non pseudo et interet
					String lineread = br.readLine();
					if(lineread.equals(INFO)){
						User user=userRepository.getConnectedUser(1);//prefManager.getUserId()
						os.write(user.getPseudo().getBytes(StandardCharsets.UTF_8));
						//os.write(user.getPhoto().getBytes(StandardCharsets.UTF_8));// un bitmap normalement
						Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
								R.drawable.profil);
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
						byte[] byteArray = stream.toByteArray();
						os.write(byteArray.length);
						os.write(byteArray);
						bmp.recycle();

						UserWithInterests userWithInterests = userDao.getUserWithInterests(user.getUid());
						os.write(userWithInterests.interests.size());
						userWithInterests.interests.forEach(interest -> {
							try {
								os.write((int) interest.getId());
							} catch (IOException e) {
								e.printStackTrace();
							}

						});

					}else{
						while(true) {
							String s =br.readLine();
							//ajout du message dans la liste de message du chat

							if(!message.equals("")){
								os.write(message.getBytes(StandardCharsets.UTF_8));
								message = "";
							}

						}
					}



				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

		}
	}

}
