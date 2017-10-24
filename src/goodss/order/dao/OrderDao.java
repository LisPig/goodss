package goodss.order.dao;

import goodss.book.domain.Book;
import goodss.order.domain.Order;
import goodss.order.domain.OrderItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;



import pager.Expression;
import pager.PageBean;
import pager.PageConstants;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr=new TxQueryRunner();
	
	
	/**
	 * 按用户查询订单
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByUser(String uid, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("uid", "=", uid));
		return findByCriteria(exprList, pc);
	}
	
	
	
	
	private PageBean<Order>findByCriteria(List<Expression> exprList,int pc)
			throws SQLException{
		/*
		 * 1. 得到ps
		 * 2. 得到tr
		 * 3. 得到beanList
		 * 4. 创建PageBean，返回
		 */
		/*
		 * 1. 得到ps
		 */
		int ps=PageConstants.ORDER_PAGE_SIZE;//每页记录数
		
		//通过exprList来生成where字句
		StringBuilder whereSql=new StringBuilder(" where 1=1");
		List<Object> params=new ArrayList<Object>();//sql中有问好，它是对于问好的值
		for(Expression expr:exprList){
			/*
			 * 添加一个条件上，
			 * 1) 以and开头
			 * 2) 条件的名称
			 * 3) 条件的运算符，可以是=、!=、>、< ... is null，is null没有值
			 * 4) 如果条件不是is null，再追加问号，然后再向params中添加一与问号对应的值
			 */
			whereSql.append(" and ").append(expr.getName()).append(" ")
				.append(expr.getOperator()).append(" ");
			if(!expr.getOperator().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		
		//总记录数
		String sql="select count(*) from t_order" +whereSql;
		Number number=(Number)qr.query(sql,new ScalarHandler(),params.toArray());
		int tr=number.intValue();//得到了总记录数
		//得到beanList,即当前页记录
		sql="select * from t_order"+ whereSql +"order by ordertime desc limit ?,?";
		params.add((pc-1)*ps);//当前页首行记录的下标
		params.add(ps);//一共查询几行，就是每页记录数
		
		List<Order> beanList=qr.query(sql,new BeanListHandler<Order>(Order.class),
				params.toArray());
		//遍历每个订单，为其加载它的所有订单条目
		for(Order order:beanList){
			loadOrderItem(order);
		}
		
		//创建pagebean,设置参数
		PageBean<Order> pb=new PageBean<Order>();
		
		
		//其中pagebean没有url，由servlet完成 
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		return pb;
	}

	/**
	 * 为指定的order载它的所有OrderItem
	 * @param order
	 * @throws SQLException
	 */
	private void loadOrderItem(Order order) throws SQLException {
		String sql="select * from t_orderitem where oid=?";
		List<Map<String, Object>>mapList=qr.query(sql, new MapListHandler(),order.getOid());
		List<OrderItem>orderItemList=toOrderItemList(mapList);
		
		order.setOrderItemList(orderItemList);
		
	}
	
	/**
	 * 把多个map转换成多个OrderItem
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		// TODO Auto-generated method stub
		List<OrderItem>orderItemList=new ArrayList<OrderItem>();
		for(Map<String,Object> map:mapList){
			OrderItem orderItem=toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}

	/**
	 * 把一个Map转换成一个OrderItem
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem=CommonUtils.toBean(map, OrderItem.class);
		Book book=CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

}