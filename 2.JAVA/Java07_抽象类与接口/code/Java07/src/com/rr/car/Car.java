package com.rr.car;

public class Car {
	String name;
	//Car�ɺܶ������ɣ��������Engine
	Engine engine;
	
//	String tyreName;
//	String tyreStyle;
	
	void run(){
		//����������
		engine.start();
		
		System.out.println("������ʼ��ʻ");
	}
}
