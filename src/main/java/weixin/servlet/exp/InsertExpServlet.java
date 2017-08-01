package weixin.servlet.exp;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import weixin.connection.exp.OperateExp;
import weixin.connection.exp.OperateExpa;
import weixin.pojo.Exp;
import weixin.pojo.Expa;

public class InsertExpServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session=request.getSession();
			String corpid=(String) session.getAttribute("wxscmid");
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String sid_b="";//平台单据编号
			String sid_w="";//微信端单据编号
			
			String userid =request.getParameter("smake");//制单人，报销人
			String bxlb=request.getParameter("bxlb");//报销类别
			String p_bank=request.getParameter("p_bank");//收款人开户行
			String p_account=request.getParameter("p_account");//收款人账号
			String fcy=request.getParameter("fcy");//金额
			String hpdate=request.getParameter("hpdate");//日期
			String remark=request.getParameter("remark");//报销说明

			String[] idxno=request.getParameterValues("idxno");//发票项目
			String[] inv_no=request.getParameterValues("inv_no");//发票编号
			String[] deduction=request.getParameterValues("deduction");//可抵扣否
			String[] inv_type=request.getParameterValues("inv_type");//发票类别
			String[] sp_tax=request.getParameterValues("sp_tax");//特殊票别
			String[] addtaxrt=request.getParameterValues("addtaxrt");//增值税率
			String[] fcys=request.getParameterValues("fcys");//金额
			String[] addtax=request.getParameterValues("addtax");//增值税金
			String[] rmbhs=request.getParameterValues("rmbhs");//无税金额
			String[] remarks=request.getParameterValues("remarks");//摘要

			Exp exp=new Exp();
			Expa expa=null;

			exp.setBxlb(bxlb);
			exp.setFcy(Double.parseDouble(fcy=""!=null?"0":fcy));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			exp.setHpdate(sdf.parse(hpdate));
			exp.setMkdate(new Date());
			exp.setP_account(p_account);
			exp.setP_bank(p_bank);
			exp.setRemark(remark);
			exp.setSopr(userid);
			exp.setSmake(userid);
			exp.setState("0");
			exp.setSid_b("sss");
			exp.setCorpid(corpid);
			//保存报销主表
			OperateExp oexp=new OperateExp();
			sid_w=oexp.insertExp(exp);
			if(sid_w.equals("no")){
				
				return;
			}
			for(int i=0;i<idxno.length;i++){
				expa=new Expa();
				expa.setCorpid(corpid);
				expa.setCid((i+1)+"");
				expa.setSid_w(sid_w);
				expa.setAddtax(Double.parseDouble(addtax[i]=""!=null?"0":addtax[i]));
				expa.setAddtaxrt(Double.parseDouble(addtaxrt[i]=""!=null?"0":addtaxrt[i]));
				expa.setDeduction(deduction[i]);
				expa.setFcy(Double.parseDouble(fcys[i]=""!=null?"0":fcys[i]));
				expa.setIdxno(idxno[i]);
				expa.setInv_no(inv_no[i]);
				expa.setInv_type(inv_type[i]);
				expa.setRemark(remarks[i]);
				expa.setRmbhs(Double.parseDouble(rmbhs[i]=""!=null?"0":rmbhs[i]));
				expa.setSid_b(sid_b);
				expa.setSp_tax(sp_tax[i]);
				//保存报销子表
				OperateExpa oexpa=new OperateExpa();
				String str=oexpa.insertExpa(expa);
				if(str.equals("no")){
					
					return;
				}
			}
			response.sendRedirect("ExpServlet");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}