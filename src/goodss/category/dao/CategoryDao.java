package goodss.category.dao;

import goodss.category.domain.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

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

	/**
	 * 添加分类
	 * @param category
	 * @throws SQLException
	 */
	public void add(Category category) throws SQLException {
		String sql="insert into t_category(cid,cname,pid,'desc') values(?,?,?,?)";
		
		String pid=null;//一级分类
		if(category.getParent()!=null){
			pid=category.getParent().getCid();
		}
		Object[] params={category.getCid(),category.getCname(),pid,category.getDesc()};
		qr.update(sql,params);
	}

	/**
	 * 获取所有父分类，但不带子分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findByParents() throws SQLException {
		String sql="select * from t_category where pid is null order by orderBy";
		List<Map<String , Object>>mapList=qr.query(sql, new MapListHandler());
		return toCategoryList(mapList);
		
	}

	/**
	 * 加载分类
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public Category load(String cid) throws SQLException {
		String sql="select * from t_category where cid=?";
		return toCategory(qr.query(sql, new MapHandler(),cid));
	}

	/**
	 * 修改分类，可修改一二级
	 * @param category
	 * @throws SQLException 
	 */
	public void edit(Category category) throws SQLException {
		// TODO Auto-generated method stub
		String sql="update t_category set cname=?,pid=?,'desc'=? where cid=?";
		String pid=null;
		if(category.getParent()!=null){
			pid=category.getParent().getCid();
		}
		Object[] params={category.getCname(),pid,category.getDesc(),category.getCid()};
		qr.update(sql,params);
	}

	/**
	 * 查询指定父分类下子分类的个数
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public int findChildrenCountByParent(String pid) throws SQLException {
		String sql="select count(*) from t_category where pid=?";
		Number cnt=(Number)qr.query(sql, new ScalarHandler(),pid);
		return cnt==null? 0:cnt.intValue();
	}

	/**
	 * 删除分类
	 * @param cid
	 * @throws SQLException
	 */
	public void delete(String cid) throws SQLException {
		String sql="delete from t_category where cid=?";
		qr.update(sql,cid);
		
	}
}
