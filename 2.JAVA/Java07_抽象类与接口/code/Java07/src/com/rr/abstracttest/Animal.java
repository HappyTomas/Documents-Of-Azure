package com.rr.abstracttest;

//�������в�һ���г��󷽷�
//��������г��󷽷�����ô���������ǳ�����
public abstract class Animal {
	String name;
	int age;
	
	//���췽�������ǳ��󷽷�
	//public abstract Animal();
	
	void eat(){}
	
	void drink(){
		System.out.println("drink");
	}
	
	//���󷽷�,ֻ�з�����������û�з�����ʵ��
	//�������ʵ�ֳ������еĳ��󷽷�
	public abstract void run();
}
