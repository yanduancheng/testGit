package itat.zttc.shop.web;

import itat.zttc.shop.dao.IAddressDao;
import itat.zttc.shop.dao.IUserDao;
import itat.zttc.shop.model.Address;
import itat.zttc.shop.model.ShopDi;
import itat.zttc.shop.model.User;
import itat.zttc.shop.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddressServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private IUserDao userDao;
	private IAddressDao addressDao;
	
	@ShopDi
	public void setAddressDao(IAddressDao addressDao) {
		this.addressDao = addressDao;
	}

	@ShopDi
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Auth
	public String add(HttpServletRequest req,HttpServletResponse resp) {
		User u = userDao.load(Integer.parseInt(req.getParameter("userId")));
		Address a = (Address)RequestUtil.setParam(Address.class, req);
		if(!RequestUtil.validate(Address.class, req)) {
			req.setAttribute("user",u);
			return "address/addInput.jsp";
		}
		addressDao.add(a, u.getId());
		return redirPath(req.getContextPath()+"/user.do?method=show&id="+u.getId());
	}
	
	@Auth
	public String delete(HttpServletRequest req,HttpServletResponse resp) {
		int addressId =Integer.parseInt(req.getParameter("id"));
		int userId = Integer.parseInt(req.getParameter("userId"));
		addressDao.delete(addressId);
		return redirPath(req.getContextPath()+"/user.do?method=show&id="+userId);
	}
	
	@Auth
	public String addInput(HttpServletRequest req,HttpServletResponse resp) {
		User u = userDao.load(Integer.parseInt(req.getParameter("userId")));
		req.setAttribute("user", u);
		return "address/addInput.jsp";
	}
	
	@Auth
	public String updateInput(HttpServletRequest req,HttpServletResponse resp) {
		Address address = addressDao.load(Integer.parseInt(req.getParameter("id")));
		req.setAttribute("address", address);
		return "address/updateInput.jsp";
	}
	
	@Auth
	public String update(HttpServletRequest req,HttpServletResponse resp) {
		Address address = addressDao.load(Integer.parseInt(req.getParameter("id")));
		Address ta = (Address)RequestUtil.setParam(Address.class, req);
		address.setName(ta.getName());
		address.setPhone(ta.getPhone());
		address.setPostcode(ta.getPostcode());
		if(!RequestUtil.validate(Address.class, req)) {
			req.setAttribute("address", address);
			return "address/updateInput.jsp";
		}
		addressDao.update(address);
		return redirPath("user.do?method=show&id="+address.getUser().getId());
	}
	
}
