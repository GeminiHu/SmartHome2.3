package com.geclab.smarthome;

import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChildLamp02 extends Activity{
	
	Button roomLamp, livingLamp;

	Socket socket = NetworkUtil.socket;
	private PrintWriter out = NetworkUtil.out;
	Util util = new Util();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_lamp02);
		
		initVars();
	}
	
	//初始化各种变量
	public void initVars(){
		roomLamp = (Button)findViewById(R.id.btnRoomLamp);
		livingLamp = (Button)findViewById(R.id.btnLivingLamp);
		
		//更新房间灯状态
		if(HomeConfig.ROOMLIGHT_STATUE){
			roomLamp.setBackgroundResource(R.drawable.room_lamp_on);
		}else{
			roomLamp.setBackgroundResource(R.drawable.room_lamp_off);
		}
		//更新客厅灯状态
		if(HomeConfig.CUSTOMERRIGHT_STATUE){
			livingLamp.setBackgroundResource(R.drawable.living_lamp_on);
		}else{
			livingLamp.setBackgroundResource(R.drawable.living_lamp_off);
		}
	}
	
	public void onBtnHome(View v){
		finish();
	}
	
	//房间灯控制按钮
	public void onBtnRoomLamp(View v){
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildLamp02.this).setTitle("网络连接").setMessage("未连接上服务器")  
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
				roomLamp.setBackgroundResource(R.drawable.room_lamp_off);
		} else {
				System.out.println(socket);
				out.println("#CTRL#B#ON#");
				HomeConfig.ROOMLIGHT_STATUE = true;
				roomLamp.setBackgroundResource(R.drawable.room_lamp_on);
		}
	}
	
	//客厅灯控制按钮
	public void onBtnLivingLamp(View v){
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildLamp02.this).setTitle("网络连接").setMessage("未连接上服务器")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   

                }  
            }).show(); 
			return;
		}
		if (HomeConfig.CUSTOMERRIGHT_STATUE) {
				out.println("!");//假设只要发送命令，就认为已经控制成功
				HomeConfig.CUSTOMERRIGHT_STATUE = false;
				livingLamp.setBackgroundResource(R.drawable.living_lamp_off);
		} else {
				System.out.println(socket);
				out.println(" ");
				HomeConfig.CUSTOMERRIGHT_STATUE = true;
				livingLamp.setBackgroundResource(R.drawable.living_lamp_on);
		}
	}
}
