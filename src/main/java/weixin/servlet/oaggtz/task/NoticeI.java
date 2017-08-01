package weixin.servlet.oaggtz.task;

public interface NoticeI extends Runnable {
	public void init(String corpid,String appid,String scm);
}
