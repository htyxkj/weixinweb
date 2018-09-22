package weixin.connection.accessToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.pojo.AccessToken;
import weixin.pojo.Insorg;
import weixin.pojo.Inswaplist;
import weixin.util.BaseDao;

public class AccessTokenDo  extends BaseDao{
	private static Logger log = LoggerFactory.getLogger(AccessTokenDo.class);
	/**
	 * 查询全部公司微信配置信息
	 * @return
	 */
	public List<AccessToken> getListWxAccess(){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		List<AccessToken> list=new ArrayList<AccessToken>();
		AccessToken acc=null;
		String sql="select uuid,orgcode,w_corpid,w_applyid,w_appsecret,d_applyid,dbid from inswaplist";
		try{
			statement=connection.prepareStatement(sql);  
			resultSet=statement.executeQuery();
			while(resultSet.next()){
				acc=new AccessToken();
				acc.setCompanyId(resultSet.getString("orgcode"));//公司标识
				acc.setW_applyId(resultSet.getString("w_applyid"));//微信应用id
				acc.setW_corpIDid(resultSet.getString("w_corpid"));//微信企业号标识
				acc.setW_secret(resultSet.getString("w_appsecret"));//微信应用秘钥标识
				String[] arr=this.geturl(resultSet.getString("uuid"));
				acc.setDomainName(arr[0]);
				acc.setServerurl(arr[1]);
				acc.setDbid(resultSet.getString("dbid"));
				list.add(acc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return list;
	}
	/**
	 * 查询全部公司钉钉配置信息
	 * @return
	 */
	public List<AccessToken> getListDdAccess(){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		List<AccessToken> list=new ArrayList<AccessToken>();
		AccessToken acc=null;
		String sql="select uuid,orgcode,d_corpid,d_secret,d_trusturl,serverurl from insorg";
		try{
			statement=connection.prepareStatement(sql);  
			resultSet=statement.executeQuery();
			while(resultSet.next()){
				acc=new AccessToken();
				acc.setCompanyId(resultSet.getString("orgcode"));//公司标识
				acc.setD_corpIDid(resultSet.getString("d_corpid"));//微信企业号标识
				acc.setD_secret(resultSet.getString("d_secret"));//微信应用秘钥标识
				String arr=this.getdbid(resultSet.getString("uuid"));
				acc.setDomainName(resultSet.getString("d_trusturl"));
				acc.setServerurl(resultSet.getString("serverurl"));
				acc.setDbid(arr);
				list.add(acc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return list;
	}
	//根据uuid获取dbid
	public String getdbid(String uuid){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String  arr =null;
		String sql="select dbid from inswaplist where uuid=?";
		try{
			statement=connection.prepareStatement(sql);  
			statement.setString(1,uuid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){ 
				arr = resultSet.getString("dbid");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return arr;
	}
	//根据uuid获取信息访问地址，信息来源地址
	public String[] geturl(String uuid){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String[] arr =null;
		String sql="select w_trusturl,serverurl,d_corpid,d_secret from insorg where uuid=?";
		try{
			statement=connection.prepareStatement(sql);  
			statement.setString(1,uuid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				 arr=new String[4];
				 arr[0]=resultSet.getString("w_trusturl");
				 arr[1]=resultSet.getString("serverurl");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return arr;
	}
	//查询公司微信配置信息是否存在  存在修改   不存在添加
	public String SelectScm(Insorg insorg){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String st="no";
		int row=0;
		String sql="select count(w_corpid) num from insorg where orgcode=? and c_corp=? and (w_corpid=? or d_corpid = ?)";
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1,insorg.getOrgcode());
			statement.setString(2,insorg.getC_corp());
			statement.setString(3,insorg.getW_corpid());
			statement.setString(4,insorg.getD_corpid());
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				int num=resultSet.getInt("num");
				if(num==0){
					row=insertScm(insorg);
					if(row==-1)
						st="no";
					st="yes";
				}else{
					row=updateScm(insorg);
					if(row==-1)
						st="no";
					st="yes";
				}
			} 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return st;
	}
	//修改微信配置信息
	public int updateScm(Insorg insorg){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		String sql="update  insorg set w_corpid=?,w_secret=?,w_trusturl=?,d_corpid=?,d_secret=?,d_trusturl=? ,serverurl=?  where orgcode=? and c_corp=? and (w_corpid=? or d_corpid=? )";
		int row=0;
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1,insorg.getW_corpid());
			statement.setString(2,insorg.getW_secret());
			statement.setString(3,insorg.getW_trusturl());
			statement.setString(4,insorg.getD_corpid());
			statement.setString(5,insorg.getD_secret());
			statement.setString(6,insorg.getD_trusturl());
			statement.setString(7,insorg.getServerurl());
			statement.setString(8,insorg.getOrgcode());
			statement.setString(9,insorg.getC_corp());
			statement.setString(10,insorg.getW_corpid());
			statement.setString(11,insorg.getD_corpid());
			row=statement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			row=-1;
		}finally{
			closeAll(connection, statement, null);
		}
		return row;
	}
	//添加微信配置信息
	public int insertScm(Insorg insorg){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		int rows=0;
		String sql="INSERT INTO insorg (c_corp,orgcode,w_corpid,w_secret,w_trusturl,d_corpid,d_secret,d_trusturl,serverurl,uuid) VALUES (?,?,?,?,?,?,?,?,?,?);";
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1,insorg.getC_corp());
			statement.setString(2,insorg.getOrgcode());
			statement.setString(3,insorg.getW_corpid());
			statement.setString(4,insorg.getW_secret());
			statement.setString(5,insorg.getW_trusturl());
			statement.setString(6,insorg.getD_corpid());
			statement.setString(7,insorg.getD_secret());
			statement.setString(8,insorg.getD_trusturl());
			statement.setString(9,insorg.getServerurl());
			statement.setString(10,insorg.getUuid());
			rows=statement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			rows=-1;
		}finally{
			closeAll(connection, statement, null);
		}
		return rows;
	}
	//查找微信APP配置信息     存在进行修改  不存在进行添加
	public String SelectApp(Inswaplist list){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String st="";
		String sql="select count(w_applyid) num from inswaplist where wapno=? and orgcode=? and (w_corpid=? or d_applyid =?)";
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1, list.getWapno());
			statement.setString(2, list.getOrgcode());
			statement.setString(3, list.getW_corpid());
			statement.setString(4, list.getD_applyid());
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				int row=0;
				int num=resultSet.getInt("num");
				if(num==0){
					row=insertApp(list);
					if(row==-1)
						st="no";
					st="yes";
				}else{
					row=updateApp(list);
					if(row==-1)
						st="no";
					st="yes";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return st;
	}
	//修改微信APP配置信息
	public int updateApp(Inswaplist list){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		String sql="update  inswaplist set w_corpid=?,w_applyid=?,w_wapurl=?,w_appsecret=?,dbid=?,d_applyid=? where wapno=? and orgcode=? and (w_corpid=? or d_applyid=?);";
		int row=0;
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1, list.getW_corpid());
			statement.setString(2, list.getW_applyid());
			statement.setString(3, list.getW_wapurl());
			statement.setString(4, list.getW_appsecret());
			statement.setString(5, list.getDbid());
			statement.setString(6, list.getD_applyid());
			statement.setString(7, list.getWapno());
			statement.setString(8, list.getOrgcode());
			statement.setString(9, list.getW_corpid());
			statement.setString(10, list.getD_applyid());
			row=statement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			row=-1;
		}finally{
			closeAll(connection, statement, null);
		}
		return row;
	} 
	//添加微信APP配置信息
	public int insertApp(Inswaplist list){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		String sql="insert into  inswaplist  (orgcode,w_applyid,w_wapurl,wapname,wapno,w_corpid,w_appsecret,dbid,d_applyid,uuid) values(?,?,?,?,?,?,?,?,?,?)";
		int row=0;
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1, list.getOrgcode());
			statement.setString(2, list.getW_applyid());
			statement.setString(3, list.getW_wapurl());
			statement.setString(4, list.getWapname());
			statement.setString(5, list.getWapno());
			statement.setString(6, list.getW_corpid());
			statement.setString(7, list.getW_appsecret());
			statement.setString(8, list.getDbid());
			statement.setString(9, list.getD_applyid());
			statement.setString(10, list.getUuid());
			row=statement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			row=-1;
		}finally{
			closeAll(connection, statement, null);
		}
		return row;
	}
	//查询微信应用ID
	public String selectAPPID(String wxscmid,String wapno){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String sql="select w_applyid from Inswaplist where wapno=? and w_corpid=?";
		String id="";
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1, wapno);
			statement.setString(2, wxscmid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				id=resultSet.getString("w_applyid");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return id;
	}

