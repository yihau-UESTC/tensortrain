package tensortrain.message;

import java.io.Serializable;

import Jama.Matrix;
/**
 * 
 * @author yihau
 * @date 2017年6月6日
 */
public class USTuple implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Matrix uMatrix;
	private Matrix sMatrix;
	
	public USTuple(){
		uMatrix = new Matrix(1, 1);
		sMatrix = new Matrix(1, 1);
	}
	
	public USTuple(Matrix uMatrix, Matrix sMatrix){
		this.uMatrix = uMatrix;
		this.sMatrix = sMatrix;
	}
	public Matrix getuMatrix() {
		return uMatrix;
	}
	public void setuMatrix(Matrix uMatrix) {
		this.uMatrix = uMatrix;
	}
	public Matrix getsMatrix() {
		return sMatrix;
	}
	public void setsMatrix(Matrix sMatrix) {
		this.sMatrix = sMatrix;
	}
	
	
}
