package weixin.pojo;

public class Insorg {
	private String c_corp;/**集团编码**/
	private String orgcode;/**公司编码**/
//	private String corpid;/**企业编号**/
	private String w_corpid;/**微信企业号ID**/
	private String w_secret;/**secret序列号**/
	private String w_trusturl;/**移动端地址串**/
	private String serverurl;/**信息来源地址**/
	public String getOrgcode() {
		return orgcode;
	}
	public String getC_corp() {
		return c_corp;
	}
	public void setC_corp(String c_corp) {
		this.c_corp = c_corp;
	}
	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
//	public String getCorpid() {
//		return corpid;
//	}
//	public void setCorpid(String corpid) {
//		this.corpid = corpid;
//	}
	public String getW_corpid() {
		return w_corpid;
	}
	public void setW_corpid(String w_corpid) {
		this.w_corpid = w_corpid;
	}
	public String getW_secret() {
		return w_secret;
	}
	public void setW_secret(String w_secret) {
		this.w_secret = w_secret;
	}
	public String getW_trusturl() {
		return w_trusturl;
	}
	public void setW_trusturl(String w_trusturl) {
		this.w_trusturl = w_trusturl;
	}
	public String getServerurl() {
		return serverurl;
	}
	public void setServerurl(String serverurl) {
		this.serverurl = serverurl;
	}
}
