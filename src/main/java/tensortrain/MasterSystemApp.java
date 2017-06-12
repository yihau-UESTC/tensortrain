package tensortrain;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;
import tensortrain.actor.MasterWorker;
import tensortrain.actor.SplitAndMergeWorker;
import tensortrain.datatype.Tensor;
import tensortrain.message.ArgsInitializationMsg;

/**
 * 主控系统
 * @author yihau
 * @date 2017年6月3日
 */
public class MasterSystemApp{
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ActorSystem system = ActorSystem.create("MasterSystem");
		double[] data = {1,5,3,7,2,6,4,8};
		Tensor tensor = new Tensor(3, 2,2,2);
		tensor.setData(data);
		ActorRef actor = system.actorOf(Props.create(MasterWorker.class),"MasterActor");
		actor.tell(tensor, ActorRef.noSender());

	}
}
