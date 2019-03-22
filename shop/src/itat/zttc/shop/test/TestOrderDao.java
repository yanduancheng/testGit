package itat.zttc.shop.test;

import itat.zttc.shop.dao.IOrdersDao;
import itat.zttc.shop.model.CartProduct;
import itat.zttc.shop.model.Orders;
import itat.zttc.shop.model.ShopDi;

import org.junit.Test;

public class TestOrderDao extends BaseTest {
	private IOrdersDao ordersDao;

	public IOrdersDao getOrdersDao() {
		return ordersDao;
	}
	@ShopDi
	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
	}
	
	@Test
	public void testLoad() {
		Orders o = ordersDao.load(7);
		System.out.println(o.getPrice()+","+o.getStatus()+","+o.getAddress().getName()+","+o.getUser().getNickname());
		for(CartProduct cp:o.getProducts()) {
			System.out.println(cp.getProduct().getName()+","+cp.getNumber());
		}
	}
}
