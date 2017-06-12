package tensortrain.message;

import java.io.Serializable;

import Jama.Matrix;
/**
 * 
 * @author yihau
 * @date 2017年6月12日
 */
public class ComposeMatrix implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Matrix data;

	public ComposeMatrix(Matrix data) {
		super();
		this.data = data;
	}

	public Matrix getData() {
		return data;
	}

	public void setData(Matrix data) {
		this.data = data;
	}

	
}
