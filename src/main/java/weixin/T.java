package weixin;

import java.util.Random;

public class T {
	public static String randomString(int i){
		Random ran=new Random(i);
		StringBuilder sb=new StringBuilder();
		for(int n=0;;n++){
			int k=ran.nextInt(27);
			if(k==0)
				break;
			sb.append((char)('`'+k));
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		System.out.println(randomString(-229985452)+randomString(-147909649));
		for(int i=0;i<100;i++){ 
			System.out.println(ofRandom(0,255));} 
	}
	private static char[] ofRandom(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
}