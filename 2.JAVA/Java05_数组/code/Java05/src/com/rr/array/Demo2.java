package com.rr.array;

public class Demo2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] a = {23, 12, 34, 2, 56};
		//ѡ������
		for(int i = 0; i < 5 - 1; i++ ) {
			
			for(int j = i + 1; j < 5; j++){
				//if(a[i] > a[j]){//���ڣ��������ݣ����յõ�һ������Ľ��
				if(a[i] < a[j]){//С�ڣ��������ݣ����յõ�һ������Ľ��
					int temp = a[i];
					a[i] = a[j];
					a[j] = temp;
				}
			}
		}
		//����foreach Ȼ�� alt + /
		for (int v : a) {
			System.out.println(v);
		}
		
	}

}
