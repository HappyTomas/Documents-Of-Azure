

public class Demo7{

	public static void main(String[] args){
		
		int a = 10;
		//++���ڱ���ǰ���ȶԱ���ִ��+1������Ȼ���ٲ����������ʽ������
		//++a -> 11 (a=11),Ȼ��11��ֵ��b
		int b = ++a;
		System.out.println(a + " " + b);
		
		//++���ڱ������棬��ȡ������ֵ���������㣬Ȼ�����ִ��+1����
		//a++ ->11(a��ֵ)����11��ֵ��b��Ȼ��aִ��+1����
		b = a++;
		System.out.println(a + " " + b);
		
		a = 10;
		int c = a++ + ++a;
		//       10 + 12
		System.out.println(a + " " + c);

		double d = 12.5;
		d++;// ++d; //��Ϊһ����������䣬����д���ȼ�
		System.out.println(d);

	}
}



