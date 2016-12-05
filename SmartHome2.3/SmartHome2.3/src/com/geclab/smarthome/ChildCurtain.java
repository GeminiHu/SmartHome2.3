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
			curtainState.setText("��");
		}else{
			curtainState.setText("��");
		}
	}

	public void onBtnHome(View v){
		finish();
	}
	
	//�������ư�ť������Ӧ
	public void onBtnSetCurtain(View v){
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildCurtain.this).setTitle("��������").setMessage("δ�����Ϸ�����")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   

                }  
            }).show(); 
			return;
		}
		Log.v("curtain_setting", "setOnCheckedChangeListener" );
		// isChecked���ǰ�ť״̬
		if (HomeConfig.CURTAIN_STATUE) {
			out.println("Q");	//���ƴ�����
			HomeConfig.CURTAIN_STATUE = false;
			curtainState.setText("��");
			Toast.makeText(ChildCurtain.this, "�����ѹر�",
					Toast.LENGTH_SHORT).show();
		} else {
			out.println("P");		//���ƴ�����
			curtainState.setText("��");
			HomeConfig.CURTAIN_STATUE = true;
			Toast.makeText(ChildCurtain.this, "�����Ѵ�",
					Toast.LENGTH_SHORT).show();
		}
	}
}
