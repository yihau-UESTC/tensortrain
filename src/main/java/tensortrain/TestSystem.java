package tensortrain;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import tensortrain.actor.TestActor;

public class TestSystem {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("test");
		ActorRef actor = system.actorOf(Props.create(TestActor.class), "no.1");
		actor.tell(null,ActorRef.noSender());
		
	}

}
