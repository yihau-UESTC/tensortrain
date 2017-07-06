package tensortrain;

import java.io.IOException;

import Jama.Matrix;
import tensortrain.datatype.Vec;
import tensortrain.message.USTuple;
import tensortrain.utils.MyUtils;

public class Test3 {

	public static void main(String[] args) {
//		double[][] data = {{1,2,3},{4,5,6}};//2*3
//		Matrix m = new Matrix(data);
//		m.print(10, 4);
//		USTuple t = MyUtils.svd(m);
//		t.getuMatrix().print(10, 4);
//		t.getsMatrix().print(10, 4);
////		m.svd().getU().print(10, 4);
//		double[][] data1 = {{1,2},{3,4},{5,6}};//3*2
//		Matrix m1 = new Matrix(data1);
//		m1.print(10, 4);
//		USTuple t1 = MyUtils.svd(m1);
//		t1.getuMatrix().print(10, 4);
//		t1.getsMatrix().print(10, 4);
//		m1.svd().getU().print(10, 4);
		//================================//
		System.out.println("==========================================");
		double[][] data2 = {{1,2},{3,4},{5,6}};//3*2
		Matrix m2 = new Matrix(data2);
		m2.print(10, 4);
		USTuple t1 = MyUtils.svd(m2);
		t1.getuMatrix().print(10, 4);
		double[][] data3 = {{5,2},{1,3},{2,8}};
		Matrix m3 = new Matrix(data3);
		USTuple t2 = MyUtils.svd(m3);
//		Matrix merge = MyUtils.add(t1.getuMatrix().times(t1.getsMatrix()), t2.getuMatrix().times(t2.getsMatrix()));
//		MyUtils.doInnerOrth(merge, 0.01);
//		merge.print(10, 4);
		USTuple t = MyUtils.doInnerOrth2(t1.getuMatrix().times(t1.getsMatrix()), t2.getuMatrix().times(t2.getsMatrix()), 0.0000001);
		t.getuMatrix().print(10, 4);
		t.getsMatrix().print(10, 4);
		try {
			MyUtils.writeMatrixToFile(t.getuMatrix(),"e://test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
