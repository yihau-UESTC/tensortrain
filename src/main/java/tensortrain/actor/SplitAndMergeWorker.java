package tensortrain.actor;

import java.util.ArrayList;

import Jama.Matrix;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.collection.Iterator;
import scala.collection.immutable.Iterable;
import tensortrain.datatype.Tensor;
import tensortrain.message.ArgsInitializationMsg;
import tensortrain.message.ComposeMatrix;
import tensortrain.message.ModifyMatrix;
import tensortrain.message.MoveUSTuple;
import tensortrain.message.OriginCalculateData;
import tensortrain.message.OrthoFinish;
import tensortrain.message.SendUMatrix;
import tensortrain.message.UMatrix;
/**
 * 
 * @author yihau
 * @date 2017年6月5日
 */
public class SplitAndMergeWorker extends UntypedActor{
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef mainThread = null;
//	private Tensor originTensor;
//	private int rank;
////	private int numOfSubtensor;
//	private ArrayList<Tensor> subTensors = null;
	
	private Matrix originMatrix;
	private int step;
	private int numOfMatrix;
	private ArrayList<Matrix> subMatrixs = null;
	
	private int oneMergeCount = 0;
	private ActorRef mergeDestActor = null;
	private int allMergeCount = 0;
	private int allMergeNum = 0;
	private int resultCount = 0;
	private ArrayList<ModifyMatrix> modifyMatrixs = new ArrayList<ModifyMatrix>();
	
	
	
	
	public int calculateMergeNum(int tensorBlocks){
		int queue = tensorBlocks;
		int result = tensorBlocks;
		while(queue != 1){
			int i = queue/2;
//			int j = queue%2;
			result += i;
			queue -= i;
		}
		return result;
	}
	
	public Matrix composeMatrix(ArrayList<ModifyMatrix> list){
		int col = list.get(0).getMatrix().getColumnDimension();
		int row = 0;
		for (ModifyMatrix matrix : list) {
			row += matrix.getMatrix().getRowDimension();
		}
		Matrix composeMatrix = new Matrix(row, col);
		int rowInitial = 0;
		int rowFinal = 0;
		int colInitial = 0;
		int colFinal = col - 1;
		for(int i = 0; i < list.size(); i++){
			for(int j = 0; j < list.size(); j++){
				if(list.get(j).getId() == i){
					rowFinal = rowInitial + list.get(j).getMatrix().getRowDimension() - 1;
					composeMatrix.setMatrix(rowInitial, rowFinal, colInitial, colFinal, list.get(j).getMatrix());
					rowInitial = rowFinal + 1; 
				}
			}
			
		}
		return composeMatrix;
	}
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof ArgsInitializationMsg){
			//初始化参数
			ArgsInitializationMsg argsInitializationMsg = (ArgsInitializationMsg) message;
			this.originMatrix = argsInitializationMsg.getMatrix();
			this.step = argsInitializationMsg.getStep();
			this.numOfMatrix = argsInitializationMsg.getDim();
			log.info("第"+step+"次切分的矩阵个数：" + numOfMatrix);
			//切分矩阵
			this.subMatrixs = new ArrayList<Matrix>();
			int distance = originMatrix.getColumnDimension()/numOfMatrix;
			int colInitial = 0;
			int colFinal = distance - 1;
			int rowInitial = 0;
			int rowFinal = originMatrix.getRowDimension() - 1;
			for(int i = 0; i < numOfMatrix; i++){
				Matrix subMatrix = originMatrix.getMatrix(rowInitial, rowFinal, colInitial, colFinal);
				subMatrixs.add(subMatrix);
				colInitial = colFinal + 1;
				colFinal = colInitial + distance - 1;
			}
			//计算子矩阵两两合并的次数
			this.allMergeNum = calculateMergeNum(numOfMatrix);
			//发送子矩阵到对应的计算节点
			for(int i = 0; i < numOfMatrix; i++){
			ActorRef actor = getContext().actorOf(Props.create(CalculationWorker.class), "calculationWorker"+i);
				actor.tell(new OriginCalculateData(i, subMatrixs.get(i)), ActorRef.noSender());
			}
		}else if(message instanceof OrthoFinish){
			allMergeCount++;
			if (allMergeCount < allMergeNum) {
				oneMergeCount++;
				// mergeCount=2的时候，发消息给actor让他把ustuple发给mergedestactor,并且重置mergecount和mergeactor
				if (oneMergeCount == 2 && mergeDestActor != null) {
					getSender().tell(new MoveUSTuple(), mergeDestActor);
					oneMergeCount = 0;
					mergeDestActor = null;
				} else {// mergeCount=1的时候，只是记录下此actor为mergedestactor。
					mergeDestActor = getSender();
				}
			} else {
				// 分块组合完成开始求解SV
				getSender().tell(new SendUMatrix(), getSelf());
			}
		}else if(message instanceof UMatrix){
			UMatrix matrix = (UMatrix) message;
			Iterable<ActorRef> iterable = getContext().children();
			Iterator<ActorRef> actorRefs = iterable.iterator();
			while(actorRefs.hasNext()){
				ActorRef oneActor = actorRefs.next();
				oneActor.tell(matrix, ActorRef.noSender());
			}
		}else if(message instanceof ModifyMatrix){
			//组合矩阵，然后发送给父节点开始下一次迭代
			log.info(getSender().path() + " received modigyMatrix");
			ModifyMatrix data = (ModifyMatrix) message;
				resultCount ++;
				modifyMatrixs.add(data);
			if (resultCount >= numOfMatrix) {
				Matrix composeMatrix = composeMatrix(modifyMatrixs);
				getContext().parent().tell(new ComposeMatrix(composeMatrix), ActorRef.noSender());
			}
		}
		
		
	}

}