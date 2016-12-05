package com.geclab.smarthome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * �ͷ�����ͨ�ŵ���
 * @author Yanzeng
 *
 */
public class ServiceSocket extends Service{

	private boolean threadDisable = false;//��ʶ�߳��Ƿ����
	Util util = new Util();			//����ͨѶЭ��

	Handler handler = null; 		// ������ʱ��
    private int i_time_out = 5000; 	// ��ʱ��ʱ��
    private Socket mSocketClient = null;
    private PrintWriter out = null;//Wraps either an existing OutputStream or an existing Writer
    							    //and provides convenience methods for printing common data types 
    								//in a human readable format. No IOException is thrown by this class. 
    								//Instead, callers should use checkError() to see if a problem has 
    								//occurred in this writer.
    
    //������ʹ��startService����
    public IBinder onBind(Intent intent) {
        return null;
    }
 
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("ServiceSocket", "ServiceSocket is start.........." );
	
        serviceSocket();	   // ���߳�, ����tcp����������
        
        handler = new Handler();        
        handler.postDelayed(runnable, i_time_out); //����i_time_out���ִ��runnable. �ӳ�һ��ʱ�����ִ��ĳ���� 
    }

    @Override
    public void onDestroy() {
        
        if(handler!=null)
        	handler.removeCallbacks(runnable);
        Log.v("CountService", "stopService on destroy");
        try {
        	if((mSocketClient!=null)&&(mSocketClient.isConnected()))
        		mSocketClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        this.threadDisable = true;
		
		super.onDestroy();	
    }
    
    /*�������������ݴ����߳�, �ں�̨����ӷ�������������, ��֪ͨ���½���*/
    void serviceSocket(){
    	new Thread(new Runnable() {
    		 
            @SuppressWarnings("static-access") //����ע�������Ǹ�������һ��ָ��������Ա���ע�Ĵ���Ԫ���ڲ���ĳЩ���汣�־�Ĭ��
			@Override
            public void run() {
				BufferedReader mBufferedReaderClient = null;		
				
				/*����intent , �����ܹ�֪ͨ�ϲ�Ӧ��*/
				Intent intent = new Intent();  
                intent.setAction("android.intent.action.MY_RECEIVER");
                
                /*����������ݵ�buffer  , ������*/
                char[] buffer = new char[64];
                for (int i = 0; i < buffer.length; i++) {
			    	buffer[i] = '\0';        
			    }
                
				try {			
					// �ȴ�������, ���ڴ˴�һֱ�ڵȴ�, ����ֱ�������ϲŻ�����while,���·����ж�����Ķ��Ƕ����
					while((NetworkUtil.socket == null)){
						try {
							if((NetworkUtil.socket!= null)&& (NetworkUtil.socket.isConnected()))
								break;
							Thread.sleep(1000);
							if(threadDisable){
								return;
							}
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					mSocketClient = NetworkUtil.socket;
					//ȡ�����롢�����
					if((mSocketClient!=null) && (mSocketClient.isConnected())){
						mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
					}
					out = NetworkUtil.out;
					
	                /**
	        		 * ��ȡ��ǰ������״̬���ͱ���    #SERVERSIGN#A#B#C#D#E#F#
	        		 * �״ν��ձ�����������:   #SERVERSIGN#A1#B1#C0#D0#E1#F0
	        		 */	
	                if ((mSocketClient!=null) && (mSocketClient.isConnected())) {
	        			out.println("#SERVERSIGN#A#B#C#D#E#F#");
	        			System.out.println("send to server :#SERVERSIGN#A#B#C#D#E#F#");
	        		} 
	                /*һֱ������������*/
					while (!threadDisable) {	                    
//						Log.v("threadDisable", "threadDisable");
						if((mSocketClient!=null)&&mSocketClient.isConnected()&&(mBufferedReaderClient.read(buffer)>0))
						{
							String strRecvMsg = String.copyValueOf(buffer);							
							//strRecvMsg = strRecvMsg.substring(4);// ���ڽ��յ�����utf8��ʽ��, ��ǰ�ĸ��ֽ�����Ч��, ȥ��
							String strTemper = null;
							String strState  = null;
							Log.v("CountService", "read:"+strRecvMsg );
							
							//	  ���յ����ݺ�, ��intent˵�������Ǹ����Ǹ�
							if(strRecvMsg.indexOf("#SERVERSDATA#")!=-1){// ��ʪ��
								strTemper = strRecvMsg.substring(strRecvMsg.indexOf("#SERVERSDATA#"));
								intent.putExtra("strRecvMsg", strTemper); 															
							}
							if((strRecvMsg.indexOf("#SERVERSIGN#")!=-1)){// ������״̬
								strState =  strRecvMsg.substring(strRecvMsg.indexOf("#SERVERSIGN#"));
								Log.v("strState", strState);  
								/*// ����״̬
								if((strState.indexOf("#ON#")!=-1)||(strState.indexOf("#OFF#")!=-1)){
									util.analyseSingleStatus(strState);
								}else{//��������״̬
									util.getStatus(strState);
								}
								intent.putExtra("strRecvMsg", strState); */
							}
							sendBroadcast(intent);//  ֪ͨ��ע������ϲ��Ӧ�ó���
							/*���buffer*/
						    for (int i = 0; i < buffer.length; i++) {
						    	buffer[i] = '\0';        
						    }
						}else{	// �������û�������Ͼ�����							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
		            }
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
            }
        }).start();
    	
    }
    /**��ʱ��  ��Ӧ����**/
    Runnable runnable=new Runnable() { 
        @Override
        public void run() { 
            // TODO Auto-generated method stub 
            //Ҫ�������� 
        	if((mSocketClient!=null)&&(mSocketClient.isConnected()))
			{
        		Log.v("timer", "timer send");  
				out.println("#SERVERSDATA#");				
//				i_time_out = 10000;
				//  ��ʱ��ֹͣ
            }else{
            	Log.v("timer", "mSocketClient.is not Connected()");
           		handler.postDelayed(this, i_time_out); // �ٴ�����, ÿ��i_time_outִ��һ��runnable.
            }  
        }
    };
}
