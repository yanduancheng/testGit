package itat.zttc.shop.test;

import itat.zttc.shop.util.DaoUtil;

public class BaseTest {
	public BaseTest() {
		DaoUtil.diDao(this);
	}
}
