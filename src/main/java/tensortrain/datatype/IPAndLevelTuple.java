package tensortrain.datatype;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class IPAndLevelTuple implements Comparable<IPAndLevelTuple>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int num;
	private String ip;
	public IPAndLevelTuple(int num, String ip) {
		super();
		this.num = num;
		this.ip = ip;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		return "IPAndLevelTuple [num=" + num + ", ip=" + ip + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		IPAndLevelTuple other = (IPAndLevelTuple) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		return true;
	}
	public static void main(String[] args){
		IPAndLevelTuple t1 = new IPAndLevelTuple(10, "DF");
		IPAndLevelTuple t2 = new IPAndLevelTuple(5, "AF");
		IPAndLevelTuple t3 = new IPAndLevelTuple(6, "CF");
		IPAndLevelTuple t4 = new IPAndLevelTuple(4, "BF");
		PriorityBlockingQueue<IPAndLevelTuple> p = new PriorityBlockingQueue<IPAndLevelTuple>();
		p.add(t1);
		p.add(t2);
		p.add(t3);
		p.add(t4);
		for(int i = 0; i < 10; i++){
			if(p.contains(new IPAndLevelTuple(0, "CF"))){
				IPAndLevelTuple temp = p.poll();
				System.out.println(temp);
			}
		}
		
	}
	@Override
	public int compareTo(IPAndLevelTuple o) {
		// TODO Auto-generated method stub
		return this.num > o.num ? 1 : this.num < o.num ? -1 : 0;
	}
	
}
