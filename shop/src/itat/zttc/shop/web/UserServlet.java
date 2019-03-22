package itat.zttc.shop.web;

import itat.zttc.shop.dao.IUserDao;
import itat.zttc.shop.model.Pager;
import itat.zttc.shop.model.ShopDi;
import itat.zttc.shop.model.ShopException;
import itat.zttc.shop.model.User;
import itat.zttc.shop.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private IUserDao userDao;

	@ShopDi
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public String list(HttpServletRequest req,HttpServletResponse resp) {
		Pager<User> users = userDao.find("");
		req.setAttribute("users", users);
		return "user/list.jsp";
	}
	@Auth("any")
	public String addInput(HttpServletRequest req,HttpServletResponse resp) {
		return "user/addInput.jsp";
	}
	
	public String delete(HttpServletRequest req,HttpServletResponse resp) {
		int id = Integer.parseInt(req.getParameter("id"));
		userDao.delete(id);
		return redirPath+"user.do?method=list";
	}
	
	public String updateInput(HttpServletRequest req,HttpServletResponse resp) {
		int id = Integer.parseInt(req.getParameter("id"));
		User u = userDao.load(id);
		req.setAttribute("user", u);
		return "user/updateInput.jsp";
	}
	
	public String changeType(HttpServletRequest req,HttpServletResponse resp) {
		int id = Integer.parseInt(req.getParameter("id"));
		User u = userDao.load(id);
		if(u.getType()==0) {
			u.setType(1);
		} else {
			u.setType(0);
		}
		userDao.update(u);
		return redirPath+"user.do?method=list";
	}
	
	public String update(HttpServletRequest req,HttpServletResponse resp) {
		User tu = (User)RequestUtil.setParam(User.class, req);
		boolean isValidate = RequestUtil.validate(User.class, req);
		int id = Integer.parseInt(req.getParameter("id"));
		User user = userDao.load(id);
		user.setNickname(tu.getNickname());
		if(!isValidate) {
			req.setAttribute("user", user);
			return "user/updateInput.jsp";
		}
		user.setPassword(tu.getPassword());
		user.setNickname(tu.getNickname());
		userDao.update(user);
		return redirPath+"user.do?method=list";
	}
	@Auth("any")
	public String add(HttpServletRequest req,HttpServletResponse resp) {
		User u = (User)RequestUtil.setParam(User.class,req);
		boolean isValidate = RequestUtil.validate(User.class, req);
		if(!isValidate) {
			return "user/addInput.jsp";
		}
//		Map<String,String> errors = new HashMap<String, String>();
//		req.setAttribute("errors", errors);
//		if(u.getUsername()==null||"".equals(u.getUsername())) {
//			errors.put("username","用户名不能为空");
//			return "user/addInput.jsp";
//		}
		try {
			userDao.add(u);
		} catch (ShopException e) {
			req.setAttribute("errorMsg",e.getMessage());
			return "inc/error.jsp";
		}
		/*try {
			Map<String,String[]> params = req.getParameterMap();
			Set<String> keys = params.keySet();
			User u = new User();
			for(String key:keys) {
				String mk = "set"+key.substring(0,1).toUpperCase()+key.substring(1);
				Method m;
				try {
					m = u.getClass().getMethod(mk,String.class);
					m.invoke(u, params.get(key)[0]);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					continue;
				}
			}
			System.out.println(u);
			userDao.add(u);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}  catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}*/
//		String username = req.getParameter("username");
//		String password = req.getParameter("password");
//		String nickname = req.getParameter("nickname");
//		u.setNickname(nickname);
//		u.setUsername(username);
//		u.setPassword(password);
		return redirPath("user.do?method=list");
	}
	
	@Auth("any")
	public String loginInput(HttpServletRequest req,HttpServletResponse resp) {
		return "user/loginInput.jsp";
	}
	
	@Auth("any")
	public String login(HttpServletRequest req,HttpServletResponse resp) {
		try {
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			User u = userDao.login(username, password);
			req.getSession().setAttribute("loginUser", u);
		} catch (ShopException e) {
			req.setAttribute("errorMsg",e.getMessage());
			return "inc/error.jsp";
		}
		return redirPath("product.do?method=list");
	}
	
	@Auth("any")
	public String logout(HttpServletRequest req,HttpServletResponse resp) {
		req.getSession().invalidate();
		return redirPath("product.do?method=list");
	}
	
	@Auth
	public String updateSelfInput(HttpServletRequest req,HttpServletResponse resp) {
		req.setAttribute("user", (User)req.getSession().getAttribute("loginUser"));
		return "user/updateSelfInput.jsp";
	}
	
	@Auth
	public String updateSelf(HttpServletRequest req,HttpServletResponse resp) {
		User tu = (User)RequestUtil.setParam(User.class, req);
		boolean isValidate = RequestUtil.validate(User.class, req);
		User user = (User)req.getSession().getAttribute("loginUser");
		user.setPassword(tu.getPassword());
		user.setNickname(tu.getNickname());
		if(!isValidate) {
			req.setAttribute("user", user);
			return "user/updateSelfInput.jsp";
		}
		userDao.update(user);
		return redirPath("goods.do?method=list");
	}
	
	@Auth
	public String show(HttpServletRequest req,HttpServletResponse resp) {
		User user = userDao.load(Integer.parseInt(req.getParameter("id")));
		req.setAttribute("user", user);
		return "user/show.jsp";
	}
}
