package itat.zttc.shop.dao;

import itat.zttc.shop.model.CartProduct;

public interface ICartProductDao {
	public void add(CartProduct cp,int oid);
	public void delete(int oid);
}
