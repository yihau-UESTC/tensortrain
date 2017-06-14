package tensortrain.actor;

import Jama.Matrix;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import tensortrain.datatype.Tensor;
import tensortrain.message.ModifyMatrix;
import tensortrain.message.MoveUSTuple;
import tensortrain.message.OriginCalculateData;
import tensortrain.message.OrthoFinish;
import tensortrain.message.SendUMatrix;
import tensortrain.message.UMatrix;
import tensortrain.message.USTuple;
import tensortrain.utils.MyUtils;
/**
 * 
 * @author yihau
 * @date 2017年6月5日
 */
public class CalculationWorker extends UntypedActor{
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private Matrix originMatrix;
	private USTuple tuple;
	private int id;
	
	
	@Override
	public void preStart() throws Exception {
		// TODO Auto-generated method stub
//		log.info(getSelf().path() + "has been constructed !!!");
	}


	@Override
	public void onReceive(Object message) throws Exception {
		//第一次迭代接收到分块的张量。
		if(message instanceof OriginCalculateData){
//			log.info("origincalculatedata");
			OriginCalculateData data = (OriginCalculateData) message;
			Matrix matrix = data.getMatrix();
			this.id = data.getId();
			originMatrix = matrix.copy();
			//svd分解，然后将u，s装进tuple。
			tuple = MyUtils.svd(matrix);
			//将tuple发送给父节点partionworker
			getContext().parent().tell(new OrthoFinish(), getSelf());
		}else if(message instanceof MoveUSTuple){
			//移动tuple给合成节点
//			log.info("moveustuple");
			getSender().tell(tuple, ActorRef.noSender());
		}else if(message instanceof USTuple){
			//合成矩阵
//			log.info("ustuple");
			USTuple receivedTuple = (USTuple) message;
			Matrix u1 = tuple.getuMatrix();
			Matrix s1 = tuple.getsMatrix();
			Matrix u2 = receivedTuple.getuMatrix();
			Matrix s2 = receivedTuple.getsMatrix();
//			Matrix combinMatrix = MyUtils.add(u1.times(s1), u2.times(s2));
			tuple = MyUtils.doInnerOrth(u1.times(s1), u2.times(s2), 0.1);
			getContext().parent().tell(new OrthoFinish(), getSelf());
		}else if(message instanceof SendUMatrix){
			//将u矩阵回传给父节点。
//			log.info("sendumatrix");
			getSender().tell(new UMatrix(tuple.getuMatrix()), ActorRef.noSender());
		}else if(message instanceof UMatrix){
//			log.info("umatrix");
			UMatrix uMatrix = (UMatrix) message;
			Matrix matrix = uMatrix.getuMatrix();
			//接下来对u进行转置，然后乘以origin矩阵。
			Matrix transUMatrix = matrix.transpose();
			Matrix modifiyMatrix = transUMatrix.times(originMatrix);
			getContext().parent().tell(new ModifyMatrix(modifiyMatrix, id), getSelf());
		}
		
	}

}
