package tensortrain.message;

import tensortrain.datatype.Tensor;

/**
 * 
 * @author yihau
 * @date 2017年6月5日
 */
public class ArgsInitializationMsg {
	private Tensor tensor;

	public ArgsInitializationMsg(Tensor tensor) {
		super();
		this.tensor = tensor;
	}

	public Tensor getTensor() {
		return tensor;
	}

	public void setTensor(Tensor tensor) {
		this.tensor = tensor;
	}
	
	
}
