package itat.zttc.shop.dao;

import itat.zttc.shop.model.Category;

import java.util.List;

public interface ICategoryDao {
	public void add(Category category);
	public void delete(int id);
	public void update(Category category);
	public List<Category> list(String name);
	public List<Category> list();
	public Category load(int id);
}
