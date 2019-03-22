package itat.zttc.shop.web;

import itat.zttc.shop.model.User;
import itat.zttc.shop.util.DaoUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class BaseServlet extends HttpServlet {
	public final static String redirPath = "redirect:";
	private Map<String,String> errors = new HashMap<String,String>();

	/**
	 * 
	 */
	private static final long serialVersionUID = -1301171098203450232L;
	
	protected String redirPath(String path) {
		return redirPath+path;
	}
	
	protected Map<String,String> getErrors() {
		return errors;
	}
	
	protected boolean hasErrors() {
		if(errors!=null&&errors.size()>0) return true;
		return false;
	}
	
	protected String handleException(Exception e,HttpServletRequest req) {
		req.setAttribute("errorMsg",e.getMessage());
		return "inc/error.jsp";
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			errors.clear();
			req.setAttribute("errors", errors);
			if(ServletFileUpload.isMultipartContent(req)) {
				req = new MultipartWrapper(req);
			}
			DaoUtil.diDao(this);
			String method = req.getParameter("method");
			Method m = this.getClass().getMethod(method, HttpServletRequest.class,HttpServletResponse.class);
			//在 这里进行权限控制
			int flag = checkAuth(req,m,resp);
			if(flag==1) {
				resp.sendRedirect("user.do?method=loginInput");
				return;
			} else if(flag==2) {
				req.setAttribute("errorMsg", "你没有权限访问该功能");
				req.getRequestDispatcher("/WEB-INF/inc/error.jsp").forward(req, resp);
				return;
			}
			String path = (String)m.invoke(this, req,resp);
			if(path.startsWith(redirPath)) {
				String rp = path.substring(redirPath.length());
				resp.sendRedirect(rp);
			} else {
				req.getRequestDispatcher("/WEB-INF/"+path).forward(req, resp);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 返回一个int类型的值，如果是0表示可以成功访问，如果是1表示到登录页面，如果是2表示显示没有权限
	 */
	private int checkAuth(HttpServletRequest req, Method m,HttpServletResponse resp) {
		User lu = (User)req.getSession().getAttribute("loginUser");
		if(lu!=null&&lu.getType()==1) {
			//如果是管理员说明所有的功能都可以访问
			return 0;
		}
		if(!m.isAnnotationPresent(Auth.class)) {
			//没有Annotation说明该方法必须由超级管理员
			if(lu==null) {
				return 1;
			}else if(lu.getType()!=1) {
				return 2;
			}
		} else {
			Auth a = m.getAnnotation(Auth.class);
			String v = a.value();
			if(v.equals("any")) {
				return 0;
			} else if(v.equals("user")){
				if(lu==null)
					return 1;
				else return 0;
			}
		}
		return 0;
	}

}
