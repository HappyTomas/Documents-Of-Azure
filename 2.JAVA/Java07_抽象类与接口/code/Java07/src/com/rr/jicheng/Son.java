package com.rr.jicheng;

public class Son extends Father{

	//����Ĺ��췽����Ĭ�ϻ��Զ����ø����޲εĹ��췽��
	//�������û���޲εĹ��췽��������
	public Son() {
		// TODO Auto-generated constructor stub
		//���ø���Ĺ��췽��
		super(10);
		System.out.println("sub");
		//super(10);//����������������߼�ǰ�����
	}
	
	
	//����������ʵ���˸���ķ�������д
	//��д�ķ�������������Ȩ�޲���С�ڸ��෽���ķ���Ȩ��
	
	//@Override ע�� ��ʾ�����Ǹ���д�ķ���
	@Override
	public void jump() {
		System.out.println("����2��");
	}
	
	@Override
	void sleep() {
		// TODO Auto-generated method stub
		
		//ͨ��super�ؼ��ֿ��Ե��ø���ķ���
		super.sleep();
		System.out.println("ϲ��˵�λ���ĥ��");
	}
	
	@Override
	protected void drink() {
		// TODO Auto-generated method stub
		super.drink();
	}
		
	
	void test(){
		super.sleep();
	}
}
