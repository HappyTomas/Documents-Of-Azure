package com.rr.jicheng;

//final���ε��಻�ܱ��̳�
public final class Person {
	//final���εı����������޸ģ��൱�ڳ���
	final int age = 10;
	final double PI = 3.14;
	//���final���εĳ���û�и���ֵ���ͱ����ڹ��췽���и���ֵ
	final int weight;
	
	//
	public static final int HEIGHT = 100;
	
	public Person() {
		// TODO Auto-generated constructor stub
		weight = 10;
	}
	
	public Person(int weight){
		this.weight = weight;
	}
	
	void test(){
		int a = ConstValue.MAX_SCOPE - 1;
		//age = 20;
	}
	//final���ε��βΣ��ڷ����в����޸�ֵ
	void test1(final int v){
		//v = 100;
	}
}
