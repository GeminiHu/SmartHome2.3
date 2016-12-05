package com.geclab.smarthome;

import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChildCurtain extends Activity{
	
	TextView curtainState;
	
	PrintWriter out = NetworkUtil.out;
	Socket socket = NetworkUtil.socket;
	Util util = new Util();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_curtain);
		initVars();
	}
	
	public void initVars(){
		curtainState = (TextView)findViewById(R.id.tvCurtainSate);
		
		if(HomeConfig.CURTAIN_STATUE){
			curtainState.setText("开");
		}else{
			curtainState.setText("关");
		}
	}

	public void onBtnHome(View v){
		finish();
	}
	
	//窗帘控制按钮单击响应
	public void onBtnSetCurtain(View v){
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildCurtain.this).setTitle("网络连接").setMessage("未连接上服务器")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   

                }  
            }).show(); 
			return;
		}
		Log.v("curtain_setting", "setOnCheckedChangeListener" );
		// isChecked就是按钮状态
		if (HomeConfig.CURTAIN_STATUE) {
			out.println("Q");	//控制窗帘关
			HomeConfig.CURTAIN_STATUE = false;
			curtainState.setText("关");
			Toast.makeText(ChildCurtain.this, "窗帘已关闭",
					Toast.LENGTH_SHORT).show();
		} else {
			out.println("P");		//控制窗帘开
			curtainState.setText("开");
			HomeConfig.CURTAIN_STATUE = true;
			Toast.makeText(ChildCurtain.this, "窗帘已打开",
					Toast.LENGTH_SHORT).show();
		}
	}
}
