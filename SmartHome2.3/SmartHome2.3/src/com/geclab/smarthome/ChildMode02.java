package com.geclab.smarthome;

import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChildMode02 extends Activity{
	
	Button mode1,mode2;
	EditText delay02;
    MediaPlayer mp;
	Socket socket = NetworkUtil.socket;
	private PrintWriter out = NetworkUtil.out;
	Util util = new Util();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_mode02);
		mp = MediaPlayer.create(this,R.raw.getup);
		initVars();
	}
	
	//��ʼ�����ֱ���
	public void initVars(){
		mode1 = (Button)findViewById(R.id.btnMode1_02);
		mode2 = (Button)findViewById(R.id.btnMode2_02);
		delay02 = (EditText)findViewById(R.id.delay02);
		
		//����Mode1״̬
		if(HomeConfig.ROOMLIGHT_STATUE){
			mode1.setBackgroundResource(R.drawable.mode1_on);
		}else{
			mode1.setBackgroundResource(R.drawable.mode1_off);
		}
		//����Mode2״̬
		if(HomeConfig.CUSTOMERRIGHT_STATUE){
			mode2.setBackgroundResource(R.drawable.mode2_on);
		}else{
			mode2.setBackgroundResource(R.drawable.mode2_off);
		}
	}
	
	public void onBtnHome(View v){
		finish();
	}

	
	//mode1���ư�ť
	public void onBtnMode1_02(View v) throws InterruptedException{
		String timer = delay02.getText().toString();
		Thread.sleep(Integer.parseInt(timer)*1000);
		Thread.sleep(3000);
		mp.start();
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildMode02.this).setTitle("��������").setMessage("δ�����Ϸ�����")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   

                }  
            }).show(); 
			return;
		}
		if (HomeConfig.ROOMLIGHT_STATUE) {
				out.println("#CTRL#B#OFF#");//����ֻҪ�����������Ϊ�Ѿ����Ƴɹ�
				HomeConfig.ROOMLIGHT_STATUE = false;
				mode1.setBackgroundResource(R.drawable.mode1_off);
		} else {
				System.out.println(socket);
				out.println("#CTRL#B#ON#");
				HomeConfig.ROOMLIGHT_STATUE = true;
				mode1.setBackgroundResource(R.drawable.mode1_on);
		}
	}
	
	//mode2���ư�ť
	public void onBtnMode2_02(View v){
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildMode02.this).setTitle("��������").setMessage("δ�����Ϸ�����")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   
                }  
            }).show(); 
			return;
		}
		if (HomeConfig.RELAY_STATUE02) {
				out.println("#CTRL#K#ON#");//����ֻҪ�����������Ϊ�Ѿ����Ƴɹ�
				HomeConfig.RELAY_STATUE02 = true;//�ر�02����ϵͳ����02������
				mode2.setBackgroundResource(R.drawable.mode2_off);
		} else {
				System.out.println(socket);
				out.println("#CTRL#K#OFF#");
				HomeConfig.RELAY_STATUE02 = false;//����02����ϵͳ���ر�02������
				mode2.setBackgroundResource(R.drawable.mode2_on);
				//�ж�01�ķ���ϵͳ�Ƿ���������������رմ���
				if (!HomeConfig.RELAY_STATUE01){
					out.println("#CTRL#C#OFF#");
					HomeConfig.RELAY_STATUE = false;//�����������ϵͳ���رտ�����
				}
				}
		}
	}