package com.rr.params;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		add(1, 2, 3);
		add(1, 2);
	}

	//�ɱ������ ����... ��������
	static void add(int... args){
		//�����ڲ��������鴦��
		int sum = 0;
		for(int i = 0; i < args.length; i++){
			sum += args[i];
		}
		System.out.println(sum);
	}
	
}
