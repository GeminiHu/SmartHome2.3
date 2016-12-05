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
	
	Handler handler = null; //  操作定时器
    private int i_time_out = 3000; // 定时器时间
    private boolean isClickable = true;	//是否能够操作空调
    private ProgressDialog pd;  	//  进度框
	
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
			airState.setText("开");
		}else{
			airState.setText("关");
		}
	}
	

	//回到主界面
	public void onBtnHome(View v){
		finish();
	}
	
	//控制空调
	public void onBtnSetAir(View v){
		/*判断网络是否连接上了,没有则提示,并返回*/
		if ((socket==null) || (!(socket.isConnected()))) {
			new AlertDialog.Builder(ChildAir.this).setTitle("网络连接").setMessage("未连接上服务器")  
            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    // TODO Auto-generated method stub   

                }  
            }).show(); 
			return;
		}
		// isChecked就是按钮状态
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
			airState.setText("关");
			Toast.makeText(ChildAir.this,
					"空调已关闭", Toast.LENGTH_SHORT).show();
	        handler.postDelayed(runnable, i_time_out); //每i_time_out秒执行一次runnable.
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
			airState.setText("开");
			Toast.makeText(ChildAir.this,
					"空调已打开", Toast.LENGTH_SHORT).show();
			handler.postDelayed(runnable, i_time_out); //每i_time_out秒执行一次runnable.							
		} 
	
	}

	private void processThread(){
	      //构建一个下载进度条
	      pd = ProgressDialog.show(ChildAir.this, "空调还在操作中", "空调还在操作中...请稍后再试...");
	      new Thread(){
	         public void run(){
	            //在这里执行长耗时方法
	            while(!isClickable){
	            	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }	            	
	            //执行完毕后给handler发送一个消息
	            handler_pd.sendEmptyMessage(0);
	         }
	      }.start();
	}

	private Handler handler_pd =new Handler(){
	   @Override
	   //当有消息发送出来的时候就执行Handler的这个方法
	   public void handleMessage(Message msg){
	      super.handleMessage(msg);	      
	      pd.dismiss();//只要执行到这里就关闭对话框
	      pd = null;
	   }
	};

	
	/**定时器  响应函数      主要是下位机操作空调的时间比较慢, 所以必须加个延时, 每隔多久才能够再进行控制**/
  Runnable runnable=new Runnable() { 
      @Override
      public void run() { 
          // TODO Auto-generated method stub 
          //要做的事情 
      	    Log.v("timer", "timer isClickable");  
      	    isClickable = true;
      } 
  };  
		
}
