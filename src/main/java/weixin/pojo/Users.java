package weixin.pojo;

public class Users {
	private String userid;
	private String username;
	private String tel;
	private String scm;
	private String email;
	private String w_corpid;
	private String imgurl="img/ren.png";
	private String status;//关注企业号状态
	private String exp_account;//收款人账号
	private String exp_bank;//收款人开户行
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getScm() {
		return scm;
	}
	public void setScm(String scm) {
		this.scm = scm;
	}
	public String getW_corpid() {
		return w_corpid;
	}
	public void setW_corpid(String w_corpid) {
		this.w_corpid = w_corpid;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExp_account() {
		return exp_account;
	}
	public void setExp_account(String exp_account) {
		this.exp_account = exp_account;
	}
	public String getExp_bank() {
		return exp_bank;
	}
	public void setExp_bank(String exp_bank) {
		this.exp_bank = exp_bank;
	}
}