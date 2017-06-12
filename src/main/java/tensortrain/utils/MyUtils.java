package tensortrain.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import tensortrain.datatype.Vec;
import tensortrain.message.USTuple;
/**
 * 
 * @author yihau
 * @date 2017年6月7日
 */
public class MyUtils {
	
	
	public static class Tuple {
        private double norm;
        private Vec vector;
        public Tuple(double norm, Vec vector) {
            this.norm = norm;
            this.vector = vector;
        }

        public double getNorm(){ return norm; }
        public Vec getVector() { return vector; }
    }
	
	
	public static class TupleComparator implements Comparator<Tuple>{

		@Override
		public int compare(Tuple o1, Tuple o2) {
			 if(o1.getNorm() > o2.getNorm())
	                return -1;
	            else if(o1.getNorm() < o2.getNorm())
	                return 1;
	            else
	                return 0;
		}
		
	}
	/**
	 * 包装奇异值分解的u矩阵，返回u；
	 * @param decom
	 * @return
	 */
	public static Matrix getU(SingularValueDecomposition decom){
		Matrix result = new Matrix(decom.getU().getArray());		
		return result;
	}
	
	public static Matrix getS(SingularValueDecomposition decom){
		double[] singularValues = decom.getSingularValues();
		int length = singularValues.length;
		Matrix result = new Matrix(length,length);
	     for(int i = 0; i < length; i++){
	    	 for(int j = 0; j < length; j++){
	    		 if(i == j)result.set(i, j, singularValues[i]);
	    	 }
	     }
	     return result;
	}
	/**
	 * 返回包含矩阵的奇异值分解的U和s矩阵的结果。
	 * @param m
	 * @return
	 */
	public static USTuple svd(Matrix m){
		int row = m.getRowDimension();
		int col = m.getColumnDimension();
		SingularValueDecomposition svdResult;
		USTuple result = null;
		if(row >= col){
			svdResult = m.svd();
			result = new USTuple(svdResult.getU(),svdResult.getS());			
		}else{
			Matrix tempMatrix = m.transpose();
			svdResult = tempMatrix.svd();
			result = new USTuple(svdResult.getV(), svdResult.getS());
		}		
		return result;	
	}
	/**
	 * 把两个矩阵按列拼接起来
	 * @param m1 第一个矩阵
	 * @param m2 第二个矩阵
	 * @return 拼接后的矩阵[m1,m2]
	 */
	public static Matrix add(Matrix m1,Matrix m2){
    	int i1 = m1.getRowDimension();
    	int i2 = m2.getRowDimension();
    	int j1 = m1.getColumnDimension();
    	int j2 = m2.getColumnDimension();
    	if(i1 != i2){
    		throw new IllegalArgumentException("Matrix'row is not compatible !");
    	}
    	int i = i1;
    	int j = j1+j2;
    	Matrix m = new Matrix(i, j);
    	for(int k1 = 0; k1 < i; k1++){
    		for(int k2 = 0; k2 < j; k2++){
    			if(k2 < j1){
    				m.set(k1, k2, m1.get(k1, k2));
    			}else{
    				m.set(k1, k2, m2.get(k1, k2-j1));
    			}
    		}
    	}
    	return m;
    }
	/**
	 * 以一维数组的形式返回矩阵m的第col列
	 * @param m 矩阵m
	 * @param col 返回的列值
	 * @return double[]
	 */
	public static double[] getCol(Matrix m,int col){
		if(col > m.getColumnDimension()-1){
			throw new IllegalArgumentException("Col outofIndex !");	}
		double[] result = new double[m.getRowDimension()];
		for(int i = 0; i < result.length; i++){
			result[i] = m.get(i, col);
		}
		return result;
	}
	/**
	 * 把1维数组转换成Vec的类型
	 * @param data
	 * @return
	 */
	public static Vec from1DArray(double[] data){
		Vec result = new Vec(data.length);
		for (double d : data) {
			result.set(d);
		}
		return result;
	}
	
