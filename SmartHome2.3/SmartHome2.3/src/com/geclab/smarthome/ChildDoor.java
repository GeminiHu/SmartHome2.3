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
			doorState.setText("��");
		}else{
			doorState.setText("��");
		}
	}

	public void onBtnHome(View v){
		finish();
	}
	
	/*�Ž����ư�ť�ĵ����¼���Ӧ*/
	public void onBtnSetDoorState(View v){

		/*�ж������Ƿ���������,û������ʾ,������*/
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildDoor.this).setTitle("��������").setMessage("δ�����Ϸ�����")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   

                }  
            }).show(); //����Ի���
			return;
		}
		
		/*�Ž����Ƶ�ʵ��*/
		if (HomeConfig.RELAY_STATUE) {
				out.println("$");
				HomeConfig.RELAY_STATUE = false;
				doorState.setText("��");
		} else {
				System.out.println(socket);

				out.println("#");
				HomeConfig.RELAY_STATUE = true;
				doorState.setText("��");
		}
	}
}
