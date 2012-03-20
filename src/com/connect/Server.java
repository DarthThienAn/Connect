package com.connect;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Server {

	private String ip;
	private BufferedReader in;
	private PrintWriter out;
	private int port;
	private Socket skt;
	
	public Server()
	{
		ip = getLocalIpAddress();
	}
	
	public Server(String ip)
	{
		this.ip = ip;
	}
	
	public void sendMsg(String msg)
	{
		out.println(msg);
	}
	
	public String getMsg()
	{
		try {
			return in.readLine();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public String getIP()
	{
		return ip;
	}
	
	public boolean close()
	{
		try {
			skt.close();
			return true;
		}
		catch (Exception e) {
			return false;
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
}
