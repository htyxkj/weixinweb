package weixin;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对资源文件的修改，添加
 * @author zhao
 *
 */
public class T {
	public static void main(String[] args) {
		String scm="005";
		String sid= "BXC{A5}{Y2M}%";
		String[] arr=scm.split("");
		if(arr.length<5){
			for(int i=0;i<6-arr.length;i++){
				scm="0"+scm;
				System.out.println(scm);
			}
		}
		sid=sid.replaceAll("\\{A5\\}",scm);
		SimpleDateFormat sdf1=new SimpleDateFormat("yyMM");
		sid=sid.replaceAll("\\{Y2M\\}",sdf1.format(new Date()));
		System.out.println(sid+"");
		//BXC000051706%0001
		//BXC000051706
	}
}