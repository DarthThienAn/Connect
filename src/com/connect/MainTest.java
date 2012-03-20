package com.connect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

//import com.connect.Start.ClientThread;
//import com.connect.Start.ServerThread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
//import android.widget.EditText;
import android.widget.TextView;

public class MainTest extends Activity {

	public static final int SERVERPORT = 8080;

	/** input stream from server **/
	BufferedReader serverIn;
	/** output stream from server **/
	PrintWriter serverOut;
	/** input stream from client **/
	BufferedReader clientIn;
	/** output stream from client **/
	PrintWriter clientOut;
	/** server socket that will initialize other sockets **/
	String serverIP = "";
	/** server socket that will initialize other sockets **/
	ServerSocket srvSocket;
	/** whether or not the client is connected yet **/
	boolean connected;
	
	private int testCount = 0;
	
	//for testing
	TextView text;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		text = (TextView) findViewById(R.id.main_text);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		
		String ip = "";
		
		if (testCount == 0)
		{
			initServer();
			text.setText("Server initialized");
		}
		if (testCount == 1)
		{
			ip = getServerIP();
			text.setText("Server IP found: " + ip);
		}
		if (testCount == 2)
		{
			if(initClient(ip))
				text.setText("Client initialized");
			else
				text.setText("Client initialization failed");
		}
		if (testCount == 3)
		{
			sendMsgFromServer("Greetings");
			text.setText("Message from Server Sent : ?");
		}
		if (testCount == 4)
		{
			String sMsg = getMsgToClient();
			text.setText("Message from Server Sent : " + sMsg);
		}
		if (testCount == 5)
		{
			sendMsgFromClient("And hello to you!");
			text.setText("Message from Client Sent : ?");
		}
		if (testCount == 6)
		{
			String cMsg = getMsgToServer();
			text.setText("Message from Client Sent : " + cMsg);
		}
		if (testCount == 7)
		{
			close();
			text.setText("Socket Closed");
		}
		
		testCount++;
		
		return super.onKeyDown(keyCode, msg);
	}	
	@Override
	protected void onStop() {
		super.onStop();

		System.exit(0);
	}

	public void initServer()
	{
		Thread serverThread = new Thread(new ServerThread());
		serverThread.start();
		serverIP = getLocalIpAddress();

//		try {
//			if (serverIP != null) {
//
//				//open server socket
//				srvSocket = new ServerSocket(SERVERPORT);
//				handler.post(new Runnable() {
//					@Override
//					public void run() {
//						text.setText("2");
//					}
//				});
//
//				while (true) {
//					//begin listening, accept when it comes
//					Socket client = srvSocket.accept();
//
//					//init I/O streams
//					try {
//						serverIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
//						// set true, for auto-flushing after print statements
//						serverOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
//
//						handler.post(new Runnable() {
//							@Override
//							public void run() {
//								text.setText("3");
//							}
//						});
//						
//						break;
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			} else {
//				//couldn't detect
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void close()
	{
		try {
			srvSocket.close();
		}
		catch (Exception e)
		{
			System.exit(0);
		}
	}
	
	public String getServerIP()
	{
		return serverIP;
	}
	
	public void sendMsgFromServer(String msg)
	{
		if (serverOut != null)
			serverOut.println(msg);
	}
	
	public String getMsgToServer()
	{
		try {
			String fromClient = serverIn.readLine();
			final String sendValue = fromClient;
			
			return sendValue;
		}
		catch (Exception e) {
			return null;
		}
	}

	public boolean initClient(String ip)
	{
		if (!connected) {
			
			serverIP = getServerIP();
			if (!serverIP.equals("")) {
				Thread cThread = new Thread(new ClientThread());
				cThread.start();
			}
			
			return true;
		} else
		{
			//error
		}

		
//		try {
//			if (!connected) {
//				InetAddress serverAddr = InetAddress.getByName(ip);
//				// connecting
//				Socket server = new Socket(serverAddr, SERVERPORT);
//				if (server != null)
//					connected = true;
//
//				while (connected) {
//					try {
//						clientIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
//						// set true, for auto-flushing after print statements
//						clientOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);
//						
//						return true;
////						
////						break;
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			} else {
//				//couldn't detect
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return false;
	}
	
	public void sendMsgFromClient(String msg)
	{
		if (clientOut != null)
			clientOut.println(msg);
	}
	
	public String getMsgToClient()
	{
		try {
			String fromServer = clientIn.readLine();
			final String sendValue = fromServer;
			
			return sendValue;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	private String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			// Log.e("Start", ex.toString());
		}
		return null;
	}
	
	public class ServerThread implements Runnable {
		public void run() {
			try {
				if (serverIP != null) {
					
					//open server socket
					srvSocket = new ServerSocket(SERVERPORT);
					
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							text.setText("Listening on IP: " + serverIP);
//						}
//					});

					while (true) {
						//begin listening, accept when it comes
						Socket client = srvSocket.accept();

//						handler.post(new Runnable() {
//							@Override
//							public void run() {
//								text.setText("2");
//							}
//						});
						
						//init I/O streams
						try {
							serverIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
							// set true, for auto-flushing after print statements
							serverOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
	
//							handler.post(new Runnable() {
//								@Override
//								public void run() {
//									text.setText("3");
//								}
//							});
							
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class ClientThread implements Runnable {
		public void run() {
			try {
				if (!connected) {
					InetAddress serverAddr = InetAddress
							.getByName(getServerIP());
					// connecting
					Socket server = new Socket(serverAddr, SERVERPORT);
					if (server != null)
						connected = true;

					while (connected) {

//						handler.post(new Runnable() {
//							@Override
//							public void run() {
//								setContentView(R.layout.client2);
//
//								clientStatus = (TextView) findViewById(R.id.client2_status);
//								clientMsg = (EditText) findViewById(R.id.client2_msg);
//								clientStatus.setText("Waiting for Message");
//
//								clientStatus.setText("Connected");
//							}
//						});

						try {
							clientIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
							// set true, for auto-flushing after print statements

							clientOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							text.setText("Couldn't detect");
//						}
//					});
				}
			} catch (Exception e) {
//				handler.post(new Runnable() {
//					@Override
//					public void run() {
//						text.setText("ERROR");
//					}
//				});
				e.printStackTrace();
			}
		}
	}
	
	
}
