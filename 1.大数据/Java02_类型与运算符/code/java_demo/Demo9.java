
public class Demo9{

	public static void main(String[] args){
		
		System.out.println(1 & 1);
		System.out.println(1 & 0);
		System.out.println(0 | 0);
		System.out.println(1 | 0);
		System.out.println(1 ^ 1);
		System.out.println(1 ^ 0);
		System.out.println(~1);
		
		int a = 10;//����
		int k = 6;//�ܳ�
		int b = a ^ k;//���ܺ�����
		System.out.println(b);
		System.out.println(b ^ k);//����

		//����1λ�൱�ڳ���2
		System.out.println(10 >> 1);
		//����1λ�൱�ڳ���2
		System.out.println(10 << 1);

	}

}