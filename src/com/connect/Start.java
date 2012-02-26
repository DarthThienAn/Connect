package com.connect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Start extends Activity {

	private static final String TAG = "SomethingView";

	/**
	 * mode macros
	 */
	public static final int PAUSE = 0;
	public static final int READY = 1;
	public static final int RUNNING = 2;
	public static final int LOSE = 3;
	public static final int WIN = 4;

	String ICICLE_KEY = "Something-View";
	private TextView startText;

	// server
	// DEFAULT IP
	public static String SERVERIP = "";
	// DESIGNATE A PORT
	public static final int SERVERPORT = 8080;
	private TextView serverStatus;
	private ServerSocket serverSocket;
	private PrintWriter serverOut;
	private PrintWriter clientOut;
	private EditText serverMsg;
	private EditText clientMsg;
	private boolean serverSide = false;
	private boolean clientSide = false;
	protected static final int MSG_ID = 0x1337;

	private Handler handler = new Handler();

	// client
	private TextView clientStatus;
	private EditText serverIp;
	private Button connectPhones;
	private String serverIpAddress = "";
	private boolean connected = false;

	private Button serverButton;
	private Button clientButton;
	private String fromClient = "";
	private String fromServer = "";

	private RefreshHandler mRefreshHandler = new RefreshHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.start);

		startText = (TextView) findViewById(R.id.start_text);
		serverButton = (Button) findViewById(R.id.server);
		serverButton.setOnClickListener(serverClick);
		clientButton = (Button) findViewById(R.id.client);
		clientButton.setOnClickListener(clientClick);

	}

	private OnClickListener serverClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			serverSide = true;
			
			setContentView(R.layout.server);

			serverStatus = (TextView) findViewById(R.id.server_status);
			SERVERIP = getLocalIpAddress();
			serverStatus.setText("Waiting for connection at: " + SERVERIP);
			serverMsg = (EditText) findViewById(R.id.server_msg);

			Thread serverThread = new Thread(new ServerThread());
			serverThread.start();
		}
	};

	private OnClickListener clientClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clientSide = true;

			setContentView(R.layout.client);

			clientStatus = (TextView) findViewById(R.id.client_status);
			serverIp = (EditText) findViewById(R.id.server_ip);
			connectPhones = (Button) findViewById(R.id.connect_phones);
			connectPhones.setOnClickListener(connectClick);

			clientStatus.setText("Enter an IP Address to connect");
		}
	};

	private OnClickListener connectClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!connected) {
				clientStatus.setText("Attempting to connect...");

				serverIpAddress = serverIp.getText().toString();
				if (!serverIpAddress.equals("")) {
					Thread cThread = new Thread(new ClientThread());
					cThread.start();
				}
			} else
				clientStatus.setText("???");

		}
	};
 	
	public String getCurrentIpAddress () {
	    try {
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpGet httpget = new HttpGet("http://www.whatismyip.org");
	            HttpResponse response;

	            response = httpclient.execute(httpget);

	            HttpEntity entity = response.getEntity();
	            if (entity != null) 
	            {
	                    long len = entity.getContentLength();
	                    if (len != -1 && len < 1024) 
	                            return EntityUtils.toString(entity);
	                    else
	                            return "Response too long or error.";
	            } else {
	                    return ("Null:" + response.getStatusLine().toString());
	            }

	    }
	    catch (Exception e)
	    {
	    	return "1";
	    }
	}
	// GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
	private String getLocalIpAddress() {
//		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE); WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//
//		int ipAddress = wifiInfo.getIpAddress();
//
//		return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
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

	class ServerThread implements Runnable {
		public void run() {
			try {
				if (SERVERIP != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							serverStatus
									.setText("Listening on IP: " + SERVERIP);
						}
					});
					serverSocket = new ServerSocket(SERVERPORT);

					while (true) {
						// LISTEN FOR INCOMING CLIENTS
						Socket client = serverSocket.accept();
						handler.post(new Runnable() {
							@Override
							public void run() {
								serverStatus.setText("Connected.");
							}
						});

						try {
							BufferedReader serverIn = new BufferedReader(
									new InputStreamReader(
											client.getInputStream()));
							// set true, for auto-flushing after print
							// statements
							serverOut = new PrintWriter(new BufferedWriter(
									new OutputStreamWriter(
											client.getOutputStream())), true);
							serverOut.println(serverMsg.getText().toString());

							while ((fromClient = serverIn.readLine()) != null) {
								
									final String sendValue = fromClient;
									handler.post(new Runnable() {
										@Override
										public void run() {
											serverStatus.setText(sendValue);
										}
									});

									update();
									mRefreshHandler.sleep(5);
							}
							break;
						} catch (Exception e) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									serverStatus
											.setText("Oops. Connection interrupted.");
								}
							});
							e.printStackTrace();
						}
					}
				} else {
					handler.post(new Runnable() {
						@Override
						public void run() {
							serverStatus
									.setText("Couldn't detect a connection.");
						}
					});
				}
			} catch (Exception e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						serverStatus.setText("ERROR");
					}
				});
				e.printStackTrace();
			}
		}
	}

	public class ClientThread implements Runnable {
		public void run() {
			try {
				if (!connected) {
					InetAddress serverAddr = InetAddress
							.getByName(serverIpAddress);
					// connecting
					Socket server = new Socket(serverAddr, SERVERPORT);
					if (server != null)
						connected = true;

					while (connected) {
						
						handler.post(new Runnable() {
							@Override
							public void run() {
								setContentView(R.layout.client2);

								clientStatus = (TextView) findViewById(R.id.client2_status);
								clientMsg = (EditText) findViewById(R.id.client2_msg);
								clientStatus.setText("Waiting for Message");
								
								clientStatus.setText("Connected");
							}
						});
						
						try {
							BufferedReader clientIn = new BufferedReader(
									new InputStreamReader(
											server.getInputStream()));
							// set true, for auto-flushing after print
							// statements
							
							clientOut = new PrintWriter(new BufferedWriter(
									new OutputStreamWriter(
											server.getOutputStream())), true);
							clientOut.println(clientMsg.getText().toString());
							
							while ((fromServer = clientIn.readLine()) != null) {
								final String sendValue = fromServer;
								handler.post(new Runnable() {
									@Override
									public void run() {
										clientStatus.setText(sendValue);
									}
								});
								update();
								mRefreshHandler.sleep(5);
							}
							break;
						} catch (Exception e) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									clientStatus
											.setText("Oops. Connection interrupted");
								}
							});
							e.printStackTrace();
						}
					}
				} else {
					handler.post(new Runnable() {
						@Override
						public void run() {
							clientStatus.setText("Couldn't detect");
						}
					});
				}
			} catch (Exception e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						clientStatus.setText("ERROR");
					}
				});
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onStop() {
		super.onStop();

			try {
				// CLOSE THE SOCKET UPON EXITING
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		System.exit(0);
	}
	
	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			update();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};
	
	public void update() {
		if (serverSide)
			serverOut.println(serverMsg.getText().toString());
		else
			clientOut.println(clientMsg.getText().toString());

		mRefreshHandler.sleep(5);
	}
}
