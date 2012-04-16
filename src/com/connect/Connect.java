/**
 * Remember that AndroidManifest.xml needs to give permission for connectivity
 * 
 * Enter lines into AndroidManifest, directly under the <manifest> tag:
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 */

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

public class Connect {

	/** default port **/
	private static final int SERVERPORT = 8080;
	private int serverPort = 0;
	

	/** input stream from server **/
	BufferedReader serverIn;
	/** output stream from server **/
	PrintWriter serverOut;
	/** input stream from client **/
	BufferedReader clientIn;
	/** output stream from client **/
	PrintWriter clientOut;
	/** server phone's IP **/
	String serverIP = "";
	/** server socket that will initialize other sockets **/
	ServerSocket srvSocket;
	/** whether or not the client is connected yet **/
	boolean connected;
	
	/** Initialize Server Socket, begin listening for connections **/
	public void initServer()
	{
		Thread serverThread = new Thread(new ServerThread());
		serverThread.start();
		serverIP = getLocalIpAddress();
	}

	/** Initialize Server Socket with specific port, begin listening for connections **/
	public void initServer(int port)
	{
		serverPort = port;
		Thread serverThread = new Thread(new ServerThread());
		serverThread.start();
		serverIP = getLocalIpAddress();
	}

	/** Close the Server Socket and end connections **/
	public void close()
	{
		try {
			srvSocket.close();
		}
		catch (Exception e)
		{
			//Terminate application if close socket fails
			System.exit(0);
		}
	}
	
	/** Returns the server phone's IP address 
	 * 
	 * @return: server phone's IP address
	 * **/
	public String getServerIP()
	{
		return serverIP;
	}
	
	/** If Server's output stream has been initialized, send "msg" **/
	public void sendMsgFromServer(String msg)
	{
		if (serverOut != null)
			serverOut.println(msg);
	}
	
	/** If Server's input stream has been initialized, read from input stream 
	 * 
	 * @return: Return message from Client.
	 * **/
	public String getMsgToServer()
	{
		try {
			String fromClient = serverIn.readLine();
			final String sendValue = fromClient;
			
			return sendValue;
		}
		catch (Exception e) {
			//return null if no message found
			return null;
		}
	}

	/** Initialize Client Socket, attempt to connect to give IP address "ip" 
	 * 
	 * @return: true if initialization succeeds, false if initializations fails
	 * **/
	public boolean initClient(String ip)
	{
		if (!connected) {
			
			serverIP = ip;
			if (!ip.equals("")) {
				Thread cThread = new Thread(new ClientThread());
				cThread.start();
			}
			
			return true;
		} else
			return false;
	}
	
	/** Initialize Client Socket, attempt to connect to give IP address "ip" 
	 * 
	 * @return: true if initialization succeeds, false if initializations fails
	 * **/
	public boolean initClient(String ip, int port)
	{
		if (!connected) {
			
			serverIP = ip;
			serverPort = port;
			if (!ip.equals("")) {
				Thread cThread = new Thread(new ClientThread());
				cThread.start();
			}
			
			return true;
		} else
			return false;
	}
	
	/** If Client's output stream has been initialized, send "msg" **/
	public void sendMsgFromClient(String msg)
	{
		if (clientOut != null)
			clientOut.println(msg);
	}
	
	/** If Server's input stream has been initialized, read from input stream 
	 * 
	 * @return: Return message from Client.
	 * **/
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
	
	/** Function to find phone's IP Address **/
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
	
	/** Create a separate thread to create server socket connection **/
	private class ServerThread implements Runnable {
		public void run() {
			try {
				if (serverIP != null) {
					
					//check if a specific port was defined
					if (serverPort != 0)
						srvSocket = new ServerSocket(serverPort);
					//else, open server socket with default port
					else
						srvSocket = new ServerSocket(SERVERPORT);


					while (true) {
						//begin listening, accept when it comes
						Socket client = srvSocket.accept();

						//init I/O streams
						try {
							//initialize server input stream
							serverIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
							//initialize server output stream
							// set true, for auto-flushing after print statements
							serverOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
	
							break;
						} catch (Exception e) {
							//Server Initialization fails
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
	
	/** Create a separate thread to create client socket connection **/
	private class ClientThread implements Runnable {
		public void run() {
			try {
				//only try if not already connected
				if (!connected) {
					Socket server;
					InetAddress serverAddr = InetAddress.getByName(serverIP);

					//try to connect to a specific port, if given
					if (serverPort != 0)
						server = new Socket(serverAddr, serverPort);
					//else, open socket with default port
					else
						server = new Socket(serverAddr, SERVERPORT);
					
					if (server != null)
						connected = true;

					while (connected) {
						try {
							//initialize client input stream
							clientIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
							//initialize client output stream
							// set true, for auto-flushing after print statements
							clientOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					//connected already
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
