package com.geclab.smarthome;

public class HomeConfig {
	
	/**服务器端的IP */
	public static final String SERVER_IP = "192.168.10.1";
	
	/**服务器端接听的TCP端口 */
	public static final int SERVER_TCP_PORT = 8002;


	/**门继电器状态  false为关, true为开*/
	public static  boolean RELAY_STATUE = false;
	
	/**01卧室门继电器状态  false为关, true为开*/
	public static  boolean RELAY_STATUE01 = false;
	
	/**02卧室门继电器状态  false为关, true为开*/
	public static  boolean RELAY_STATUE02 = false;
	
	/**房间灯状态  false为关, true为开*/
	public static  boolean ROOMLIGHT_STATUE = false;
	
	/**客厅灯状态  false为关, true为开*/
	public static  boolean CUSTOMERRIGHT_STATUE = false;
	
	/** 窗帘状态 false为关, true为开 */
	public static boolean CURTAIN_STATUE = false;
	 
	/** 空调状态false为关, true为开 */
	public static boolean AIRCONDITIONING_STATUE = false;
	
	/** 警报状态false为关, true为开 */
	public static boolean ALARM_STATUE = false;

}
