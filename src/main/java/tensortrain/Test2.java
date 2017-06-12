package tensortrain;

import java.util.ArrayList;

import Jama.Matrix;

public class Test2 {

	public static void main(String[] args) {
		Matrix m = new Matrix(4, 4, 1);
		m.print(10, 4);
		int[] r = {0,1};
		int[] c = {1,2};
		Matrix m1 = new Matrix(2,2,2);
		m1.print(10, 4);
		m.setMatrix(0, 1, 1, 2, m1);
		m.print(10, 4);
		Matrix m2 = new Matrix(2,2,5);
		m.setMatrix(2, 3, 1, 2, m2);
		m.print(10, 4);
		ArrayList<String> list = new ArrayList<>(2);
		
		list.add(0, "sdfa");
		list.add(1, "sdfa");
	}

}
