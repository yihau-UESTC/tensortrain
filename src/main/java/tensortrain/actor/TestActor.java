package tensortrain.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class TestActor extends UntypedActor{

	@Override
	public void onReceive(Object arg0) throws Exception {
		if(arg0 instanceof String){
			getSender().tell("fff", ActorRef.noSender());
		}
		
	}

}
