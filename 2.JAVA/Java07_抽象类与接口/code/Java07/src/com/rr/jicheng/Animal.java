package com.rr.jicheng;

//Ĭ������£�һ����Ĭ�ϼ̳���Object��
public class Animal {
	String name;
	int age;
	
	{
		System.out.println("Animal�Ĺ�������");
	}
	
	static{
		System.out.println("Animal�ľ�̬�����");
	}
	
	//����Ĺ��췽�����ܱ�����̳�
	public Animal(){
		System.out.println("Animal�Ĺ��췽��");
	}
	
	void eat(){
		
	}
	
	public void drink(){
		
	}
	
	protected void Sleep(){
		
	}
	
	private void Play(){
		
	}
	
	//��̬����
	public static void staticMethod(){
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
