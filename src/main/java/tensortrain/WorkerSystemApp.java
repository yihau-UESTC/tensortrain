package tensortrain;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.kernel.Bootable;

/**
 * 从节点系统
 * @author yihau
 * @date 2017年6月3日
 */
public class WorkerSystemApp implements Bootable{
	private ActorSystem system;
	
	public void shutdown() {
		system.shutdown();
		
	}

	public void startup() {
		system = ActorSystem.create("WorkerSystem", 
				ConfigFactory.load().getConfig("WorkerSys"));
		System.out.println("Start WorkerSystem !");
		
	}

}
