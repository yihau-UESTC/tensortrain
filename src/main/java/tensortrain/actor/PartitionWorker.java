package tensortrain.actor;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.collection.Iterator;
import scala.collection.immutable.Iterable;
import tensortrain.datatype.Tensor;
import tensortrain.message.ArgsInitializationMsg;
import tensortrain.message.MoveUSTuple;
import tensortrain.message.OrthoFinish;
import tensortrain.message.SendUMatrix;
import tensortrain.message.UMatrix;
/**
 * 
 * @author yihau
 * @date 2017年6月5日
 */
public class PartitionWorker extends UntypedActor{
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef mainThread = null;
	private Tensor originTensor;
	private int rank;
	private int numOfSubtensor;
	private ArrayList<Tensor> subTensors = null;
	private int oneMergeCount = 0;
	private ActorRef mergeDestActor = null;
	private int allMergeCount = 0;
	private int allMergeNum = 0;
	
	
	public int calculateMergeNum(int tensorBlocks){
		int queue = tensorBlocks;
		int result = tensorBlocks;
		while(queue != 1){
			int i = queue/2;
			int j = queue%2;
			result += i;
			queue -= i;
		}
		return result;
	}
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof ArgsInitializationMsg){
			ArgsInitializationMsg argsInitializationMsg = (ArgsInitializationMsg) message;
			int[] dims = argsInitializationMsg.getTensor().getDims();
			this.rank = argsInitializationMsg.getTensor().getRank();
			this.numOfSubtensor = dims[1];
			log.info("第一次切分的子张量个数：" + numOfSubtensor);
			
			subTensors = new ArrayList<Tensor>();
			for(int i = 0; i < numOfSubtensor; i++){
				Tensor tempTensor = originTensor.slice(2, i, i+1);
				subTensors.add(tempTensor);
			}
			
			this.allMergeNum = calculateMergeNum(numOfSubtensor);
			
			for(int i = 0; i < numOfSubtensor; i++){
			ActorRef actor = getContext().actorOf(Props.create(CalculationWorker.class), "calculationWorker"+i);
				actor.tell(subTensors.get(i), ActorRef.noSender());
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
		}
		
	}

}
