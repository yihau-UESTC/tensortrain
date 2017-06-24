package tensortrain.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

import tensortrain.datatype.IPAndLevelTuple;

public class ActorLoadBalance implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> list;
	private static int count = 0;

	public ActorLoadBalance(ArrayList<String> list) {
		super();
		this.list = list;
	}
	
	public String get(){
		String host = "";
		if (count >= list.size()){
			count = 0;
		}
		host = list.get(count); 
		count ++;
		return host;
	}
	
	
	public static void main(String[] args){
		ArrayList<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");
		ActorLoadBalance alb = new ActorLoadBalance(list);
		System.out.println(alb.get());
		System.out.println(alb.get());
		System.out.println(alb.get());
		System.out.println(alb.get());
		System.out.println(alb.get());
		System.out.println(alb.get());
	}
}
