package com.rr.jicheng;

public class Bird extends Animal{
	{
		System.out.println("Bird�Ĺ�������");
	}
	
	static{
		System.out.println("Bird�ľ�̬�����");
	}
	
	public Bird(){
		System.out.println("Bird�Ĺ��췽��");
	}
	
	//���໹����ӵ���Լ������Ժͷ���
	void fly(){
		//�����ڲ����Ե��ø���ȱʡ�ġ�public��protected�ķ���
		this.eat();
		drink();
		Sleep();
		//����̳��˸���˽�еķ����������������ڲ����ܵ���
		//Play();
		System.out.println("fly");
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
