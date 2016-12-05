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
	
	//初始化各种变量
	public void initVars(){
		mode1 = (Button)findViewById(R.id.btnMode1_02);
		mode2 = (Button)findViewById(R.id.btnMode2_02);
		delay02 = (EditText)findViewById(R.id.delay02);
		
		//更新Mode1状态
		if(HomeConfig.ROOMLIGHT_STATUE){
			mode1.setBackgroundResource(R.drawable.mode1_on);
		}else{
			mode1.setBackgroundResource(R.drawable.mode1_off);
		}
		//更新Mode2状态
		if(HomeConfig.CUSTOMERRIGHT_STATUE){
			mode2.setBackgroundResource(R.drawable.mode2_on);
		}else{
			mode2.setBackgroundResource(R.drawable.mode2_off);
		}
	}
	
	public void onBtnHome(View v){
		finish();
	}

	
	//mode1控制按钮
	public void onBtnMode1_02(View v) throws InterruptedException{
		String timer = delay02.getText().toString();
		Thread.sleep(Integer.parseInt(timer)*1000);
		Thread.sleep(3000);
		mp.start();
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildMode02.this).setTitle("网络连接").setMessage("未连接上服务器")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   

                }  
            }).show(); 
			return;
		}
		if (HomeConfig.ROOMLIGHT_STATUE) {
				out.println("#CTRL#B#OFF#");//假设只要发送命令，就认为已经控制成功
				HomeConfig.ROOMLIGHT_STATUE = false;
				mode1.setBackgroundResource(R.drawable.mode1_off);
		} else {
				System.out.println(socket);
				out.println("#CTRL#B#ON#");
				HomeConfig.ROOMLIGHT_STATUE = true;
				mode1.setBackgroundResource(R.drawable.mode1_on);
		}
	}
	
	//mode2控制按钮
	public void onBtnMode2_02(View v){
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildMode02.this).setTitle("网络连接").setMessage("未连接上服务器")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   
                }  
            }).show(); 
			return;
		}
		if (HomeConfig.RELAY_STATUE02) {
				out.println("#CTRL#K#ON#");//假设只要发送命令，就认为已经控制成功
				HomeConfig.RELAY_STATUE02 = true;//关闭02防盗系统，打开02卧室门
				mode2.setBackgroundResource(R.drawable.mode2_off);
		} else {
				System.out.println(socket);
				out.println("#CTRL#K#OFF#");
				HomeConfig.RELAY_STATUE02 = false;//开启02防盗系统，关闭02卧室门
				mode2.setBackgroundResource(R.drawable.mode2_on);
				//判断01的防盗系统是否开启，如果开启，关闭大门
				if (!HomeConfig.RELAY_STATUE01){
					out.println("#CTRL#C#OFF#");
					HomeConfig.RELAY_STATUE = false;//开启整体防盗系统，关闭客厅门
				}
				}
		}
	}