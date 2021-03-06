package com.connect;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;

public class ServerActivity
{
	String ip;
	int SERVERPORT = 8080;
	ServerSocket srvSocket;
	
	public String initServer()
	{
		ip = getLocalIpAddress();
		
		try {
			srvSocket = new ServerSocket(SERVERPORT);
		}
		catch (Exception e)
		{
			
		}
		
		
		return ip;
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