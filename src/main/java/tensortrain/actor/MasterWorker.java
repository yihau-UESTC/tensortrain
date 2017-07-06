package tensortrain.actor;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import Jama.Matrix;
import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Deploy;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.remote.RemoteScope;
import tensortrain.datatype.IPAndLevelTuple;
import tensortrain.datatype.Tensor;
import tensortrain.message.ArgsInitializationMsg;
import tensortrain.message.ComposeMatrix;
import tensortrain.utils.ActorLoadBalance;
/**
 * 
 * @author yihau
 * @date 2017年6月12日
 */
public class MasterWorker extends UntypedActor{
//	Logger logger = Logger.getLogger(MasterWorker.class);
	LoggingAdapter log = Logging.getLogger(getContext().system(), this); 
	private Tensor originTensor;
	private int currentStep;
	private int step;
	private ArrayList<String> list;
	private ActorLoadBalance alb;
	private long time;
	
	public MasterWorker(ArrayList<String> list, long time){
		this.list = list;
		alb = new ActorLoadBalance(list);
		this.time = time;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Tensor){
			//将初始张量展开
			this.originTensor = (Tensor) message;
			this.currentStep = 1;
			this.step = originTensor.getRank();
			//========================================================//
		
			
			String host = alb.get();
//			String host = "192.168.1.119";
			Address addr = new Address("akka.tcp", "WorkerSystem", host, 2555);
			
			ActorRef actor = getContext().actorOf(Props.create(SplitAndMergeWorker.class, this.alb)
					.withDeploy(new Deploy(new RemoteScope(addr))), 
					"SplitAndMergeWorker" + currentStep);
			
			Matrix matrix = originTensor.matricization();
			actor.tell(new ArgsInitializationMsg(matrix, currentStep, originTensor.getRank(), 
					originTensor.getDims()[currentStep]), ActorRef.noSender());
		}else if(message instanceof ComposeMatrix){
			//迭代一次后的矩阵
			ComposeMatrix composeMatrix = (ComposeMatrix) message;
			Matrix matrix = composeMatrix.getData();
//			matrix.print(10, 4);
			currentStep ++;
			if(currentStep < step){
//				ArrayList<IPAndLevelTuple> list = new ArrayList<>();
				String ip1 = "192.168.1.119";
//				String ip2 = "192.168.1.2";
//				String ip3 = "192.168.1.3";
//				list.add(new IPAndLevelTuple(0, ip1));
//				list.add(new IPAndLevelTuple(0, ip2));
//				list.add(new IPAndLevelTuple(0, ip3));
				String host = alb.get();
//				String host = ip1;
				Address addr = new Address("akka.tcp", "WorkerSystem", host, 2555);
				
				ActorRef actor = getContext().actorOf(Props.create(SplitAndMergeWorker.class,alb)
						.withDeploy(new Deploy(new RemoteScope(addr))), 
						"SplitAndMergeWorker" + currentStep);
				actor.tell(new ArgsInitializationMsg(matrix, currentStep, originTensor.getRank(),
						originTensor.getDims()[currentStep]), ActorRef.noSender());
			}else{
				System.out.println("第"+ step +"个TT核");
				matrix.print(10, 4);
				long endTime = System.currentTimeMillis();
				long usedTime = endTime - this.time;
				log.info("usedTime: ", usedTime);
			}
			
			
		}else if (message instanceof String){
			
		}
		
	}

}
