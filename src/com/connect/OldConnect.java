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

import android.util.Log;

public class OldConnect {

	/** default port **/
	private static final int SERVERPORT = 8080;
	/** port that user inputs **/
	private int userPort = -1;


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
	/** socket between client and server **/
	Socket socket;
	
	/** whether or not the client is connected yet **/
	boolean connected = false;
	/** true if connection was initialized as the server **/
	boolean isServer = false;
	/** true if connection was initialized as the client **/
	boolean isClient = false;

	/** Initialize Server Socket, begin listening for connections.
	 * Default port if not specified is 8080.
	 **/
	public void initServer()
	{
		isServer = true;
		serverIP = getLocalIpAddress();
		Thread serverThread = new Thread(new ServerThread());
		serverThread.start();
	}

	/** Initialize Server Socket with specific port, begin listening for connections
	 * 
	 * @param: port - port to be connected to.
	 **/
	public void initServer(int port)
	{
		isServer = true;
		userPort = port;
		serverIP = getLocalIpAddress();
		Thread serverThread = new Thread(new ServerThread());
		serverThread.start();
	}

	/** Close the Server Socket and end connections **/
	public void close()
	{
		if (isServer)
		{
			try {
				serverIn.close();
				serverOut.close();
				srvSocket.close();
				socket.close();
			}
			catch (Exception e)
			{
				//Terminate application if close socket fails
				System.exit(0);
			}
		}
		if (isClient)
		{
			try {
				clientIn.close();
				clientOut.close();
				socket.close();
			}
			catch (Exception e)
			{
				//Terminate application if close socket fails
				System.exit(0);
			}
		}
	}

	/** Returns the server phone's IP address 
	 * If called before server initialization, will return empty string.
	 * @return: server phone's IP address
	 * **/
	public String getServerIP()
	{
		return serverIP;
	}

	/** If Server's output stream has been initialized, send "msg"
	 * 
	 * @param: msg - the message to be sent.
	 **/
	 private void sendMsgFromServer(String msg)
	{
		if (serverOut != null)
			serverOut.println(msg);
	}

	/** If Server's input stream has been initialized, read from input stream 
	 * 
	 * @return: Return message from Client.
	 * **/
	private String getMsgToServer()
	{
		try {
			String fromClient = serverIn.readLine();
			final String returnValue = fromClient;

			return returnValue;
		}
		catch (Exception e) {
			//return null if no message found
			return null;
		}
	}

	/** Initialize Client Socket, attempt to connect to give IP address "ip" 
	 * 
	 * @return: true if initialization succeeds, false if initializations fails
	 * 
	 * @param: ip - the IP Address to be connected to.
	 **/
	public boolean initClient(String ip)
	{
		isClient = true;
		if (!connected) {

			serverIP = ip;
			if (!ip.equals("")) {
				Thread cThread = new Thread(new ClientThread());
				cThread.start();
				
				return true;
			}
		}
		
		return false;
	}

	/** Initialize Client Socket, attempt to connect to give IP address "ip" 
	 * 
	 * @return: true if initialization succeeds, false if initializations fails
	 * 
	 * @param: ip - the IP Address to be connected to.
	 * port - the port number to be connected to.
	 **/
	public boolean initClient(String ip, int port)
	{
		isClient = true;
		if (!connected) {

			serverIP = ip;
			userPort = port;
			if (!ip.equals("")) {
				Thread cThread = new Thread(new ClientThread());
				cThread.start();
			}
		}
		
		if (connected)
			return false;
		else
			return true;
	}

	/** If Client's output stream has been initialized, send "msg" 
	 * 
	 * @param: msg - the message to be sent.
	 **/
	private void sendMsgFromClient(String msg)
	{
		if (clientOut != null)
			clientOut.println(msg);
	}

	/** If Server's input stream has been initialized, read from input stream 
	 * 
	 * @return: Return message from Client.
	 * **/
	private String getMsgToClient()
	{
		try {
			String fromServer = clientIn.readLine();
			final String returnValue = fromServer;

			return returnValue;
		}
		catch (Exception e) {
			return null;
		}
	}

	
	/** If the output stream has been initialized, 
	 * send "msg" to the other side of the connection. 
	 * 
	 * @param: msg - the message to be sent.
	 * **/
	public void sendMsg(String msg)
	{
		if (isServer)
		{
			if (serverOut != null)
				serverOut.println(msg);
		}
		else if (isClient)
		{
			if (clientOut != null)
				clientOut.println(msg);
		}
	}
	
	/** If the input stream has been initialized, read from input stream
	 * and return the result 
	 * 
	 * @return: Return message from other end of connection.
	 * 
	 * **/
	public String getMsg()
	{
		if (isServer)
		{
			try {
				String fromClient = serverIn.readLine();
				final String returnValue = fromClient;

				return returnValue;
			}
			catch (Exception e) {
				//return null if no message found
				return null;
			}
		} else if (isClient)
		{
			try {
				String fromServer = clientIn.readLine();
				final String returnValue = fromServer;

				return returnValue;
			}
			catch (Exception e) {
				//return null if no message found
				return null;
			}
		}
		else 
			//return null if no message found
			return null;
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
			Log.e("Connect", ex.toString());
		}
		return null;
	}

	/** Create a separate thread to create server socket connection **/
	private class ServerThread implements Runnable {
		public void run() {
			try {
				if (serverIP != null) {

					//check if a specific port was defined
					if (userPort != -1)
						srvSocket = new ServerSocket(userPort);
					//else, open server socket with default port
					else
						srvSocket = new ServerSocket(SERVERPORT);


					while (true) {
						//begin listening, accept when it comes
						socket = srvSocket.accept();

						//init I/O streams
						try {
							//initialize server input stream
							serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							//initialize server output stream
							// set true, for auto-flushing after print statements
							serverOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

							break;
						} catch (Exception e) {
							//Server Initialization fails
							e.printStackTrace();
						}
					}
				} else {
					Log.e("Connect", "Null Server IP Address");
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
					InetAddress serverAddr = InetAddress.getByName(serverIP);

					//try to connect to a specific port, if given
					if (userPort != -1)
						socket = new Socket(serverAddr, userPort);
					//else, open socket with default port
					else
						socket = new Socket(serverAddr, SERVERPORT);

					if (socket != null)
						connected = true;

					while (connected) {
						try {
							//initialize client input stream
							clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							//initialize client output stream
							// set true, for auto-flushing after print statements
							clientOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
							
							break;
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
