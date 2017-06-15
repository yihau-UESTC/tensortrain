package tensortrain;

import java.util.concurrent.PriorityBlockingQueue;

import tensortrain.datatype.IPAndLevelTuple;

public class Test3 {

	public static void main(String[] args) {
		PriorityBlockingQueue<IPAndLevelTuple> pbQueue = new PriorityBlockingQueue<>();
		
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		String ip3 = "192.168.1.3";
		pbQueue.put(new IPAndLevelTuple(2, ip1));
		pbQueue.put(new IPAndLevelTuple(3, ip2));
		pbQueue.put(new IPAndLevelTuple(4, ip3));
		System.out.println(pbQueue);

	}

}
