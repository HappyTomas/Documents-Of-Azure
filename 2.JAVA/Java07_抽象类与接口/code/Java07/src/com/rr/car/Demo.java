package com.rr.car;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Engine engine = new Engine();
		engine.name = "�¹���";
		Car car = new Car();
		car.engine = engine;
		car.run();
	}

}
