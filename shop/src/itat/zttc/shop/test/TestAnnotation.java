package itat.zttc.shop.test;

import itat.zttc.shop.model.ShopDi;

import java.lang.reflect.Method;

import org.junit.Test;

public class TestAnnotation {
	
	@ShopDi("addressDao")
	public void aa() {
		
	}
	
	@ShopDi
	public void setUserDao() {
		
	}
	
	@Test
	public void test01() {
		Method [] ms = this.getClass().getDeclaredMethods();
		for(Method m:ms) {
			System.out.println(m.getName()+":"+m.isAnnotationPresent(ShopDi.class));
			if(m.isAnnotationPresent(ShopDi.class)) {
				ShopDi sd = m.getAnnotation(ShopDi.class);
				String v = sd.value();
				if(v==null||"".equals(v.trim())) {
					v = m.getName().substring(3);
				}
				System.out.println(v);
			}
		}
	}
}
