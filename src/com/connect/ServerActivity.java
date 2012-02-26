package com.connect;
//package com.tetris;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import java.util.Enumeration;
//
//import com.tetris.ClientActivity.ClientThread;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//
//public class ServerActivity extends Activity
//{
////    private TextView serverStatus;
//   	String ICICLE_KEY = "Tetris-view";
//
//    // DEFAULT IP
//    public static String SERVERIP = "";
//    // DESIGNATE A PORT
//    public static final int SERVERPORT = 8080;
//    
//    String mClientMsg = "";
//    private TextView serverStatus;
//    private EditText mServerMsg;
////    private Handler handler = new Handler();
//    private ServerSocket serverSocket;
////    private Bundle savedInstanceState;
//    private Button playTetris;
//    PrintWriter out;
//    protected static final int MSG_ID = 0x1337;
//    private boolean clicked = false;
//    
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.server);
//        
//        serverStatus = (TextView) findViewById(R.id.server_status);
//        mServerMsg = (EditText) findViewById(R.id.server_msg);
//        SERVERIP = getLocalIpAddress();
//        serverStatus.setText("Waiting for connection at: " + SERVERIP);
////        this.savedInstanceState = savedInstanceState;
//        playTetris = (Button) findViewById(R.id.play_tetris);
//        playTetris.setOnClickListener(connectListener);
//
//        
//        
////        Thread serverThread = new Thread(new ServerThread());
////        serverThread.start();
//        
////        Socket s = null;
////        try {
////        	serverSocket = new ServerSocket(SERVERPORT);
////        } catch (IOException e) {
////        	e.printStackTrace();
////        }
////        while (!clicked)
////        {
////        	Message m = new Message();
////        	m.what = MSG_ID;
////        	try {
////        		if (s == null)
////        			s = serverSocket.accept();
////        		
////        		//listen for message and update
////        		BufferedReader input = new BufferedReader(
////        				new InputStreamReader(s.getInputStream()));
////        		String st = null;
////        		st = input.readLine();
////        		mClientMsg = st;
////        		myUpdateHandler.sendMessage(m);
////
////        		//send a message
////        		String sendMsg = mServerMsg.getText().toString();
////        		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s
////        				.getOutputStream())), true);
////        		//output the message
////        		out.println(sendMsg);
////
////        		//Begin
////        		//End
////        	} catch (IOException e) 
////        	{
////        		e.printStackTrace();
////        	}    
////        }
//    }
//    
//    class ServerThread implements Runnable {
//        public void run() {
//           Socket s = null;
//           try {
//              serverSocket = new ServerSocket(SERVERPORT);
//           } catch (IOException e) {
//              e.printStackTrace();
//           }
//           while (!Thread.currentThread().isInterrupted()) {
//              Message m = new Message();
//              m.what = MSG_ID;
//              try {
//                 if (s == null)
//                    s = serverSocket.accept();
//                 
//                 //listen for message and update
//                 BufferedReader input = new BufferedReader(
//                       new InputStreamReader(s.getInputStream()));
//                 String st = null;
//                 st = input.readLine();
//                 mClientMsg = st;
//                 myUpdateHandler.sendMessage(m);
//                                  
//                 //send a message
//                 String sendMsg = mServerMsg.getText().toString();
//                 out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s
//                             .getOutputStream())), true);
//                 //output the message
//                 out.println(sendMsg);
//
//                 //Begin
//
//                 
//                 
//                 
//                 
//                 //End
//              } catch (IOException e) {
//                 e.printStackTrace();
//              }
//           }
//        }
//     }
//
//    
//    
//    private OnClickListener connectListener = new OnClickListener() {
//
////      @Override
//        public void onClick(View v) {
//        	clicked = true;
//
////        	out.println("playtetris");
//        	
////          requestWindowFeature(Window.FEATURE_NO_TITLE);
////            setContentView(R.layout.tetris_layout);
////
////        	TetrisView mTetrisView = (TetrisView) findViewById(R.id.tetris);
////            mTetrisView.setTextView((TextView) findViewById(R.id.text));
////            
////            mTetrisView.setMode(TetrisView.READY);
//        }
//    };
//    
//    class TetrisThread extends Thread {
//        public void run() {
//        	
//        	out.println("playtetris");
//        	
//            setContentView(R.layout.tetris_layout);
//
//        	TetrisView mTetrisView = (TetrisView) findViewById(R.id.tetris);
//            mTetrisView.setTextView((TextView) findViewById(R.id.text));
//            
//            mTetrisView.setMode(TetrisView.READY);
//        }
//     }    
//    
////    public class ServerThread implements Runnable
////    {
//////    	@Override
//////    	public void run()
//////    	{
//////    		
//////    		setContentView(R.layout.tetris_layout);
//////    		
//////    		serverStatus = (TetrisView) findViewById(R.id.tetris);
//////    		serverStatus.setTextView((TextView) findViewById(R.id.text));
//////    		
//////	           		if (savedInstanceState == null)
//////	           		{
//////	                   // We were just launched -- set up a new game
//////	            	   serverStatus.setMode(TetrisView.READY);
//////	           		} 
//////	           		else 
//////	           		{
//////	           			// We are being restored
//////	           			Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
//////	           			if (map != null) {
//////	           				serverStatus.restoreState(map);
//////	           			} else {
//////	           				serverStatus.setMode(TetrisView.PAUSE);
//////	           			}
//////	               }    	   
//////               }
//////    	}
////    
////    	@Override
////        public void run()
////        {
////        	Socket s = null;
////            try {
////                if (SERVERIP != null)
////                {
////                    handler.post(new Runnable()
////                    {
////                        @Override
////                        public void run()
////                        {
////                            serverStatus.setText("Listening on IP: " + SERVERIP);
////                        }
////                    });
////                    serverSocket = new ServerSocket(SERVERPORT);
////                    while (true)
////                    {
////                        // LISTEN FOR INCOMING CLIENTS
////                        Socket client = serverSocket.accept();
////                        handler.post(new Runnable()
////                        {
////                            @Override
////                            public void run()
////                            {
////                                serverStatus.setText("Connected.");
////                            }
////                        });
//// 
////                        try {
////                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
////                            String line = null;
////                            while ((line = in.readLine()) != null)
////                            {
////                                Log.d("ServerActivity", line);
////                                handler.post(new Runnable()
////                                {
////                                    @Override
////                                    public void run()
////                                    {
//////                                    	TetrisView mTetrisView;
//////                                    	String ICICLE_KEY = "Tetris-view";
//////                                    	
//////                                    	setContentView(R.layout.tetris_layout);
//////                                    	
//////                                        mTetrisView = (TetrisView) findViewById(R.id.tetris);
//////                                        mTetrisView.setTextView((TextView) findViewById(R.id.text));
//////                                        
//////                                        if (savedInstanceState == null)
//////                                        {
//////                                            // We were just launched -- set up a new game
//////                                            mTetrisView.setMode(TetrisView.READY);
//////                                        } 
//////                                        else 
//////                                        {
//////                                            // We are being restored
//////                                            Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
//////                                            if (map != null) {
//////                                                mTetrisView.restoreState(map);
//////                                            } else {
//////                                                mTetrisView.setMode(TetrisView.PAUSE);
//////                                            }
//////                                        }
////                                    	
////
//////                                      public void run() {
//////                                         try {
//////                                            serverSocket = new ServerSocket(SERVERPORT);
//////                                         } catch (IOException e) {
//////                                            e.printStackTrace();
//////                                         }
//////                                         while (!Thread.currentThread().isInterrupted()) {
//////                                            Message m = new Message();
//////                                            m.what = MSG_ID;
//////                                            try {
//////                                               if (s == null)
//////                                                  s = serverSocket.accept();
//////                                               BufferedReader input = new BufferedReader(
//////                                                     new InputStreamReader(s.getInputStream()));
//////                                               String st = null;
//////                                               st = input.readLine();
//////                                               mClientMsg = st;
//////                                               myUpdateHandler.sendMessage(m);
//////                                            } catch (IOException e) {
//////                                               e.printStackTrace();
//////                                            }
//////                                         }
//////                                      }
//////                                   }                                        			
////
////                                    	
////                                    	try {
////                                          if (s == null)
////                                             s = serverSocket.accept();
////                                          BufferedReader input = new BufferedReader(
////                                                new InputStreamReader(s.getInputStream()));
////                                          String st = input.readLine();
////                                          mClientMsg = st;
////                                          serverStatus.setText(mClientMsg);
////                                          
//////                                          myUpdateHandler.sendMessage(m);
////                                       } catch (IOException e) {
////                                          e.printStackTrace();
////                                       }
////                                    	
//////                                    	Message m = new Message();
//////                                        Socket s = null;
//////                                    	
//////                                    	m.what = MSG_ID;
//////                                               try {
//////                                                  if (s == null)
//////                                                     s = serverSocket.accept();
//////                                                  BufferedReader input = new BufferedReader(
//////                                                        new InputStreamReader(s.getInputStream()));
//////                                                  String st = input.readLine();
//////                                                  mClientMsg = st;
//////                                                  serverStatus.setText(mClientMsg);
//////                                                  
//////                                                  myUpdateHandler.sendMessage(m);
//////                                               } catch (IOException e) {
//////                                                  e.printStackTrace();
//////                                               }
////                                         }
////                                });
////                            }
////                            break;
////                        } catch (Exception e) {
////                            handler.post(new Runnable() {
////                                @Override
////                                public void run() {
////                                    serverStatus.setText("Oops. Connection interrupted. Please reconnect your phones.");
////                                }
////                            });
////                            e.printStackTrace();
////                        }
////                    }
////                } else {
////                    handler.post(new Runnable() {
////                        @Override
////                        public void run() {
////                            serverStatus.setText("Couldn't detect internet connection.");
////                        }
////                    });
////                }
////            } catch (Exception e) {
////                handler.post(new Runnable() {
////                    @Override
////                    public void run() {
////                        serverStatus.setText("Error");
////                    }
////                });
////                e.printStackTrace();
////            }
////        }
////    }
// 
//    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
//    private String getLocalIpAddress() {
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
//                }
//            }
//        } catch (SocketException ex) {
//            Log.e("ServerActivity", ex.toString());
//        }
//        return null;
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        try {
//             // MAKE SURE YOU CLOSE THE SOCKET UPON EXITING
//             serverSocket.close();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//    }
//    
//    Handler myUpdateHandler = new Handler() {
//        public void handleMessage(Message msg) {
//           switch (msg.what) {
//           case MSG_ID:
//              TextView tv = (TextView) findViewById(R.id.server_status);
//              tv.setText(mClientMsg);
//              break;
//           default:
//              break;
//           }
//           super.handleMessage(msg);
//        }
//     };
//}
