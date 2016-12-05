package com.geclab.smarthome;

import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChildMode extends Activity{
	
	Button mode1,mode2;
	EditText delay;
    MediaPlayer mp;
	Socket socket = NetworkUtil.socket;
	private PrintWriter out = NetworkUtil.out;
	Util util = new Util();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_mode);
		mp = MediaPlayer.create(this,R.raw.getup);
		initVars();
	}
	
	//��ʼ�����ֱ���
	public void initVars(){
		mode1 = (Button)findViewById(R.id.btnMode1);
		mode2 = (Button)findViewById(R.id.btnMode2);
		delay = (EditText)findViewById(R.id.delay);
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
	public void onBtnMode1(View v) throws InterruptedException{
		String timer = delay.getText().toString();
		Thread.sleep(Integer.parseInt(timer)*1000);
		mp.start();
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildMode.this).setTitle("��������").setMessage("δ�����Ϸ�����")  
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
	public void onBtnMode2(View v){
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildMode.this).setTitle("��������").setMessage("δ�����Ϸ�����")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   
                }  
            }).show(); 
			return;
		}
		if (HomeConfig.RELAY_STATUE01) {
				out.println("#CTRL#J#ON#");//����ֻҪ�����������Ϊ�Ѿ����Ƴɹ�
				HomeConfig.RELAY_STATUE01 = true;//�ر�01����ϵͳ����01������
				mode2.setBackgroundResource(R.drawable.mode2_off);
		} else {
				System.out.println(socket);
				out.println("#CTRL#J#OFF#");
				HomeConfig.RELAY_STATUE01 = false;//����01����ϵͳ���ر�01������
				mode2.setBackgroundResource(R.drawable.mode2_on);
				//�ж�02�ķ���ϵͳ�Ƿ���������������رմ���
				if (!HomeConfig.RELAY_STATUE02){
					out.println("#CTRL#C#OFF#");
					HomeConfig.RELAY_STATUE = false;//�����������ϵͳ���رտ�����
				}
				}
		}
	}
