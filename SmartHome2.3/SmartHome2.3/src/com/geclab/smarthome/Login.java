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
 * 登录的时候，用户输入的名和密码应该登录智能家居的网络服务器（控制板）
 * 需要网络服务器的验证，提高智能家居的安全性
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

	//设置登录密码
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
	
	//登录
	public void onBtnLogin01(View v){
		//step1,获取用户输入的用户名和密码
		String inputuser = userName01.getText().toString();
		String inputpass = passWord01.getText().toString();

		//step2,提取正确的用户名和密码
		someData01 = getSharedPreferences(filename01, 0);
		String user = someData01.getString("username01", "Couldn't load data!");
		String pass = someData01.getString("password01", "nothing");
			
		//step3,用户输入的用户名和密码  vs.正确的用户名和密码 
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
			//用户名或密码输入错误的提示
			Toast toast = Toast.makeText(Login.this,
				     "用户名或密码错误，请重新输入。", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	public void onBtnLogin02(View v){
		//step1,获取用户输入的用户名和密码
		String inputuser = userName02.getText().toString();
		String inputpass = passWord02.getText().toString();

		//step2,提取正确的用户名和密码
		someData02 = getSharedPreferences(filename02, 0);
		String user = someData02.getString("username02", "Couldn't load data!");
		String pass = someData02.getString("password02", "nothing");
			
		//step3,用户输入的用户名和密码  vs.正确的用户名和密码 
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
			//用户名或密码输入错误的提示
			Toast toast = Toast.makeText(Login.this,
				     "用户名或密码错误，请重新输入。", Toast.LENGTH_LONG);
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
