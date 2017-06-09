package tensortrain.message;

import java.io.Serializable;

import Jama.Matrix;
/**
 * 
 * @author yihau
 * @date 2017年6月9日
 */
public class UMatrix implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Matrix uMatrix;

	public UMatrix(Matrix uMatrix) {
		super();
		this.uMatrix = uMatrix;
	}

	public Matrix getuMatrix() {
		return uMatrix;
	}

	public void setuMatrix(Matrix uMatrix) {
		this.uMatrix = uMatrix;
	}
	
}
