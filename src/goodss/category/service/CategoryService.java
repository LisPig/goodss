package goodss.category.service;



import java.sql.SQLException;
import java.util.List;

import goodss.category.dao.CategoryDao;
import goodss.category.domain.Category;

public class CategoryService {
	private CategoryDao categoryDao=new CategoryDao();
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category>findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查询指定父分类下的所有子类
	 * @param pid
	 * @return
	 */
	public List<Category>findChildren(String pid){
		try {
			return categoryDao.findByParent( pid);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加分类
	 * @param category
	 */
	public void add(Category category) {
		try {
			categoryDao.add(category);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException();
		}
		
	}

	/**
	 * 获取所有父分类，不带子分类
	 * @return
	 */
	public List<Category> findParents() {
		try{
		 return categoryDao.findByParents();
		}catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 加载分类
	 * @param cid
	 * @return
	 */
	public Category load(String cid) {
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 修改分类
	 * @param parent
	 */
	public void edit(Category category) {
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * 查询指定分类下子分类的个数
	 * @param pid
	 * @return
	 */
	public int findChildrenCountByParent(String pid) {
		try {
			return categoryDao.findChildrenCountByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 删除分类
	 * @param cid
	 */
	public void delete(String cid) {
		// TODO Auto-generated method stub
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException();
		}
	}
}
