package tensortrain.actor;

import Jama.Matrix;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import tensortrain.datatype.Tensor;
import tensortrain.message.MoveUSTuple;
import tensortrain.message.OrthoFinish;
import tensortrain.message.USTuple;
import tensortrain.utils.MyUtils;
/**
 * 
 * @author yihau
 * @date 2017年6月5日
 */
public class CalculationWorker extends UntypedActor{

	private USTuple tuple;
	@Override
	public void onReceive(Object message) throws Exception {
		//第一次迭代接收到分块的张量。
		if(message instanceof Tensor){
			Tensor tensor = (Tensor) message;
			//张量展开
			Matrix matrix = tensor.matricization();
			//svd分解，然后将u，s装进tuple。
			tuple = MyUtils.svd(matrix);
			//将tuple发送给父节点partionworker
			getContext().parent().tell(new OrthoFinish(), getSelf());
		}else if(message instanceof MoveUSTuple){
			getSender().tell(tuple, ActorRef.noSender());
		}else if(message instanceof USTuple){
			USTuple receivedTuple = (USTuple) message;
			Matrix u1 = tuple.getuMatrix();
			Matrix s1 = tuple.getsMatrix();
			Matrix u2 = receivedTuple.getuMatrix();
			Matrix s2 = receivedTuple.getsMatrix();
			Matrix combinMatrix = MyUtils.add(u1.times(s1), u2.times(s2));
			MyUtils.doInnerOrth(combinMatrix, 0.1);
			//做完正交化，接下来得到合成矩阵的U矩阵。
			
		}
		
	}

}