	public static void main(String[] args){
		double[][] data = {{1,2,3},{4,5,6}};
		Matrix m = new Matrix(data);
		Vec v = from1DArray(getCol(m, 2));
		doInnerOrth(m, 0.1);
		m.print(10, 4);
	}
	/**
	 * 对矩阵m做一次sweep来正交化矩阵
	 * @param block 矩阵m
	 * @param tol 误差系数
	 * @return 是否收敛
	 */
	private static boolean innerOrth(Matrix block, double tol) {
        Matrix uMatrix = block;
        boolean converged = true;

        //一次round-robin循环，一次sweep来正交化矩阵
        for(int i = 0; i < block.getColumnDimension() -1; i++) {
            for(int j = i+1; j < block.getColumnDimension(); j++) {
                Vec di = from1DArray(getCol(block, i));
                Vec dj = from1DArray(getCol(block, j));
                double dii = di.dot(di);
                double dij = di.dot(dj);
                double djj = dj.dot(dj);

                //如果存在两列之间的正交化结果大于误差值，则判定为按照该模展开的矩阵未收敛
                if (Math.abs(dij) > tol) {
                    converged = false;

                    double tao = (djj - dii) / (2 * dij);
                    double t = Math.signum(tao) / (Math.abs(tao) + Math.sqrt(Math.pow(tao, 2) + 1));
                    double c = 1.0 / Math.sqrt(Math.pow(t, 2) + 1);
                    double s = t * c;

                    //update data block
                    //乘以旋转矩阵
                    for(int k = 0; k < block.getRowDimension(); k++) {
                        double res1 = block.get(k, i) * c - block.get(k, j) * s;
                        double res2 = block.get(k, i) * s + block.get(k, j) * c;
                        uMatrix.set(k,i,res1);
                        uMatrix.set(k,j,res2);
                    }

                }
            }
        }
        return converged;
    }
	/**
	 * 对矩阵做jacobi正交化操作
	 * @param U
	 * @param tol
	 */
	public static USTuple doInnerOrth(Matrix U, double tol) {
		int maxSteps = 1000;
		List<Tuple> normVecList = null;
		USTuple result = null;

        for (int i = 0; i < maxSteps; i++) {
            boolean con = innerOrth(U, tol);
            if (con) {
            	//============================================//
//            	System.out.println("未归一化之前的矩阵：");
//            	U.print(10, 4);
//            	System.out.println("#############################################");
            	normVecList = new ArrayList<Tuple>();

                 for(int j = 0; j < U.getColumnDimension(); j++){
                	 Vec colVec = from1DArray(getCol(U, j));
                	 double norm = colVec.norm();
                	 normVecList.add(new Tuple(norm, colVec.divide(norm)));
                 }

                 //对存放Tuple的list进行排序，按照降序顺序
//                 normVecList.sort(new TupleComparator());
                 Collections.sort(normVecList,new TupleComparator());


                 for(int j = 0; j < U.getColumnDimension(); j++) {
                    for(int k = 0; k < U.getRowDimension(); k++){
                    	double data = normVecList.get(j).getVector().get(k);                   	
                    	U.set(k, j, data);
                    }
//                    System.out.println(normVecList.get(j).getNorm());
                 }
            	//=======================================================//
            	Matrix uMatrix = sliceByCol(U, 0, U.getColumnDimension()/2-1);
            	double[] sData = new double[U.getColumnDimension()/2];
            	for(int h = 0; h < sData.length; h++){
            		double data = normVecList.get(h).getNorm();
            		sData[h] = data;
            	}
            	Matrix sMatrix = from1DtoSMatrix(sData);
            	result = new USTuple(uMatrix, sMatrix);
            	
            	
                break;
            }
        }
        return result;
    }
	public static Matrix sliceByCol(Matrix m,int from, int to) {
        int max_col = Math.max(from,to);
        int min_col = Math.min(from, to);
        int slice_col = max_col - min_col + 1;
        Matrix result = new Matrix(m.getRowDimension(), slice_col);
        for(int i = 0; i < m.getRowDimension(); i++) {
            for(int j = 0; j < slice_col; j++) {
                result.set(i,j,m.get(i,j+min_col));
            }
        }
        return result;
    }
	
	public static Matrix from1DtoSMatrix(double[] data){
		int length = data.length;
		Matrix result = new Matrix(length, length);
		for(int i = 0; i < length; i++){
			for(int j = 0; j < length; j++){
				if(i == j)result.set(i, j, data[i]);
				else result.set(i, j, 0.0);
			}
		}
		return result;
	}
	
}
