package tensortrain.message;

import java.io.Serializable;

import Jama.Matrix;
/**
 * 
 * @author yihau
 * @date 2017年6月11日
 */
public class ModifyMatrix implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Matrix matrix;
	private int id;
	public ModifyMatrix(Matrix matrix, int id) {
		super();
		this.matrix = matrix;
		this.id = id;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
