package tensortrain;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.kernel.Bootable;

/**
 * 主控系统
 * @author yihau
 * @date 2017年6月3日
 */
public class MasterSystemApp implements Bootable{
	private ActorSystem system;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	public void startup() {
		system = ActorSystem.create("MasterSystem"
				,ConfigFactory.load().getConfig("MasterSys"));
	}

}
