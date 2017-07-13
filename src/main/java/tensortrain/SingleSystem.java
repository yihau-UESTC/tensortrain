package tensortrain;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.typesafe.config.ConfigFactory;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import tensortrain.actor.MasterWorker;
import tensortrain.datatype.Tensor;
import tensortrain.message.USTuple;
import tensortrain.utils.MyUtils;

public class SingleSystem {
	
	public static Tensor testReal() {
		double[] data = { 1, 5, 3, 7, 2, 6, 4, 8 };
		Tensor tensor = new Tensor(3, 2, 2, 2);
		tensor.setData(data);
		return tensor;
	}
	
	public static Tensor testRandom(){
		Random random = new Random(47);
		Scanner in = new Scanner(System.in);
		System.out.println("please input the order");
		int order = in.nextInt();
		System.out.println("you input number: " + order);
		int[] dims = new int[order];
		int size = 1;
		for (int i = 0; i < order; i++) {
			System.out.println("please input the " + i + " dim");
			dims[i] = in.nextInt();
			size = size * dims[i];
			System.out.println("you input number: " + dims[i]);
		}
		System.out.println("the tensor size is :" + size);
		double[] data = new double[size];
		Tensor tensor = new Tensor(order, dims);
		for (int i = 0; i < size; i++) {
			data[i] = random.nextDouble();
		}
		tensor.setData(data);
		return tensor;
	}
 

	public static void main(String[] args) {
//		Tensor tensor = testReal();
		Tensor tensor = testRandom();
		long start = System.currentTimeMillis();
		Matrix matrix = tensor.matricization();
		Matrix originMatrix = matrix.copy();
		matrix.print(10, 4);
		USTuple temp = null;
		for(int i = 0; i < tensor.getRank()-1; i++){
//			temp = MyUtils.svd(matrix);
			temp = MyUtils.doInnerOrth(matrix, 1e-7);
			System.out.println("第"+(i+1) +"个TT核：");
			temp.getuMatrix().print(10, 4);
			matrix = temp.getuMatrix().transpose().times(originMatrix);
			if(i != tensor.getRank()-2){
			ArrayList<Matrix> subMatrixs = new ArrayList<>();
			int distance = originMatrix.getColumnDimension()/tensor.getDims()[i+1];
			int colInitial = 0;
			int colFinal = distance - 1;
			int rowInitial = 0;
			int rowFinal = matrix.getRowDimension() - 1;
			for(int j = 0; j < tensor.getDims()[i+1]; j++){
				Matrix subMatrix = matrix.getMatrix(rowInitial, rowFinal, colInitial, colFinal);
				subMatrixs.add(subMatrix);
				colInitial = colFinal + 1;
				colFinal = colInitial + distance - 1;
			}
			for(int k = 0; k < subMatrixs.size(); k++){
				matrix = MyUtils.composeByRowMatrix(subMatrixs);
			}
			originMatrix = matrix.copy();
			}
		}
		System.out.println("第"+tensor.getRank() +"个TT核：");
		matrix.print(10, 4);
		long time = System.currentTimeMillis()-start;
		System.out.println(time/1000);
	}

}
