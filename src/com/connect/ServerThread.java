//package com.connect;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class ServerThread implements Runnable {
//	
//	public String serverIP = "";
//	public ServerSocket 
//	
//	public void run() {
//		try {
//			if (serverIP != null) {
//				
//				//open server socket
//				srvSocket = new ServerSocket(SERVERPORT);
//				
////				handler.post(new Runnable() {
////					@Override
////					public void run() {
////						text.setText("Listening on IP: " + serverIP);
////					}
////				});
//
//				while (true) {
//					//begin listening, accept when it comes
//					Socket client = srvSocket.accept();
//
////					handler.post(new Runnable() {
////						@Override
////						public void run() {
////							text.setText("2");
////						}
////					});
//					
//					//init I/O streams
//					try {
//						serverIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
//						// set true, for auto-flushing after print statements
//						serverOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
//
////						handler.post(new Runnable() {
////							@Override
////							public void run() {
////								text.setText("3");
////							}
////						});
//						
//						break;
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			} else {
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
