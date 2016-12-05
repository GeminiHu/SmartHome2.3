package com.geclab.smarthome;

import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChildAir extends Activity{

	TextView airState;
	Socket socket = NetworkUtil.socket;
	//Util util = new Util();
	PrintWriter out = NetworkUtil.out;
	
	Handler handler = null; //  ������ʱ��
    private int i_time_out = 3000; // ��ʱ��ʱ��
    private boolean isClickable = true;	//�Ƿ��ܹ������յ�
    private ProgressDialog pd;  	//  ���ȿ�
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_air);
		
		initVariables();
	}
	
	private void initVariables(){
		airState = (TextView)findViewById(R.id.tvAirState);
		
		handler = new Handler();
		if(HomeConfig.AIRCONDITIONING_STATUE){
			airState.setText("��");
		}else{
			airState.setText("��");
		}
	}
	

	//�ص�������
	public void onBtnHome(View v){
		finish();
	}
	
	//���ƿյ�
	public void onBtnSetAir(View v){
		/*�ж������Ƿ���������,û������ʾ,������*/
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildAir.this).setTitle("��������").setMessage("δ�����Ϸ�����")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   

                }  
            }).show(); 
			return;
		}
		// isChecked���ǰ�ť״̬
		if (HomeConfig.AIRCONDITIONING_STATUE) {
			if(!isClickable){
				if(pd==null){
					processThread();
				}									
				return;
			}
			isClickable = false;
			out.println("#CTRL#E#OFF#");
			HomeConfig.AIRCONDITIONING_STATUE = false;
			airState.setText("��");
			Toast.makeText(ChildAir.this,
					"�յ��ѹر�", Toast.LENGTH_SHORT).show();
	        handler.postDelayed(runnable, i_time_out); //ÿi_time_out��ִ��һ��runnable.
		}else {
			if(!isClickable){
				if(pd==null){
					processThread();
				}	
				return;
			}
			isClickable = false;
			out.println("#CTRL#E#ON#");
			HomeConfig.AIRCONDITIONING_STATUE = true;
			airState.setText("��");
			Toast.makeText(ChildAir.this,
					"�յ��Ѵ�", Toast.LENGTH_SHORT).show();
			handler.postDelayed(runnable, i_time_out); //ÿi_time_out��ִ��һ��runnable.							
		} 
	
	}

	private void processThread(){
	      //����һ�����ؽ�����
	      pd = ProgressDialog.show(ChildAir.this, "�յ����ڲ�����", "�յ����ڲ�����...���Ժ�����...");
	      new Thread(){
	         public void run(){
	            //������ִ�г���ʱ����
	            while(!isClickable){
	            	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }	            	
	            //ִ����Ϻ��handler����һ����Ϣ
	            handler_pd.sendEmptyMessage(0);
	         }
	      }.start();
	}

	private Handler handler_pd =new Handler(){
	   @Override
	   //������Ϣ���ͳ�����ʱ���ִ��Handler���������
	   public void handleMessage(Message msg){
	      super.handleMessage(msg);	      
	      pd.dismiss();//ֻҪִ�е�����͹رնԻ���
	      pd = null;
	   }
	};

	
	/**��ʱ��  ��Ӧ����      ��Ҫ����λ�������յ���ʱ��Ƚ���, ���Ա���Ӹ���ʱ, ÿ����ò��ܹ��ٽ��п���**/
  Runnable runnable=new Runnable() { 
      @Override
      public void run() { 
          // TODO Auto-generated method stub 
          //Ҫ�������� 
      	    Log.v("timer", "timer isClickable");  
      	    isClickable = true;
      } 
  };  
		
}
