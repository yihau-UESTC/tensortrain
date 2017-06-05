package tensortrain.actor;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import tensortrain.datatype.Tensor;
import tensortrain.message.ArgsInitializationMsg;
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
			
			ActorRef actor = getContext().actorOf(Props.create(CalculationWorker.class), "");
		}
		
	}

}
