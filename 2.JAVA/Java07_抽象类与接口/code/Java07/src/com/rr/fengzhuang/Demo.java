package com.rr.fengzhuang;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Person p = new Person();
		p.name = "zhangsan";
		p.age = 12;
		p.height = 120;
		//���ⲻ�ܷ���˽�еĳ�Ա����
		//p.weight = 100;
		
		p.run();
		p.eat();
		p.drink();
		//���ⲻ�ܷ���˽�еķ���
		//p.sleep();
		
		
		Dog d = new Dog();
		d.setName("wangcai");
		d.setAge(2);
		int age = d.getAge();
		System.out.println(age);
	}

}
