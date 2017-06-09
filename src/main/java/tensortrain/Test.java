package tensortrain;

import java.util.List;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import tensortrain.message.USTuple;
import tensortrain.utils.MyUtils;
import tensortrain.utils.MyUtils.Tuple;

public class Test {

	public static void main(String[] args) {
//		double[][] a = {{1,2,3,4,5,6},{7,8,9,10,11,12}};
//		Matrix m = new Matrix(a);
//		m.print(0, 0);
//		USTuple t = MyUtils.svd(m);
//		Matrix u = t.getuMatrix();
//		u.print(10, 4);
//		Matrix ss = t.getsMatrix();
//		ss.print(10, 4);
//		double[][] a = {{1,2},{7,8},{3,4}};
//		Matrix m = new Matrix(a);
//		m.print(0, 0);
//		USTuple t = MyUtils.svd(m);
//		Matrix u = t.getuMatrix();
//		u.print(10, 4);
//		Matrix ss = t.getsMatrix();
//		ss.print(10, 4);
//		Matrix c = u.times(ss);
//		c.print(10, 4);
		double[][] a = {{1,2},{3,4},{5,6}};
		double[][] b = {{5,6},{7,8},{9,10}};
		Jama.Matrix A = new Jama.Matrix(a);
		Jama.Matrix B = new Jama.Matrix(b);
		SingularValueDecomposition sa = A.svd();
		SingularValueDecomposition sb = B.svd();
		Jama.Matrix u1 = sa.getU();
		Jama.Matrix u2 = sb.getU();
		System.out.println("##################U1###################");
		u1.print(10, 4);
		System.out.println("##################U2###################");
		u2.print(10, 4);		
		Jama.Matrix s1 = sa.getS();
		Jama.Matrix s2 = sb.getS();
		System.out.println("##################S1###################");
		s1.print(10, 4);
		System.out.println("##################S2###################");
		s2.print(10, 4);
		Jama.Matrix c1 = u1.times(s1);
		Jama.Matrix c2 = u2.times(s2);		
		Jama.Matrix m = MyUtils.add(c1, c2);
		m.print(10, 4);
		USTuple result= MyUtils.doInnerOrth(m, 0.1);
		Matrix u = result.getuMatrix();
		Matrix s = result.getsMatrix();
		u.print(10, 4);
		s.print(10, 4);
//		int numOfSubtensor = 7;
//		int queue = numOfSubtensor;
//		int result = numOfSubtensor;
//		while(queue != 1){
//			int i = queue/2;
//			int j = queue%2;
//			result += i;
//			queue -= i;
//		}
//		System.out.println(result);
//		Matrix m = new Matrix(2, 2);
//		Matrix n = m.copy();
//		m.set(1, 1, 10);
//		m.print(10, 4);
//		System.out.println("###################################");
//		n.print(10, 4);
		
	}

}
