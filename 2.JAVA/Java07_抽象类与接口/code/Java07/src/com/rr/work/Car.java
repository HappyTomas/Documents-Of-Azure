package com.rr.work;

public class Car {
	int speed;
	
	public Car(int speed){
		this.speed = speed;
	}
	//������ʻ��·��
	void runOnRoad(Road road){
		
		int time = road.length / speed;
		System.out.println(time);
	}
}
