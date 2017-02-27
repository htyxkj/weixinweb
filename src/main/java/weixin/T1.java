package weixin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import weixin.connection.message.ShowData;
import weixin.pojo.Message;
import weixin.pojo.PageInfo;

public class T1 {
	public static void main(String[] args) {
//		String  a="sdaaaaa@aaasgsdfsd";
//		System.out.println(a.indexOf("@"));
//		try {
//			Message msg=new Message();
//			if(msg.getAppid().equals("1"));
//		} catch (Exception ex) {
//			StackTraceElement stackTraceElement= ex.getStackTrace()[0];
//			System.out.println("File="+stackTraceElement.getFileName());
//			System.out.println("Line="+stackTraceElement.getLineNumber());
//			System.out.println("Method="+stackTraceElement.getMethodName());
//		}
		 try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
			 Date time=sdf.parse("2017-01-01");
			    Calendar cal = Calendar.getInstance();  
			    cal.setTime(time);  
			    // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
			    int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
			    if (1 == dayWeek) {  
			        cal.add(Calendar.DAY_OF_MONTH, -1);  
			    }
			    System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期  
			    cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
			    int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
			    cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
			    String imptimeBegin = sdf.format(cal.getTime());
			    System.out.println("所在周星期一的日期：" + imptimeBegin);
			    cal.add(Calendar.DATE, 2);
			    String imptimeMi = sdf.format(cal.getTime());
			    System.out.println("所在周星期三的日期：" + imptimeMi);
			    cal.add(Calendar.DATE, 2);
			    String imptimeEnd = sdf.format(cal.getTime());
			    System.out.println("所在周星期五的日期：" + imptimeEnd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}