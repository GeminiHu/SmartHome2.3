package com.geclab.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * ��¼��ʱ���û��������������Ӧ�õ�¼���ܼҾӵ���������������ư壩
 * ��Ҫ�������������֤��������ܼҾӵİ�ȫ��
 * @author Yanzeng
 *
 */
public class Login extends Activity{

	public static String filename01 = "MySharedString01",filename02 = "MySharedString02";
	SharedPreferences someData01,someData02;
	EditText userName01, passWord01,userName02,passWord02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initVars01();
		initVars02();
	}
	
	public void initVars01(){
		userName01 = (EditText)findViewById(R.id.etUser);
		passWord01 = (EditText)findViewById(R.id.etPass);
		
	}
	public void initVars02(){
		userName02 = (EditText)findViewById(R.id.etUser);
		passWord02 = (EditText)findViewById(R.id.etPass);
		
	}

	//���õ�¼����
	public void onBtnSetPass01(View v){
		SharedPreferences.Editor editor = someData01.edit();
		editor.clear();
		editor.putString("username01", userName01.getText().toString());
		editor.putString("password01", passWord01.getText().toString());
		editor.commit();
	}
	public void onBtnSetPass02(View v){
		SharedPreferences.Editor editor = someData02.edit();
		editor.clear();
		editor.putString("username02", userName02.getText().toString());
		editor.putString("password02", passWord02.getText().toString());
		editor.commit();
	}
	
	//��¼
	public void onBtnLogin01(View v){
		//step1,��ȡ�û�������û���������
		String inputuser = userName01.getText().toString();
		String inputpass = passWord01.getText().toString();

		//step2,��ȡ��ȷ���û���������
		someData01 = getSharedPreferences(filename01, 0);
		String user = someData01.getString("username01", "Couldn't load data!");
		String pass = someData01.getString("password01", "nothing");
			
		//step3,�û�������û���������  vs.��ȷ���û��������� 
		if(user.contentEquals(inputuser) && pass.contentEquals(inputpass)){
			try {
				Class ourClass = Class.forName("com.geclab.smarthome.MainPage");
				Intent ourIntent = new Intent(Login.this, ourClass);
				startActivity(ourIntent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//�û�������������������ʾ
			Toast toast = Toast.makeText(Login.this,
				     "�û���������������������롣", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	public void onBtnLogin02(View v){
		//step1,��ȡ�û�������û���������
		String inputuser = userName02.getText().toString();
		String inputpass = passWord02.getText().toString();

		//step2,��ȡ��ȷ���û���������
		someData02 = getSharedPreferences(filename02, 0);
		String user = someData02.getString("username02", "Couldn't load data!");
		String pass = someData02.getString("password02", "nothing");
			
		//step3,�û�������û���������  vs.��ȷ���û��������� 
		if(user.contentEquals(inputuser) && pass.contentEquals(inputpass)){
			try {
				Class ourClass02 = Class.forName("com.geclab.smarthome.MainPage02");
				Intent ourIntent02 = new Intent(Login.this, ourClass02);
				startActivity(ourIntent02);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//�û�������������������ʾ
			Toast toast = Toast.makeText(Login.this,
				     "�û���������������������롣", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	public void onBtnCancel01(View v){
		finish();
	}
	public void onBtnCancel02(View v){
		finish();
	}
}
