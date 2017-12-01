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
	//查询某个公司的所有微信应用的配置信息或全部公司
	public List<AccessToken> getListAccess(){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		List<AccessToken> list=new ArrayList<AccessToken>();
		AccessToken acc=null;
		String sql="select * from inswaplist";
//		if(scm!=null){
//			sql+=" where orgcode ='"+scm+"'";
//		}
		try{
			statement=connection.prepareStatement(sql);  
			resultSet=statement.executeQuery();
			while(resultSet.next()){
				acc=new AccessToken();
				acc.setApplyId(resultSet.getString("w_applyid"));
				acc.setCompanyId(resultSet.getString("orgcode"));
				acc.setCorpIDid(resultSet.getString("w_corpid"));
				acc.setSecret(resultSet.getString("w_appsecret"));
				String[] arr=this.geturl(resultSet.getString("w_corpid"));
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
	//根据w_corpid获取信息访问地址，信息来源地址
	public String[] geturl(String w_corgid){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String[] arr =null;
		String sql="select w_trusturl,serverurl from insorg where w_corpid=?";
		try{
			statement=connection.prepareStatement(sql);  
			statement.setString(1,w_corgid);
			resultSet=statement.executeQuery();
			if(resultSet.next()){
				 arr=new String[2];
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
	
//	//查询某个公司的微信配置信息  或者查询全部公司的微信配置信息
//	public List<AccessToken> getListAcc(String scm){
//		Connection connection=getConnection();
//		PreparedStatement statement=null;
//		ResultSet resultSet=null;
//		List<AccessToken> list=new ArrayList<AccessToken>();
//		AccessToken acc=null;
//		String sql="select * from insorg";
//		if(scm!=null){
//			sql+=" where orgcode ='"+scm+"'";
//		}
//		sql+="";
//		try{
//			statement=connection.prepareStatement(sql);  
//			resultSet=statement.executeQuery();
//			while(resultSet.next()){
//				acc=new AccessToken();
//				acc.setCompanyId(resultSet.getString("orgcode"));
//				acc.setCorpIDid(resultSet.getString("w_corpid"));
//				acc.setSecret(resultSet.getString("w_secret"));
//				acc.setServerurl(resultSet.getString("serverurl"));
//				acc.setDomainName(resultSet.getString("w_trusturl"));
//				list.add(acc);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			closeAll(connection, statement, resultSet);
//		}
//		return list;
//	}
	//查询公司微信配置信息是否存在  存在修改   不存在添加
	public String SelectScm(Insorg insorg){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String st="no";
		int row=0;
		String sql="select count(w_corpid) num from insorg where orgcode=? and c_corp=? and w_corpid=?";
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1,insorg.getOrgcode());
			statement.setString(2,insorg.getC_corp());
			statement.setString(3,insorg.getW_corpid());
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
		String sql="update  insorg set w_corpid=?,w_secret=?,w_trusturl=? ,serverurl=?  where orgcode=? and c_corp=? and w_corpid=?";
		int row=0;
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1,insorg.getW_corpid());
			statement.setString(2,insorg.getW_secret());
			statement.setString(3,insorg.getW_trusturl());
			statement.setString(4,insorg.getServerurl());
			statement.setString(5,insorg.getOrgcode());
			statement.setString(6,insorg.getC_corp());
			statement.setString(7,insorg.getW_corpid());
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
		String sql="INSERT INTO insorg (c_corp,orgcode,w_corpid,w_secret,w_trusturl,serverurl) VALUES (?,?,?,?,?,?);";
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1,insorg.getC_corp());
			statement.setString(2,insorg.getOrgcode());
			statement.setString(3,insorg.getW_corpid());
			statement.setString(4,insorg.getW_secret());
			statement.setString(5,insorg.getW_trusturl());
			statement.setString(6,insorg.getServerurl());
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
		String sql="select count(w_applyid) num from inswaplist where wapno=? and orgcode=? and w_corpid=?";
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1, list.getWapno());
			statement.setString(2, list.getOrgcode());
			statement.setString(3, list.getW_corpid());
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
		String sql="update  inswaplist set w_applyid=?,w_wapurl=?,w_appsecret=?,dbid=? where wapno=? and orgcode=? and w_corpid=?;";
		int row=0;
		try{
			statement=connection.prepareStatement(sql);
			statement.setString(1, list.getW_applyid());
			statement.setString(2, list.getW_wapurl());
			statement.setString(3, list.getW_appsecret());
			statement.setString(4, list.getDbid());
			statement.setString(5, list.getWapno());
			statement.setString(6, list.getOrgcode());
			statement.setString(7, list.getW_corpid()); 
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
		String sql="insert into  inswaplist  (orgcode,w_applyid,w_wapurl,wapname,wapno,w_corpid,w_appsecret,dbid) values(?,?,?,?,?,?,?,?)";
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
}