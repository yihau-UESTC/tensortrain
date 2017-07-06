package tensortrain;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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
		
//		double[] data = {1,9,5,13,3,11,7,15,2,10,6,14,4,12,8,16};
		boolean start = false;
		
		Random random = new Random(47);
		Scanner in = new Scanner(System.in);
		System.out.println("please input the order");
		int order = in.nextInt();
		System.out.println("you input number: "+order );
		int[] dims = new int[order];
		int size = 1;
		for(int i = 0; i < order; i++){
			System.out.println("please input the "+ i +" dim");
			dims[i] = in.nextInt();
			size = size*dims[i];
			System.out.println("you input number: "+dims[i]);
		}
		System.out.println("the tensor size is :"+ size);
		double[] data = new double[size];
//		double[] data = {1,5,3,7,2,6,4,8};
//		Tensor tensor = new Tensor(6, 4,4,4,4,4,4);
		Tensor tensor = new Tensor(order,dims);
		for(int i = 0; i < size; i++){
			data[i] = random.nextDouble();
		}
		tensor.setData(data);
		ArrayList<String> list = new ArrayList<>();
		int pc = 0;
		System.out.println("please input the pc's number: ");
		pc = in.nextInt();
		System.out.print("you input: "+ pc);
		int[] pcs = new int[pc];
		for(int i = 0; i < pc; i++){
			System.out.println("please input the pc " + i + "ip:");
			pcs[i] = in.nextInt();
			System.out.println("you input: "+pcs[i]);
			list.add("192.168.1."+pcs[i]);
		}
		start = true;
		
//		String ip1 = "192.168.1.109";
//		String ip2 = "192.168.1.136";
//		list.add(ip1);
//		list.add(ip2);
		if(start){
			long startTime = System.currentTimeMillis();
			ActorSystem system = ActorSystem.create("MasterNodeApp", ConfigFactory.load().getConfig("MasterSys"));
		ActorRef actor = system.actorOf(Props.create(MasterWorker.class,list, startTime),"MasterActor");
		actor.tell(tensor, ActorRef.noSender());
		
		}

	}
	}
