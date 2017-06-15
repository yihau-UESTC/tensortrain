package tensortrain.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

import tensortrain.datatype.IPAndLevelTuple;

public class ActorLoadBalance {
	private ArrayList<IPAndLevelTuple> list;

	public ActorLoadBalance(ArrayList<IPAndLevelTuple> list) {
		super();
		this.list = list;
		Collections.sort(list);
	}
	
	public String get(){
		IPAndLevelTuple temp = list.get(0);
		String host = temp.getIp();
		temp.setNum(temp.getNum() + 1);
		Collections.sort(list);
		return host;
	}
	
	public void set(String host){
		Iterator<IPAndLevelTuple> iter = list.iterator();
		while(iter.hasNext()){
			IPAndLevelTuple temp = iter.next();
			if(temp.getIp().equals(host) && temp.getNum() > 0){
				temp.setNum(temp.getNum() + 1);				
			}
		}
		Collections.sort(list);
	}
	
	public static void main(String[] args){
		ArrayList<IPAndLevelTuple> list = new ArrayList<>();
		list.add(new IPAndLevelTuple(2, "A"));
		list.add(new IPAndLevelTuple(5, "B"));
		list.add(new IPAndLevelTuple(3, "C"));
		ActorLoadBalance ac = new ActorLoadBalance(list);
		String s = ac.get();
		System.out.println(s);
		System.out.println(ac.list);
		ac.set("C");
		System.out.println(ac.list);
	}
}
