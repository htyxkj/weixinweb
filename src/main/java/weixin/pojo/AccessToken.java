package weixin.pojo;

public class AccessToken {
    // 获取到的凭证  
    private String token;  
    // 凭证有效时间，单位：秒  
    private int expiresIn;
    private String companyId;//公司标识
    private String applyId;//应用id
	private String corpIDid;//企业号标识
	private String secret;//管理组标识
	private String domainName;//访问域名
	private String serverurl;/**来源服务器地址**/
    public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getToken() {  
        return token;  
    }  
    public void setToken(String token) {  
        this.token = token;  
    }  
    public int getExpiresIn() {  
        return expiresIn;  
    }  
    public void setExpiresIn(int expiresIn) {  
        this.expiresIn = expiresIn;  
    }  
    public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getCorpIDid() {
		return corpIDid;
	}
	public void setCorpIDid(String corpIDid) {
		this.corpIDid = corpIDid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getServerurl() {
		return serverurl;
	}
	public void setServerurl(String serverurl) {
		this.serverurl = serverurl;
	}
}
