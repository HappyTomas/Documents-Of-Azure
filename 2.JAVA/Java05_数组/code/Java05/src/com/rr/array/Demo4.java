package com.rr.array;

import java.util.Arrays;

public class Demo4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] a = {12, 34, 2, 23, 9};
		
		//Ĭ������
		Arrays.sort(a);
		
		for (int v : a) {
			System.out.println(v);
		}
		
		System.out.println(a);
		//����������ת��Ϊ�ַ�����ʽ
		String str = Arrays.toString(a);
		System.out.println(str);
		
		//���ֲ���
		//��һ�������������ҵ����飬�ڶ���������Ҫ���ҵ�ֵ
		int index = Arrays.binarySearch(a, 23);
		System.out.println(index);
	}

}
