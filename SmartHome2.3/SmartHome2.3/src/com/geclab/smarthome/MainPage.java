package com.geclab.smarthome;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends Activity{

	//private BrdcstReceiver receiver; 
	private boolean   isOnStart ;  // 用来判断是否更新界面
	private String conIP;	//智能家居控制板网络服务IP地址
	private String conPort;	//智能家居控制板网络服务端口号
	
	private TextView mainDoorState,
	                mainAirState,
					mainAirTemper, 
					mainAirHum,
					mainRan,
					mainYan,
					mainCurtainState,
					mainLLamp,
					mainRLamp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initVariables();
		
		connectToServer();
	}
	
	public void initVariables(){
		conIP = "192.168.10.1";//"192.168.1.105";//"192.168.100.103" ;//
		conPort = "8002";//"192.168.1.105";//
		
		mainDoorState = (TextView)findViewById(R.id.tvMainDoor);
		mainAirState = (TextView)findViewById(R.id.tvAirState);
		mainAirTemper = (TextView)findViewById(R.id.tvMainAirTemperature);
		mainAirHum = (TextView)findViewById(R.id.tvMainAirHumidity);
		mainRan = (TextView)findViewById(R.id.tvMainRan);
		mainYan = (TextView)findViewById(R.id.tvMainYan);
		mainCurtainState = (TextView)findViewById(R.id.tvMainCurtain);
		mainLLamp = (TextView)findViewById(R.id.tvMainLivingLamp);	//客厅灯TextView
		mainRLamp = (TextView)findViewById(R.id.tvMainRoomLamp);//房间灯TextView
	}

	public void openChild(String str){
		try {
			Class ourClass = Class.forName("com.geclab.smarthome."+str);
			Intent ourIntent = new Intent(MainPage.this, ourClass);
			startActivity(ourIntent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onIVDoor(View v){
		openChild("ChildDoor");
	}
	
	public void onIVAir(View v){
		openChild("ChildAir");
	}
	
	//窗帘控制的响应
	public void onIVCurtain(View v){
		openChild("ChildCurtain");
	}
	
	public void onIVLamp(View v){
		openChild("ChildLamp");
	}
	
	 public void onIVMode(View v){
			openChild("ChildMode");
		}
	
	public void onIVConfig(View v){

		/**
		 * 在实际开发中LayoutInflater这个类还是非常有用的，
		 * 它的作用类似于findViewById()。
		 * 不同点是LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化；
		 * 而findViewById()是找xml布局文件下的具体widget控件(如 Button、TextView等)。
		 * 具体作用：
		 * 1、对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
		 * 2、对于一个已经载入的界面，就可以使用Activiyt.findViewById()方法来获得其中的界面元素。
		 */
		LayoutInflater factory = LayoutInflater.from(MainPage.this);
		final View v1=factory.inflate(R.layout.child_config,null);
		AlertDialog.Builder dialog=new AlertDialog.Builder(MainPage.this);
		
		dialog.setTitle("网络连接属性");
		dialog.setView(v1);
		final EditText editTextIp = (EditText)v1.findViewById(R.id.connectionurl);
		final EditText editTextPort = (EditText)v1.findViewById(R.id.controlurl);
		editTextIp.setText(conIP);		//初始值
		editTextPort.setText(conPort);	//初始值
    	
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	conIP   = editTextIp.getText().toString();
            	conPort = editTextPort.getText().toString();
            	Toast.makeText(MainPage.this, "设置成功！", Toast.LENGTH_SHORT).show(); 
            }
        });
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
        dialog.show();
	}
	
	public void onIVExit(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
        builder.setMessage("确认退出?")
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                	   finish();
                   }
               })
               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   
                   }
               }).show();		//finish();
	}
	
	/* 开启连接服务器线程, 避免卡死界面*/
	void connectToServer(){
		new Thread() {
            public void run() {
            	/*启动后台service服务, 接受网络数据*/
            	MainPage.this.startService(new Intent(MainPage.this, ServiceSocket.class)); 
            	
            	/*设置接收后台的广播信息*/
        		//receiver = new BrdcstReceiver();  
                IntentFilter filter = new IntentFilter();  
                filter.addAction("android.intent.action.MY_RECEIVER");                   
                //registerReceiver(receiver, filter); //注册  
            	
                /*连接服务器*/
            	int  port = Integer.parseInt(conPort);        	    
        		try {
        			Socket socket = new Socket(conIP, port);
        			/**
        			 * 设置网络,输入流,输出流
        			 */
        			NetworkUtil.socket = socket;
        			NetworkUtil.out = new PrintWriter(
        					new BufferedWriter(
        							new OutputStreamWriter(socket.getOutputStream(),"UTF-8")), true);
        			NetworkUtil.br = new BufferedReader(
        			new InputStreamReader(socket.getInputStream(), "UTF-8"));
        		} catch (UnsupportedEncodingException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		                
            }
		}.start();
	}
	@Override  
    protected void onResume() {  
        super.onResume();  
        isOnStart = true;
        updateUI();  // 更新界面
        Log.e("setting", "start onResume~~~");  
    }  
	
	@Override  
    protected void onStop() {
        isOnStart = false;
        Log.e("mainactivity ", "start onStop~~~");  
        super.onStop();          
    }  
	/*更新界面的各个图标的状态*/
	private void updateUI(){
		if(!isOnStart){
    		return;
    	}
		//更新门禁状态
		if(HomeConfig.RELAY_STATUE){
			mainDoorState.setText("状态：开");
		}else{
			mainDoorState.setText("状态：关");
		}
		//更新空调状态
		/*if(HomeConfig.AIRCONDITIONING_STATUE){
			mainAirState.setText("状态01：开");
		}else{
			mainAirState.setText("状态01：关");
		}*/
		//更新窗帘状态
		if(HomeConfig.CURTAIN_STATUE){
			mainCurtainState.setText("状态：开");
		}else{
			mainCurtainState.setText("状态：关");
		}
		//更新房间灯状态
		if(HomeConfig.ROOMLIGHT_STATUE){
			mainRLamp.setText("房：亮");
		}else{
			mainRLamp.setText("房：灭");
		}
		//更新客厅灯状态
		if(HomeConfig.CUSTOMERRIGHT_STATUE){
			mainLLamp.setText("厅：亮");
		}else{
			mainLLamp.setText("厅：灭");
		}
	}

	@Override protected void onDestroy() {  		 
		//unregisterReceiver(receiver); //  注销
		this.stopService(new Intent(this, ServiceSocket.class));// 停止service  
		Log.v("mainactivity ", "mainactivity onDestroy" );
		super.onDestroy(); 
    }
    
	/**************广播:接收后台的service发送的广播******************/
    /*private class BrdcstReceiver extends BroadcastReceiver {  		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
//            Bundle bundle = intent.getExtras();
            String stringValue=intent.getStringExtra("strRecvMsg");            
        	//Log.v("mainactivity", "onReceive" + stringValue);
            //   发送的测试码(windows平台) , 如果是linux平台请去掉前面四个点
        	//....#SERVERSDATA#12C#35%#
        	//....#SERVERSIGN#A1#B1#C0#D0#E1#F0#
        	//....#SERVERSIGN#A0#B0#C0#D0#E1#F0#
            //....#SERVERSIGN#A1#B1#C1#D1#E1#F0#
            //....#SERVERSDATA#42C#41%#
            //....#SERVERSIGN#A0#B0#C0#D0#E0#F0#
            //....#SERVERSIGN#B#ON#
        	if(stringValue.startsWith("#SERVERSDATA#")){// 温湿度
	    		String sub = stringValue.substring(stringValue.indexOf('#'), stringValue.lastIndexOf('#') + 1);
				String[] strs = sub.split("#");
    			mainAirTemper.setText("温：" + strs[2]);
    			mainAirHum.setText("湿：" + strs[3]);
        	}else{        		
        		updateUI();        		     		
        	}
        	
        }
    }  */
}
