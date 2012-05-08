package com.connect;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Echo extends Activity {

	Connect test = new Connect();
	
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
		Log.d("updating", ".");
		if (serverSide)
			test.sendMsg(serverMsg.getText().toString());
//		else if (clientSide)
//			test.sendMsg(clientMsg.getText().toString());
//
//		if (serverSide)
//		{
//			String message = test.getMsg();
//			if (message == null)
//				text.setText("No msg");
//			else
//				text.setText(message);
//		}
		else if (clientSide)
		{
			String message = test.getMsg();
			if (message == null)
				text.setText("No msg");
			else
				text.setText(message);
		}
		
		mRefreshHandler.sleep(250);
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
			
			update();
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
						
						update();
						
						clientButton = (Button) findViewById(R.id.client_check);
						clientButton.setOnClickListener(check);
					}
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

	@Override
	protected void onStop() {
		super.onStop();

		test.close();
		System.exit(0);
	}

	@Override
	protected void onPause() {
		super.onPause();

		test.close();
		System.exit(0);
	}
}
