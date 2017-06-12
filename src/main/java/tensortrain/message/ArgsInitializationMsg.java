package tensortrain.message;

import java.io.Serializable;

import Jama.Matrix;

/**
 * 
 * @author yihau
 * @date 2017年6月5日
 */
public class ArgsInitializationMsg implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Matrix matrix;
	private int step;
	private int dim;
	
	public ArgsInitializationMsg(Matrix matrix, int step, int dim) {
		super();
		this.matrix = matrix;
		this.step = step;
		this.dim = dim;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getDim() {
		return dim;
	}

	public void setDim(int dim) {
		this.dim = dim;
	}
	
	
	
}