	/**
	 * 查询钉钉应用logo
	 * @param corpid  钉钉企业标识
	 * @param appid   钉钉应用id
	 * @return
	 */
	public String selDDAppLogo(String corpid,String appid){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String uuidSQL = "select uuid from insorg where d_corpid =?"; 
		String uuid = "";	
		String sql="select d_logo from Inswaplist where uuid = ? and d_applyid=?";
		String d_logo="";
		try{
			statement=connection.prepareStatement(uuidSQL);
			statement.setString(1, corpid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				uuid=resultSet.getString("uuid");
			}
			statement=connection.prepareStatement(sql);
			statement.setString(1, uuid);
			statement.setString(2, appid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				d_logo=resultSet.getString("d_logo");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return d_logo;
	}
	
	/**
	 * 修改钉钉应用logo
	 * @param corpid  钉钉企业标识
	 * @param appid   钉钉应用id
	 * @return
	 */
	public void upDDAppLogo(String corpid,String appid,String logo){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String uuidSQL = "select uuid from insorg where d_corpid =?"; 
		String uuid = "";	
		String sql="update Inswaplist set d_logo = ? where uuid = ? and d_applyid=?";
		try{
			statement=connection.prepareStatement(uuidSQL);
			statement.setString(1, corpid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				uuid=resultSet.getString("uuid");
			}
			statement=connection.prepareStatement(sql);
			statement.setString(1, logo);
			statement.setString(2, uuid);
			statement.setString(3, appid);
			statement.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
	}

	/**
	 * 查询微信或钉钉应用对应的平台应用编码
	 * @param corpid
	 * @param appid
	 * @return
	 */
	public String selBipAppId(String corpid,String appid){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String uuidSQL = "select uuid from insorg where d_corpid =?"; 
		String uuid = "";	
		String sql="select wapno from Inswaplist where uuid = ? and d_applyid=?";
		String bipApp="";
		try{
			statement=connection.prepareStatement(uuidSQL);
			statement.setString(1, corpid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				uuid=resultSet.getString("uuid");
			}
			statement=connection.prepareStatement(sql);
			statement.setString(1, uuid);
			statement.setString(2, appid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				bipApp=resultSet.getString("wapno");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return bipApp;
	} 

	public String[] selCorpid(String d_corpid,String w_corpid){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String sql = "select d_corpid,w_corpid from insorg where (d_corpid =? or w_corpid=?)";
		String[] corpid = new String[2];
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1, d_corpid);
			statement.setString(2, w_corpid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				corpid[0] = resultSet.getString("d_corpid");
				corpid[1] = resultSet.getString("w_corpid");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return corpid;
	} 
}