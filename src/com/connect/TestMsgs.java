package com.connect;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestMsgs extends Activity {

	MainTest test = new MainTest();

	private int testCount = 0;
	
	TextView text;
	
	private Button serverButton;
	private Button clientButton;
	private Button connectButton;
	private boolean serverSide = false;
	private boolean clientSide = false;
	private EditText serverMsg;
	private EditText clientMsg;
	private EditText serverIp;
	private boolean connected = false;
	
	private RefreshHandler mRefreshHandler = new RefreshHandler();

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
			test.sendMsgFromServer(serverMsg.getText().toString());
		else if (clientSide)
			test.sendMsgFromClient(clientMsg.getText().toString());

		if (serverSide)
		{
			String x = test.getMsgToServer();
			if (x == null)
				text.setText("No msg");
			else
				text.setText(x);
		}
		else if (clientSide)
		{
			String x = test.getMsgToClient();
			if (x == null)
				text.setText("No msg");
			else
				text.setText(x);
		}
		
		mRefreshHandler.sleep(5);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.start);
		text = (TextView) findViewById(R.id.main_text);
		serverButton = (Button) findViewById(R.id.server);
		serverButton.setOnClickListener(serverClick);
		clientButton = (Button) findViewById(R.id.client);
		clientButton.setOnClickListener(clientClick);
	}
	
	private OnClickListener serverClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setContentView(R.layout.server);

			text = (TextView) findViewById(R.id.server_status);
			serverMsg = (EditText) findViewById(R.id.server_msg);
			serverButton = (Button) findViewById(R.id.server_check);
			serverButton.setOnClickListener(check);
			
			test.initServer();
			text.setText("Server initialized, IP: " + test.getServerIP());
			
			serverSide = true;
		}
	};

	private OnClickListener clientClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			clientSide = true;
			setContentView(R.layout.client);

			text = (TextView) findViewById(R.id.client_status);
			serverIp = (EditText) findViewById(R.id.server_ip);
			connectButton = (Button) findViewById(R.id.connect_phones);
			connectButton.setOnClickListener(connectClick);

			text.setText("Enter an IP Address to connect to");
		}
//			clientSide = true;
//			
//			setContentView(R.layout.client);
//			text = (TextView) findViewById(R.id.client_status);
//			text.setText("Enter an IP Address to connect");
//
//			EditText serverIp = (EditText) findViewById(R.id.server_ip);
//			String serverIpAddress = serverIp.getText().toString();
//
//
//			if(test.initClient(serverIpAddress))
//			{
//				setContentView(R.layout.client2);
//
//				text = (TextView) findViewById(R.id.client2_status);
//				clientMsg = (EditText) findViewById(R.id.client2_msg);
//				text.setText("Client initialized");
//			}
//			else
//				text.setText("Client initialization failed");
//		}
	};	
	

	private OnClickListener connectClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!connected) {
				text.setText("Attempting to connect...");

				String serverIpAddress = serverIp.getText().toString();
				if (!serverIpAddress.equals(""))
				{
					if(test.initClient(serverIpAddress))
					{
						connected = true;
						setContentView(R.layout.client2);

						text = (TextView) findViewById(R.id.client2_status);
						clientMsg = (EditText) findViewById(R.id.client2_msg);

						text.setText("Client Initialized");
						clientSide = true;
						
						clientButton = (Button) findViewById(R.id.client_check);
						clientButton.setOnClickListener(check);
					}
//					while (true)
//					{
//						mRefreshHandler.sleep(5);
//					}
				}
				else
					text.setText("Client Initialization failed");
			}
		}
	};
	
	private OnClickListener check = new OnClickListener() {
		@Override
		public void onClick(View v) {
			update();
		}
	};

	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent msg) {
//		
//		String ip = "";
//		
//		if (testCount == 0)
//		{
//			test.initServer();
//			text.setText("Server initialized");
//		}
//		if (testCount == 1)
//		{
//			ip = test.getServerIP();
//			text.setText("Server IP found: " + ip);
//		}
//		if (testCount == 2)
//		{
//			if(test.initClient(ip))
//				text.setText("Client initialized");
//			else
//				text.setText("Client initialization failed");
//		}
//		if (testCount == 3)
//		{
//			test.sendMsgFromServer("Greetings");
//			text.setText("Message from Server Sent : ?");
//		}
//		if (testCount == 4)
//		{
//			String sMsg = test.getMsgToClient();
//			text.setText("Message from Server Sent : " + sMsg);
//		}
//		if (testCount == 5)
//		{
//			test.sendMsgFromClient("And hello to you!");
//			text.setText("Message from Client Sent : ?");
//		}
//		if (testCount == 6)
//		{
//			String cMsg = test.getMsgToServer();
//			text.setText("Message from Client Sent : " + cMsg);
//		}
//		if (testCount == 7)
//		{
//			test.close();
//			text.setText("Socket Closed");
//		}
//		
//		testCount++;
//		
//		return super.onKeyDown(keyCode, msg);
//	}	
	
	@Override
	protected void onStop() {
		super.onStop();

		test.close();
		System.exit(0);
	}

}
