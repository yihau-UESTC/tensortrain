package tensortrain;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import tensortrain.message.USTuple;
import tensortrain.utils.MyUtils;

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
		double[][] a = {{1,2},{7,8},{3,4}};
		Matrix m = new Matrix(a);
		m.print(0, 0);
		USTuple t = MyUtils.svd(m);
		Matrix u = t.getuMatrix();
		u.print(10, 4);
		Matrix ss = t.getsMatrix();
		ss.print(10, 4);
		Matrix c = u.times(ss);
		c.print(10, 4);
	}

}
