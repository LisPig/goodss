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
	
	public List<Category>findChildren(String pid){
		try {
			return categoryDao.findByParent( pid);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
}
