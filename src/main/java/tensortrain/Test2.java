package tensortrain;

import java.util.ArrayList;

import Jama.Matrix;
import tensortrain.message.USTuple;
import tensortrain.utils.MyUtils;

public class Test2 {

	public static void main(String[] args) {
		double[][] d1 = {{1},{2},{3},{4}};
		double[][] d2 = {{5},{6},{7},{8}};
		Matrix m1 = new Matrix(d1);
		Matrix m2 = new Matrix(d2);
		USTuple t1 = MyUtils.svd(m1);
		USTuple t2 = MyUtils.svd(m2);
		t1.getuMatrix().print(10, 4);
		t1.getsMatrix().print(10, 4);
		t2.getuMatrix().print(10, 4);
		t2.getsMatrix().print(10, 4);
		Matrix m3 = MyUtils.add(t1.getuMatrix().times(t1.getsMatrix()), 
				t2.getuMatrix().times(t2.getsMatrix()));
		System.out.println("===============m3==================================");
		m3.print(10, 4);
//		USTuple t3 = MyUtils.doInnerOrth(m3, 0.1);
		USTuple t4 = MyUtils.svd(m3);
//		System.out.println("=============t3==================");
//		t3.getuMatrix().print(10, 4);
//		t3.getsMatrix().print(10, 4);
		System.out.println("=============t4==================");
		t4.getuMatrix().print(10, 4);
		t4.getsMatrix().print(10, 4);
	}

}
