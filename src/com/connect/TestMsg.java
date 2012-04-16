package com.connect;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

public class TestMsg extends Activity {

	Connect test = new Connect();

	private int testCount = 0;
	private String ip = "";
	
	TextView text;
	
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
		if (testCount == 0)
		{
			test.initServer();
			text.setText("Server initialized");
		}
		if (testCount == 1)
		{
			ip = test.getServerIP();
			text.setText("Server IP found: " + ip);
		}
		if (testCount == 2)
		{
			if(test.initClient(ip))
				text.setText("Client initialized");
			else
				text.setText("Client initialization failed");
		}
		if (testCount == 3)
		{
			test.sendMsgFromServer("Greetings");
			text.setText("Message from Server Sent : ?");
		}
		if (testCount == 4)
		{
			String sMsg = test.getMsgToClient();
			text.setText("Message from Server Sent : " + sMsg);
		}
		if (testCount == 5)
		{
			test.sendMsgFromClient("And hello to you!");
			text.setText("Message from Client Sent : ?");
		}
		if (testCount == 6)
		{
			String cMsg = test.getMsgToServer();
			text.setText("Message from Client Sent : " + cMsg);
		}
		if (testCount == 7)
		{
			test.close();
			text.setText("Socket Closed");
		}
		
		testCount++;
		
		return super.onKeyDown(keyCode, msg);
	}	
}
