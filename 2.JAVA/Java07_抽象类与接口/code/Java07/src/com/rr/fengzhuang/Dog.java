package com.rr.fengzhuang;

public class Dog {
	private String name;
	private int age;
	
	//set������ͨ��set�����Գ�Ա���������ԣ����и�ֵ
	//������������set��ͷ��set����������õı��������֣����е�������ĸ��д
	public void setName(String name){
		this.name = name;
	}
	
	public void setAge(int age){
		this.age = age;
	}
	
	//get���������ڻ�ȡĳ����Ա������ֵ
	//��������get��ͷ��������Ż�ȡ�ı��������֣����е�������ĸ��д
	public String getName(){
		return name;
	}
	
	public int getAge(){
		return age;
	}
	
	public void bark(){
		System.out.println("~~~wow~~~");
	}
	
}
