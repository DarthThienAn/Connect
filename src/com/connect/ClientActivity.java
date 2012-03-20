//package com.connect;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//public class ClientActivity extends Activity {
////		   private Button bt;
////		   private TextView tv;
////		   private Socket socket;
////		   private String serverIpAddress = "10.0.2.2";
////		   // AND THAT'S MY DEV'T MACHINE WHERE PACKETS TO
////		   // PORT 5000 GET REDIRECTED TO THE SERVER EMULATOR'S
////		   // PORT 6000
////		   private static final int REDIRECTED_SERVERPORT = 5000;
////
////		   @Override
////		   public void onCreate(Bundle savedInstanceState) {
////		      super.onCreate(savedInstanceState);
////		      setContentView(R.layout.client);
////		      bt = (Button) findViewById(R.id.connect_phones);
////		      tv = (TextView) findViewById(R.id.client_text);
////
////		      try {
////		         InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
////		         socket = new Socket(serverAddr, REDIRECTED_SERVERPORT);
////		      } catch (UnknownHostException e1) {
////		         e1.printStackTrace();
////		      } catch (IOException e1) {
////		         e1.printStackTrace();
////		      }
////
////		      bt.setOnClickListener(new OnClickListener() {
////
////		         public void onClick(View v) {
////		            try {
////		               EditText et = (EditText) findViewById(R.id.server_ip);
////		               String str = et.getText().toString();
////		               PrintWriter out = new PrintWriter(new BufferedWriter(
////		                     new OutputStreamWriter(socket.getOutputStream())),
////		                     true);
////		               out.println(str);
////		               Log.d("Client", "Client sent message");
////
////		            } catch (UnknownHostException e) {
////		               tv.setText("Error1");
////		               e.printStackTrace();
////		            } catch (IOException e) {
////		               tv.setText("Error2");
////		               e.printStackTrace();
////		            } catch (Exception e) {
////		               tv.setText("Error3");
////		               e.printStackTrace();
////		            }
////		         }
////		      });
////		   }
////		}
//    private TextView clientStatus;
//    private EditText serverIp;
//    private EditText mClientMsg;
//    private Button connectPhones;
//    private String serverIpAddress = "";
//    private boolean connected = false;
////    private Handler handler = new Handler();
//    String fromServer = "";
//    protected static final int MSG_ID = 0x1337;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.client);
//
//        clientStatus = (TextView) findViewById(R.id.client_text);
//        serverIp = (EditText) findViewById(R.id.server_ip);
//        mClientMsg = (EditText) findViewById(R.id.client_msg);
//        connectPhones = (Button) findViewById(R.id.connect_phones);
//        connectPhones.setOnClickListener(connectListener);
//        clientStatus.setText("Waiting to connect");
//    }
//
//    private OnClickListener connectListener = new OnClickListener() {
//
////        @Override
//        public void onClick(View v) {
//            if (!connected) {
//                serverIpAddress = serverIp.getText().toString();
//                if (!serverIpAddress.equals("")) {
//                    Thread cThread = new Thread(new ClientThread());
//                    cThread.start();
//                }
//            }
//        }
//    };
//
//    public class ClientThread implements Runnable {
//        public void run() {
//            try {
//                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
//                //connecting
//                Socket s = new Socket(serverAddr, ServerActivity.SERVERPORT);
//                connected = true;
//                
//                //connected
//                while (connected) 
//                {
//                    Message m = new Message();
//                    m.what = MSG_ID;
//                	try 
//                	{
//                		//send a message
//                        String sendMsg = mClientMsg.getText().toString();
//                        PrintWriter out = new PrintWriter(new BufferedWriter(
//                        		new OutputStreamWriter(s.getOutputStream())), true);
//                        //output the message
//                        out.println(sendMsg);
//
//                		//listen for message and update
//                		BufferedReader input = new BufferedReader(
//                				new InputStreamReader(s.getInputStream()));
//                		String st = null;
//                		st = input.readLine();
//                		fromServer = st;
//
//                      Log.d("ClientActivity", fromServer);
//                		
//                		if(fromServer == "hi")
//                			fromServer = "bye";
//
//                		//run tetris
//                		if(fromServer == "playtetris")
//                		{
//                            setContentView(R.layout.tetris_layout);
//
//                        	TetrisView mTetrisView = (TetrisView) findViewById(R.id.tetris);
//                            mTetrisView.setTextView((TextView) findViewById(R.id.text));
//                            
//                            mTetrisView.setMode(TetrisView.READY);
//                		}
//                		myUpdateHandler.sendMessage(m);
//                          
//                        
//                        //flush
//                            
//                            
//                        //Sent
//                    } catch (Exception e) {
//                        Log.e("ClientActivity", "S: Error", e);
//                    }
//                }
//                s.close();
////                Log.d("ClientActivity", "C: Closed.");
//            } catch (Exception e) {
////                Log.e("ClientActivity", "C: Error", e);
//                connected = false;
//            }
//        }
//    }
//    
//    Handler myUpdateHandler = new Handler() {
//        public void handleMessage(Message msg) {
//           switch (msg.what) {
//           case MSG_ID:
//              TextView tv = (TextView) findViewById(R.id.client_text);
//              tv.setText(fromServer);
//              break;
//           default:
//              break;
//           }
//           super.handleMessage(msg);
//        }
//     };
//}