
public class Demo6{

	public static void main(String[] args){
		
		// "/"���߶����������൱��ȡ��
		int a = 10 / 3;
		System.out.println(a);

		double d = 10 / 3.0;
		System.out.println(d);

		int a1 = 20;
		int a2 = 30;
		double d1 = a1 / (double)a2;
		// a1 / a2 * 1.0
		System.out.println(d1);
		
		// %ȡ��
		int a3 = 10 % 3;
		System.out.println(a3);
		double d2 = 10.5 % 2;
		System.out.println(d2);//0.5

		//+
		int a4 = 10 + 4;

		//�ַ��� ����������������
		// + �ַ�������ϣ�ƴ�ӣ�
		System.out.println("hello" + "world");
		System.out.println("a4=" + a4);
		System.out.println(12 + "" + 4);
		System.out.println(12 + 4 + "");
		System.out.println(12 + 4);

		a4 += 10;//a4 = a4 + 10;
		System.out.println(a4);


	}

}