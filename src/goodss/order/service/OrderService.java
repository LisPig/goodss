package goodss.order.service;

import java.sql.SQLException;

import cn.itcast.jdbc.JdbcUtils;
import pager.PageBean;
import goodss.order.dao.OrderDao;
import goodss.order.domain.Order;



public class OrderService {
	private OrderDao orderDao=new OrderDao();

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

}
