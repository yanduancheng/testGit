package itat.zttc.shop.util;

import itat.zttc.shop.dao.IFactoryDao;
import itat.zttc.shop.model.ShopDi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class DaoUtil {
	public static void main(String[] args) {
		System.out.println(createDaoFactory());
	}
	
	public static void diDao(Object obj) {
		try {
			/**
			 * 获取所有的方法
			 */
			Method[] ms = obj.getClass().getDeclaredMethods();
			for(Method m:ms) {
				/**
				 * 判断方法上面是否有ShopDi的Annotation，有这个Annotation才进行注入
				 */
				if(m.isAnnotationPresent(ShopDi.class)) {
					/*
					 * 获取method上的ShopDi对象
					 */
					ShopDi sd = m.getAnnotation(ShopDi.class);
					/**
					 * 获取这个Annotation的值
					 */
					String mn = sd.value();
					/**
					 * 判断shopDi的value是否为空，如果为空，就等于要使用setXXX这个方法名称
					 * 来完成注入
					 */
					if(mn==null||"".equals(mn.trim())) {
						mn = m.getName().substring(3);
						mn = mn.substring(0,1).toLowerCase()+mn.substring(1);
					}
					Object o = DaoUtil.createDaoFactory().getDao(mn);
					m.invoke(obj, o);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/*public static void diDao(Object obj) {
		try {
			
			 * 获取自己的所有方法，之后判断这些方法中是否有setXX的方法
			 * ，如果有就对这个方法进行对象的注入
			 
			Method[] ms = obj.getClass().getDeclaredMethods();
			for(Method m:ms) {
				
				 * 获取了方法来名称
				 
				String mn = m.getName();
				
				 * 判断这个方法是否是以set开头
				 
				if(mn.startsWith("set")) {
					
					 * 需要获取究竟要注入哪个dao
					 * 
					mn = mn.substring(3);
					mn = mn.substring(0,1).toLowerCase()+mn.substring(1);
					
					 * 调用工厂获取这个dao
					 
					Object o = DaoUtil.createDaoFactory().getDao(mn);
					
					 * 通过method来完成注入，m是一个setXXX的方法，调用者是this,参数是
					 * 上一段代码从工厂中取出的具体的XXX对象
					 * this.setXX(o)--m.invoke(this,o)
					 
					m.invoke(obj, o);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}*/
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static IFactoryDao createDaoFactory() {
		IFactoryDao f = null;
		try {
			Properties prop = PropertiesUtil.getDaoProp();
			String fs = prop.getProperty("factory");
			Class clz = Class.forName(fs);
			String mn = "getInstance";
			Method m = clz.getMethod(mn);
			f = (IFactoryDao)m.invoke(clz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return f;
	}
}
