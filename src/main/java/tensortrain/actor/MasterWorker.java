package tensortrain.actor;

import Jama.Matrix;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import tensortrain.datatype.Tensor;
import tensortrain.message.ArgsInitializationMsg;
import tensortrain.message.ComposeMatrix;
/**
 * 
 * @author yihau
 * @date 2017年6月12日
 */
public class MasterWorker extends UntypedActor{

	LoggingAdapter log = Logging.getLogger(getContext().system(), this); 
	private Tensor originTensor;
	private int currentStep;
	private int step;
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Tensor){
			//将初始张量展开
			this.originTensor = (Tensor) message;
			this.currentStep = 1;
			this.step = originTensor.getRank();
			ActorRef actor = getContext().actorOf(Props.create(SplitAndMergeWorker.class), 
					"SplitAndMergeWorker" + currentStep);
			Matrix matrix = originTensor.matricization();
			actor.tell(new ArgsInitializationMsg(matrix, currentStep, originTensor.getRank(), 
					originTensor.getDims()[currentStep]), ActorRef.noSender());
		}else if(message instanceof ComposeMatrix){
			//迭代一次后的矩阵
			ComposeMatrix composeMatrix = (ComposeMatrix) message;
			Matrix matrix = composeMatrix.getData();
//			System.out.println("======================");
//			matrix.print(10, 4);
			currentStep ++;
			if(currentStep < step){
				
				ActorRef actor = getContext().actorOf(Props.create(SplitAndMergeWorker.class), 
						"SplitAndMergeWorker" + currentStep);
				actor.tell(new ArgsInitializationMsg(matrix, currentStep, originTensor.getRank(),
						originTensor.getDims()[currentStep]), ActorRef.noSender());
			}else{
				System.out.println("第"+ step +"个TT核");
				matrix.print(10, 4);
			}
			
			
		}
		
	}

}
