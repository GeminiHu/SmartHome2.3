package com.geclab.smarthome;

import java.io.PrintWriter;
import java.net.Socket;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChildDoor extends Activity{
	
	private TextView doorState;
	Socket socket = NetworkUtil.socket;
	Util util = new Util();
	PrintWriter out = NetworkUtil.out;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_door);
		
		initVars();
	}
	
	public void initVars(){
		doorState = (TextView)findViewById(R.id.tvDoorSate);
		
		if(HomeConfig.RELAY_STATUE){
			doorState.setText("开");
		}else{
			doorState.setText("关");
		}
	}

	public void onBtnHome(View v){
		finish();
	}
	
	/*门禁控制按钮的单击事件响应*/
	public void onBtnSetDoorState(View v){

		/*判断网络是否连接上了,没有则提示,并返回*/
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildDoor.this).setTitle("网络连接").setMessage("未连接上服务器")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   

                }  
            }).show(); //警告对话框
			return;
		}
		
		/*门禁控制的实现*/
		if (HomeConfig.RELAY_STATUE) {
				out.println("$");
				HomeConfig.RELAY_STATUE = false;
				doorState.setText("关");
		} else {
				System.out.println(socket);

				out.println("#");
				HomeConfig.RELAY_STATUE = true;
				doorState.setText("开");
		}
	}
}
