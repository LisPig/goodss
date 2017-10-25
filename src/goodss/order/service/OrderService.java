package goodss.order.service;

import java.sql.SQLException;

import cn.itcast.jdbc.JdbcUtils;
import pager.PageBean;
import goodss.order.dao.OrderDao;
import goodss.order.domain.Order;



public class OrderService {
	private OrderDao orderDao=new OrderDao();

	/**
	 * 生成订单
	 * @param order
	 */
	public void createOrder(Order order) {
		try {
			JdbcUtils.beginTransaction();
			orderDao.add(order);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e2) {}
				throw new RuntimeException(e);
			
		}
		
	}
	
	/**
	 * 我的订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrders(String uid, int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb=orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e2) {}
				throw new RuntimeException(e);
			
		}
		
	}
	
	/**
	 * 按状态查询
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<Order>findByStatus(int status,int pc){
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb=orderDao.findByStatus(status, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			// TODO: handle exception
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e2) {}
				// TODO: handle exception
				throw new RuntimeException();
			
		}
	}
	/**
	 * 查询所有
	 * @param pc
	 * @return
	 */
	public PageBean<Order>findAll(int pc){
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order>pb=orderDao.findAll(pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			// TODO: handle exception
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e2) {
				// TODO: handle exception
			}
			throw new RuntimeException();
		}
	}

	/**
	 * 加载订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid) {
		try {
			JdbcUtils.beginTransaction();
			Order order=orderDao.load(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (SQLException e) {
			// TODO: handle exception
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e2) {
				// TODO: handle exception
			}
			throw new RuntimeException();
		}
	}

}
