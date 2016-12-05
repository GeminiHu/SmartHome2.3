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
	private boolean   isOnStart ;  // �����ж��Ƿ���½���
	private String conIP;	//���ܼҾӿ��ư��������IP��ַ
	private String conPort;	//���ܼҾӿ��ư��������˿ں�
	
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
		mainLLamp = (TextView)findViewById(R.id.tvMainLivingLamp);	//������TextView
		mainRLamp = (TextView)findViewById(R.id.tvMainRoomLamp);//�����TextView
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
	
	//�������Ƶ���Ӧ
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
		 * ��ʵ�ʿ�����LayoutInflater����໹�Ƿǳ����õģ�
		 * ��������������findViewById()��
		 * ��ͬ����LayoutInflater��������res/layout/�µ�xml�����ļ�������ʵ������
		 * ��findViewById()����xml�����ļ��µľ���widget�ؼ�(�� Button��TextView��)��
		 * �������ã�
		 * 1������һ��û�б����������Ҫ��̬����Ľ��棬����Ҫʹ��LayoutInflater.inflate()�����룻
		 * 2������һ���Ѿ�����Ľ��棬�Ϳ���ʹ��Activiyt.findViewById()������������еĽ���Ԫ�ء�
		 */
		LayoutInflater factory = LayoutInflater.from(MainPage.this);
		final View v1=factory.inflate(R.layout.child_config,null);
		AlertDialog.Builder dialog=new AlertDialog.Builder(MainPage.this);
		
		dialog.setTitle("������������");
		dialog.setView(v1);
		final EditText editTextIp = (EditText)v1.findViewById(R.id.connectionurl);
		final EditText editTextPort = (EditText)v1.findViewById(R.id.controlurl);
		editTextIp.setText(conIP);		//��ʼֵ
		editTextPort.setText(conPort);	//��ʼֵ
    	
        dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	conIP   = editTextIp.getText().toString();
            	conPort = editTextPort.getText().toString();
            	Toast.makeText(MainPage.this, "���óɹ���", Toast.LENGTH_SHORT).show(); 
            }
        });
        dialog.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
        dialog.show();
	}
	
	public void onIVExit(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
        builder.setMessage("ȷ���˳�?")
               .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                	   finish();
                   }
               })
               .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   
                   }
               }).show();		//finish();
	}
	
	/* �������ӷ������߳�, ���⿨������*/
	void connectToServer(){
		new Thread() {
            public void run() {
            	/*������̨service����, ������������*/
            	MainPage.this.startService(new Intent(MainPage.this, ServiceSocket.class)); 
            	
            	/*���ý��պ�̨�Ĺ㲥��Ϣ*/
        		//receiver = new BrdcstReceiver();  
                IntentFilter filter = new IntentFilter();  
                filter.addAction("android.intent.action.MY_RECEIVER");                   
                //registerReceiver(receiver, filter); //ע��  
            	
                /*���ӷ�����*/
            	int  port = Integer.parseInt(conPort);        	    
        		try {
        			Socket socket = new Socket(conIP, port);
        			/**
        			 * ��������,������,�����
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
        updateUI();  // ���½���
        Log.e("setting", "start onResume~~~");  
    }  
	
	@Override  
    protected void onStop() {
        isOnStart = false;
        Log.e("mainactivity ", "start onStop~~~");  
        super.onStop();          
    }  
	/*���½���ĸ���ͼ���״̬*/
	private void updateUI(){
		if(!isOnStart){
    		return;
    	}
		//�����Ž�״̬
		if(HomeConfig.RELAY_STATUE){
			mainDoorState.setText("״̬����");
		}else{
			mainDoorState.setText("״̬����");
		}
		//���¿յ�״̬
		/*if(HomeConfig.AIRCONDITIONING_STATUE){
			mainAirState.setText("״̬01����");
		}else{
			mainAirState.setText("״̬01����");
		}*/
		//���´���״̬
		if(HomeConfig.CURTAIN_STATUE){
			mainCurtainState.setText("״̬����");
		}else{
			mainCurtainState.setText("״̬����");
		}
		//���·����״̬
		if(HomeConfig.ROOMLIGHT_STATUE){
			mainRLamp.setText("������");
		}else{
			mainRLamp.setText("������");
		}
		//���¿�����״̬
		if(HomeConfig.CUSTOMERRIGHT_STATUE){
			mainLLamp.setText("������");
		}else{
			mainLLamp.setText("������");
		}
	}

	@Override protected void onDestroy() {  		 
		//unregisterReceiver(receiver); //  ע��
		this.stopService(new Intent(this, ServiceSocket.class));// ֹͣservice  
		Log.v("mainactivity ", "mainactivity onDestroy" );
		super.onDestroy(); 
    }
    
	/**************�㲥:���պ�̨��service���͵Ĺ㲥******************/
    /*private class BrdcstReceiver extends BroadcastReceiver {  		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
//            Bundle bundle = intent.getExtras();
            String stringValue=intent.getStringExtra("strRecvMsg");            
        	//Log.v("mainactivity", "onReceive" + stringValue);
            //   ���͵Ĳ�����(windowsƽ̨) , �����linuxƽ̨��ȥ��ǰ���ĸ���
        	//....#SERVERSDATA#12C#35%#
        	//....#SERVERSIGN#A1#B1#C0#D0#E1#F0#
        	//....#SERVERSIGN#A0#B0#C0#D0#E1#F0#
            //....#SERVERSIGN#A1#B1#C1#D1#E1#F0#
            //....#SERVERSDATA#42C#41%#
            //....#SERVERSIGN#A0#B0#C0#D0#E0#F0#
            //....#SERVERSIGN#B#ON#
        	if(stringValue.startsWith("#SERVERSDATA#")){// ��ʪ��
	    		String sub = stringValue.substring(stringValue.indexOf('#'), stringValue.lastIndexOf('#') + 1);
				String[] strs = sub.split("#");
    			mainAirTemper.setText("�£�" + strs[2]);
    			mainAirHum.setText("ʪ��" + strs[3]);
        	}else{        		
        		updateUI();        		     		
        	}
        	
        }
    }  */
}
