package tensortrain.message;

import java.io.Serializable;

import Jama.Matrix;
/**
 * 
 * @author yihau
 * @date 2017年6月11日
 */
public class OriginCalculateData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private Matrix matrix;
	public OriginCalculateData(int id, Matrix matrix) {
		super();
		this.id = id;
		this.matrix = matrix;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Matrix getMatrix() {
		return matrix;
	}
	public void setTensor(Matrix matrix) {
		this.matrix = matrix;
	}
	
	
	
}
