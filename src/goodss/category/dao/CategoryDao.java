package goodss.category.dao;

import goodss.category.domain.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.sun.org.apache.bcel.internal.generic.NEW;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;


public class CategoryDao {

	private QueryRunner qr=new TxQueryRunner();
	
	/**
	 * 把一个map中的数据映射到category中
	 * @param map
	 * @return
	 */
	private Category toCategory(Map<String, Object>map){
		Category category=CommonUtils.toBean(map, Category.class);
		String pid=(String)map.get("pid");
		if(pid!=null){
			//使用一个父分类对象来拦截pid
			Category parent=new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}
	
	/**
	 * 可以把多额Map映射层多个Category
	 * @param mapList
	 * @return
	 */
	private List<Category>toCategoryList(List<Map<String, Object>>mapList){
		List<Category> categoryList=new ArrayList<Category>();//创建一个空集合
		for(Map<String, Object>map:mapList){
			Category c=toCategory(map);
			categoryList.add(c);
		}
		return categoryList;
	}
	
	/**
	 * 返回所有分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category>findAll()throws SQLException{
		//查询所有记忆分类
		String sql="select * from t_category where pid is null order by orderBy";
		List<Map<String, Object>>mapList=qr.query(sql, new MapListHandler());
		
		List<Category> parents=toCategoryList(mapList);
		
		//循环遍历所有的一级分类，为每个一级分类加载它的二级分类
		for(Category parent:parents){
			//查询当前父分类的所有子类
			List<Category> children=findByParent(parent.getCid());
			//设置给父分类
			parent.setChildren(children);
		}
		return parents;
	}

	/**
	 * 通过父分类查询子分类
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findByParent(String pid) throws SQLException{
		// TODO Auto-generated method stub
		String sql="select * from t_category where pid=? order by orderBy";
		List<Map<String, Object>> mapList=qr.query(sql, new MapListHandler(),pid);
		return toCategoryList(mapList);
	}
}
