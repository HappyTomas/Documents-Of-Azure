package com.rr.jicheng;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Bird bird = new Bird();
		bird.name = "Polly";
		bird.eat();
		bird.drink();
		bird.Sleep();
		//����˽�еķ�������������ܷ���
		//bird.Play();
		bird.fly();
		
		
//		Animal animal = new Animal();
//		animal.eat();
		//����Ķ����ܵ�������ķ���
		//animal.fly();
		
		Animal.staticMethod();
		Bird.staticMethod();
		
		Father f = new Father();
		f.jump();
		Son s = new Son();
		s.jump();
		
		s.sleep();
		s.run();
	}

}
