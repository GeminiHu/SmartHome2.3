package com.geclab.smarthome;

/**
 * ͨѶЭ�������
 * @author Yanzeng
 *
 */
public class Util {

	/***********��������״̬ *********************************
	 * ��������״̬
	 * ���յ��������п�����:
	 * #SERVERSIGN#B#ON#  	 
	 * *********************************************************************/
	/*public static void analyseSingleStatus(String str) {

		String sub = str.substring(str.indexOf('#'), str.lastIndexOf('#') + 1);
		System.out.println(sub);

		String[] strs = sub.split("#");

		if (strs[1].equals("SERVERSIGN")) {

			if (strs[2].charAt(0) == 'A') {				//�̵�����ģ���Ž�
				if (strs[3].equals("ON")) {
					HomeConfig.RELAY_STATUE = true;
				} else if (strs[2].equals("OFF")) {
					HomeConfig.RELAY_STATUE = false;
				}
			} else if (strs[2].charAt(0) == 'B') {		//led��ģ�����Ե�
				if (strs[3].equals("ON")) {
					HomeConfig.ROOMLIGHT_STATUE = true;
				} else if (strs[3].equals("OFF")) {
					HomeConfig.ROOMLIGHT_STATUE = false;
				}
			} else if (strs[2].charAt(0) == 'C') {			//led��ģ����Ե�
				if (strs[3].equals("ON")) {
					HomeConfig.CUSTOMERRIGHT_STATUE = true;
				} else if (strs[3].equals("OFF")) {
					HomeConfig.CUSTOMERRIGHT_STATUE = false;
				}
			} else if (strs[2].charAt(0) == 'D') {			//���������ģ�ⴰ������
				if (strs[3].equals("ON")) {
					HomeConfig.CURTAIN_STATUE = true;
				} else if (strs[3].equals("OFF")) {
					HomeConfig.CURTAIN_STATUE = false;
				}
			} else if (strs[2].charAt(0) == 'E') {			//ֱ�������ģ��յ�
				if (strs[3].equals("ON")) {
					HomeConfig.AIRCONDITIONING_STATUE = true;
				} else if (strs[3].equals("OFF")) {
					HomeConfig.AIRCONDITIONING_STATUE = false;
				}
			} else if (strs[2].charAt(0) == 'F') {			//ģ�ⱨ��
				if (strs[3].equals("ON")) {
					HomeConfig.ALARM_STATUE = true;
				} else if (strs[3].equals("OFF")) {
					HomeConfig.ALARM_STATUE = false;
				}
			} else {
				System.out.println("���ݽ��ճ���");
			}
		}
	}

	/**************************************************************
	 * ���շ�������״̬: A Ϊ�̵���״̬ ,B Ϊ�����״̬ ,C Ϊ������״̬ ,D Ϊ����״̬, E Ϊ�յ�״̬��F Ϊ����״̬
	 * �������״̬һ��
	 * ���յ��������п�����:
	 * #SERVERSIGN#A0#B0#C0#D0#E0#F1#  �ȵ�
	 *************************************************************/
	/*public static void getStatus(String str) {

		String sub = str.substring(str.indexOf('#'), str.lastIndexOf('#') + 1);
		System.out.println("str:"+str);
		System.out.println("sub:"+sub);

		String[] strs = sub.split("#");

		if (strs[1].equals("SERVERSIGN")) {
			System.out.println("�ַ���������ȡ״̬");
			for (int i = 2; i < strs.length; i++) {// ѭ��ÿ��strs�Ӵ�
				if (strs[i].charAt(0) == 'A') {//strs[i]:  A0����A1
					if (strs[i].substring(1).equals("1")) {
						HomeConfig.RELAY_STATUE = true;
					} else if (strs[i].substring(1).equals("0")) {
						HomeConfig.RELAY_STATUE = false;
					}
				} else if (strs[i].charAt(0) == 'C') {//strs[i]:  B0����B1  ��ͬ
					if (strs[i].substring(1).equals("1")) {
						HomeConfig.ROOMLIGHT_STATUE = true;
					} else if (strs[i].substring(1).equals("0")) {
						HomeConfig.ROOMLIGHT_STATUE = false;
					}
				} else if (strs[i].charAt(0) == 'B') {
					if (strs[i].substring(1).equals("1")) {
						HomeConfig.CUSTOMERRIGHT_STATUE = true;
					} else if (strs[i].substring(1).equals("0")) {
						HomeConfig.CUSTOMERRIGHT_STATUE = false;
					}
				} else if (strs[i].charAt(0) == 'D') {
					if (strs[i].substring(1).equals("1")) {
						HomeConfig.CURTAIN_STATUE = true;
					} else if (strs[i].substring(1).equals("0")) {
						HomeConfig.CURTAIN_STATUE = false;
					}
				} else if (strs[i].charAt(0) == 'E') {
					if (strs[i].substring(1).equals("1")) {
						HomeConfig.AIRCONDITIONING_STATUE = true;
					} else if (strs[i].substring(1).equals("0")) {
						HomeConfig.AIRCONDITIONING_STATUE = false;
					}
				}else if (strs[i].charAt(0) == 'F') {
					if (strs[i].substring(1).equals("1")) {
						HomeConfig.ALARM_STATUE = true;
					} else if (strs[i].substring(1).equals("0")) {
						HomeConfig.ALARM_STATUE = false;
					}
					System.out.println("HomeConfig.ALERM_STATUE");
					System.out.println(HomeConfig.ALARM_STATUE);
				}  else {
					System.out.println("���ݽ��ճ���");
					break;
				}
			}
		}

	}*/
}
