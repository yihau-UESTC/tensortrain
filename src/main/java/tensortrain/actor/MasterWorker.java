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
			this.originTensor = (Tensor) message;
			this.currentStep = 1;
			this.step = originTensor.getRank() - 1;
			ActorRef actor = getContext().actorOf(Props.create(SplitAndMergeWorker.class), 
					"SplitAndMergeWorker" + currentStep);
			Matrix matrix = originTensor.matricization();
			actor.tell(new ArgsInitializationMsg(matrix, currentStep, 
					originTensor.getDims()[currentStep]), ActorRef.noSender());
		}else if(message instanceof ComposeMatrix){
			ComposeMatrix composeMatrix = (ComposeMatrix) message;
			Matrix matrix = composeMatrix.getData();
//			System.out.println("==========composeMatrix============");
//			matrix.print(10, 4);
			currentStep ++;
			if(currentStep <= step){
				
				ActorRef actor = getContext().actorOf(Props.create(SplitAndMergeWorker.class), 
						"SplitAndMergeWorker" + currentStep);
				actor.tell(new ArgsInitializationMsg(matrix, currentStep,
						originTensor.getDims()[currentStep]), ActorRef.noSender());
			}else{
				//迭代完成
			}
			
			
		}
		
	}

}
