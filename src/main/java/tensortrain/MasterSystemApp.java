package tensortrain;

import java.util.ArrayList;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import tensortrain.actor.MasterWorker;
import tensortrain.datatype.IPAndLevelTuple;
import tensortrain.datatype.Tensor;

/**
 * 主控系统
 * @author yihau
 * @date 2017年6月3日
 */
public class MasterSystemApp{
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ActorSystem system = ActorSystem.create("MasterNodeApp", ConfigFactory.load().getConfig("MasterSys"));
		double[] data = {1,9,5,13,3,11,7,15,2,10,6,14,4,12,8,16};
		
//		double[] data = {1,5,3,7,2,6,4,8};
		Tensor tensor = new Tensor(4, 2,2,2,2);
		tensor.setData(data);
		ArrayList<String> list = new ArrayList<>();
		String ip1 = "192.168.1.119";
		String ip2 = "192.168.1.136";
		list.add(ip1);
		list.add(ip2);
		ActorRef actor = system.actorOf(Props.create(MasterWorker.class,list),"MasterActor");
		actor.tell(tensor, ActorRef.noSender());

	}
	}
