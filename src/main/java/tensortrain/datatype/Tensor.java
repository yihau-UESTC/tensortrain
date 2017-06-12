package tensortrain.datatype;

import Jama.Matrix;

/**
 * 
 * @author yihau
 * @date 2017年6月4日
 */
public class Tensor {
	//用一维数组来存储数据。
	private double[] data = null;
	//张量的阶数
	private int rank;
	//张量中所有元素个数
	private int len;
	//张量中的各阶上的维数
	private int dims[];
	//在一维数组中阶与阶之间相隔的元素个数,长度。
	private int distance[];
	
	/**
	 * tensor的构造器，完成rank,dims,len,distance的初始化工作。
	 * @param rank
	 * @param args
	 */
	public Tensor(int rank,int ...args){
		if(args.length != rank){
			throw new IllegalArgumentException("args is not compatible with rank");
		}
		this.rank = rank;
		this.dims = args;
		//计算张量元素的总个数
		len = 1;
		for(int dimLength : args){
			len *= dimLength;
		}
		this.data = new double[len];
		this.distance = new int[rank];
		//初始化distance数组
		for(int i = 0; i < rank-1; i++){
			int result = 1;
			for(int j = i+1; j < rank; j++){
				result *= dims[j];
			}
			distance[i] = result;
		}
		distance[rank-1] = 1;
	}
	
	public void setData(double[] data){
		this.data = data;
	}
	
	public double[] getData() {
		return data;
	}



	public int getRank() {
		return rank;
	}



	public int getLen() {
		return len;
	}



	public int[] getDims() {
		return dims;
	}



	public int[] getDistance() {
		return distance;
	}



	/**
	 * 根据下标数组获取元素在一维数组中的位置
	 * @param args
	 * @return
	 */
	public int getIndex(int[] args){
		int result = 0;
		for(int i = 0; i < rank; i++){
			result += distance[i]*args[i];
		}
		return result;
	}
	/**
	 * 根据下标数组获取对应的张量元素
	 * @param args
	 * @return
	 */
	public double get(int ...args){
		if(args.length != rank){
			throw new IllegalArgumentException("Args is not compatible with rank!");
		}
		return data[getIndex(args)];
	}
	/**
	 * 设置下标数组对应的元素为value给定的值。
	 * @param value
	 * @param args
	 */
	public void set(double value, int ...args){
		if(args.length != rank){
			throw new IllegalArgumentException("Args is not compatible with rank!");
		}
		data[getIndex(args)] = value;
	}
	/**
	 * 张量的模一展开，这里tt中只需要模一展开即可。
	 * @return  展开的矩阵
	 */
	public Matrix matricization() {
		//dims[0]模一的长度
		Matrix result = new Matrix(dims[0], len/dims[0]);
		int distance[] = new int[rank -1];
		//初始化展开矩阵中列之间各模之间的距离，列可能是I3I2排列的。
		for(int i = 0; i < rank-2; i++){
			int temp = 1;
			for(int j = i + 2; j < rank; j++){
				temp *= dims[j];
			}
			distance[i] = temp;
		}
		//变化最快的元素之间的距离是1
		distance[rank - 2] = 1;
		//position用来保存当前计算元素的位置。
		int position[] = new int[rank];
		
		recurUnfolding(1, 0, 0, distance, position, result);
		
		return result;
	}
	
	/**
	 * 递归处理每个模的展开情况，模拟rank个for循环。
	 * @param currentDim  当前计算的模
	 * @param handleDimCount  已经处理的模的个数
	 * @param col		列的值
	 * @param distance  列之间模与模之间的距离
	 * @param position  当前计算元素的位置
	 * @param result	结果矩阵
	 */
	private void recurUnfolding(int currentDim, int handleDimCount, int col, int[] distance, int[] position, Matrix result) {
		int tempCol = 0;
		//递归出口，当处理了rank-1个模的时候，只剩下最后一个模未处理。
		if(handleDimCount == rank - 1){
			for(int pos = 0; pos < dims[0]; pos++){
				position[0] = pos;
				result.set(pos, col, get(position));
			}
			return;
			//处理外层的rank-1个模
		}else{
			for(int pos = 0; pos < dims[currentDim]; pos++){
				position[currentDim] = pos;
				tempCol = distance[handleDimCount]*pos;
				tempCol += col;
				recurUnfolding(currentDim+1, handleDimCount+1, tempCol, distance, position, result);
			}
		}
		
	}
	/**
	 * 将张量在dim上切块，从start开始到end结束，不包括end，注意数组是从0开始的。
	 * @param dim 这个dim是1就是1模不包括0
	 * @param start
	 * @param end
	 * @return subTensor
	 */
	public Tensor slice(int dim, int start, int end){
		if(dim > rank){
			throw new IllegalArgumentException("Args is not compatible with rank");
		}
		int[] tempDims = dims.clone();
		int length = end - start;
		tempDims[dim -1] = length;
		Tensor result = new Tensor(rank, tempDims);
		int[] position = new int[rank];
		recurSlice(dim%rank, 0, start, position, result);
		return result;
	}
	
	/**
	 * 递归的对切块的子张量进行赋值。
	 * @param currentDim
	 * @param handleDimsCount
	 * @param start
	 * @param position
	 * @param result
	 */
	private void recurSlice(int currentDim, int handleDimsCount, int start, int[] position, Tensor result) {
		if(handleDimsCount == rank-1){
			for(int pos = 0; pos < result.dims[currentDim]; pos++){
				position[currentDim] = start + pos;
				double value = this.get(position);
				position[currentDim] = pos;
				result.set(value, position);
			}
			return;
		}else{
			for(int pos = 0; pos < dims[currentDim]; pos++){
				position[currentDim] = pos;
				recurSlice((currentDim+1) % rank, handleDimsCount + 1, start, position, result);
			}
		}
		
	}
	public static void main(String[] args) {
		int size = 2*2*3;
		Tensor t = new Tensor(3, 2,2,3);
		for(int i = 0; i < size; i++){
			t.data[i] = i+1;
		}
		Matrix m = t.matricization();
		m.print(5, 2);
		Tensor t1 = t.slice(2, 1, 2);
		t1.matricization().print(5, 2);

	}

}
