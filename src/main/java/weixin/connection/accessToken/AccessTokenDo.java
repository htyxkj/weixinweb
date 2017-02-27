package weixin.connection.accessToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import weixin.pojo.AccessToken;
import weixin.pojo.Insorg;
import weixin.pojo.Inswaplist;
import weixin.util.BaseDao;

public class AccessTokenDo  extends BaseDao{
	//查询某个公司的微信配置信息  或者查询全部公司的微信配置信息
	public List<AccessToken> getListAcc(String scm){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		List<AccessToken> list=new ArrayList<AccessToken>();
		AccessToken acc=null;
		String sql="select * from insorg";
		if(scm!=null){
			sql+=" where orgcode ='"+scm+"'";
		}
		sql+="";
		try{
			statement=connection.prepareStatement(sql);  
			resultSet=statement.executeQuery();
			while(resultSet.next()){
				acc=new AccessToken();
				acc.setCompanyId(resultSet.getString("orgcode"));
				acc.setCorpIDid(resultSet.getString("w_corpid"));
				acc.setSecret(resultSet.getString("w_secret"));
				acc.setServerurl(resultSet.getString("serverurl"));
				acc.setDomainName(resultSet.getString("w_trusturl"));
				list.add(acc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(connection, statement, resultSet);
		}
		return list;
	}
	//查询公司微信配置信息是否存在  存在修改   不存在添加
	public String SelectScm(Insorg insorg){
		Connection connection=getConnection();
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		String st="no";
		int row=0;
		String sql="select count(w_corpid) num from insorg where orgcode='"+insorg.getOrgcode()+"' and c_corp='"+insorg.getC_corp()+"' and serverurl='"+insorg.getServerurl()+"'";
		try{
			statement=connection.prepareStatement(sql);
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
		String sql="update  insorg set w_corpid='"+insorg.getW_corpid()+"',w_secret='"+insorg.getW_secret()+"',w_trusturl='"+insorg.getW_trusturl()+"' ,serverurl='"+insorg.getServerurl()+"'  where orgcode='"+insorg.getOrgcode()+"' and c_corp='"+insorg.getC_corp()+"'";
		int row=0;
		try{
			statement=connection.prepareStatement(sql);
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
		String sql="INSERT INTO insorg (c_corp,orgcode,w_corpid,w_secret,w_trusturl,serverurl) VALUES ('"+insorg.getC_corp()+"','"+insorg.getOrgcode()+"','"+insorg.getW_corpid()+"','"+insorg.getW_secret()+"','"+insorg.getW_trusturl()+"','"+insorg.getServerurl()+"');";
		try{
			statement=connection.prepareStatement(sql);
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
		String sql="select count(wapname) num from inswaplist where wapno='"+list.getWapno()+"' and orgcode='"+list.getOrgcode()+"';";
		try{
			statement=connection.prepareStatement(sql);
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
		String sql="update  inswaplist set w_applyid='"+list.getW_applyid()+"',w_wapurl='"+list.getW_wapurl()+"' where wapno='"+list.getWapno()+"' and orgcode='"+list.getOrgcode()+"';";
		int row=0;
		try{
			statement=connection.prepareStatement(sql);
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
		String sql="insert into  inswaplist  (orgcode,w_applyid,w_wapurl,wapname,wapno) values('"+list.getOrgcode()+"','"+list.getW_applyid()+"','"+list.getW_wapurl()+"','"+list.getWapname()+"','"+list.getWapno()+"')";
		int row=0;
		try{
			statement=connection.prepareStatement(sql);
			row=statement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			row=-1;
		}finally{
			closeAll(connection, statement, null);
		}
		return row;
	} 
}