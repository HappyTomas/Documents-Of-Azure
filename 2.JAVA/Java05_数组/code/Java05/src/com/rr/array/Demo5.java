package com.rr.array;

public class Demo5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//��ά����,�����������飬�����е�Ԫ��Ϊһ������
		
		//2��3��
		int[][] arr = new int[2][3];
		
		arr[0][0] = 1;
		arr[0][2] = 2;
		
		System.out.println(arr[0][0]);
		
		//������������Ԫ�أ�ÿ��Ԫ�ش������һ��һά����ĵ�ַ
		int[][] arr1 = new int[2][];
		arr1[0] = new int[3];
		arr1[1] = new int[2];
		System.out.println(arr1[0][0]);
		
		//int[][] arr2 = new int[][3];//����
		
		//��ά����ľ�̬��ʼ��
		int[][] arr2 = {{1, 2}, {3, 4}, {5}};
		
		for(int i = 0; i < arr2.length; i++){
			
			for(int j = 0; j < arr2[i].length; j++){
				System.out.print(arr2[i][j]);
			}
			System.out.println();
		}
	}

}
