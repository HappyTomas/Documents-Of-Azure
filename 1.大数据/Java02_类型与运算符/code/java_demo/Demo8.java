
public class Demo8{

	public static void main(String[] args){
		// ��ϵ���ʽ�����ص�ֵΪtrue����false
		System.out.println(10 > 3);
		System.out.println(10 < 3);
		System.out.println(10 == 3);
		System.out.println(10 != 3);

		//д�����ʽ����ʾa�Ƿ����0��С��100
		int a = 1000;
		// 0 < a < 100
		//0 < a�õ�true���Ƚϱ�Ϊ true < 100
		//System.out.println(0 < a < 100);

		//�߼����ʽ�����ؽ��Ϊtru����false
		System.out.println(a > 0 || a < 100);

		System.out.println(!(a > 0 && a < 100));


		// �ж��Ƿ�����
		//�ܱ�4���������ǲ��ܱ�100�����������ܱ�400����
		int year = 2016; 
		System.out.println( (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 );
		


		//������ֵ�е����ֵ
		int a1 = 10;
		int b = 20;
		int max = a1 > b ? a1 : b;
		System.out.println(max);
	}
}




