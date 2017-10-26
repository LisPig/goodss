package goodss.order.web.servlet;



import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pager.PageBean;

import goodss.cart.domain.CartItem;
import goodss.cart.service.CartItemService;
import goodss.order.domain.Order;
import goodss.order.domain.OrderItem;
import goodss.order.service.OrderService;
import goodss.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	private OrderService orderService=new OrderService();
	private CartItemService cartItemService=new CartItemService();
	
	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if(param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch(RuntimeException e) {}
		}
		return pc;
	}
	
	/**
	 * 截取url，页面中的分页导航中需要使用它做为超链接的目标！
	 * @param req
	 * @return
	 */
	/*
	 * http://localhost:8080/goods/BookServlet?methed=findByCategory&cid=xxx&pc=3
	 * /goods/BookServlet + methed=findByCategory&cid=xxx&pc=3
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		/*
		 * 如果url中存在pc参数，截取掉，如果不存在那就不用截取。
		 */
		int index = url.lastIndexOf("&pc=");
		if(index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}
	/**
	 * 取消订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cencel(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		String oid=req.getParameter("oid");
		//检验订单状态
		int status=orderService.findStatus(oid);
		if(status!=1){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能取消");
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);//设置状态为取消
		req.setAttribute("code", "success");
		req.setAttribute("msg", "你的订单已取消");
		return "f:/jsps/msg.jsp";
	}
	/**
	 * 交易完成
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid=req.getParameter("oid");
		
		int status=orderService.findStatus(oid);
		if(status !=3){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能确认收货");
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 4);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "交易成功");
		return "f:jsps/msg.jsp";
	}
	/**
	 * 加载订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		String oid=req.getParameter("oid");
		Order order=orderService.load(oid);
		req.setAttribute("order", order);
		String btn=req.getParameter("btn");
		req.setAttribute("btn", btn);
		return "/jsps/order/desc.jsp";
	}
	
	/**
	 * 生成订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String createOrder(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		//获取所有购物车条目
		String cartItemIds=req.getParameter("cartItemIds");
		List<CartItem> cartItemList=cartItemService.loadCartItems(cartItemIds);
		if(cartItemList.size()==0){
			req.setAttribute("code","error");
			req.setAttribute("msg", "您没有选择要购买的图书，不能下单！");
			return "f:/jsps/msg.jsp";
		}
		//创建order
		Order order=new Order();
		order.setOid(CommonUtils.uuid());//设置主键
		order.setOrdertime(String.format("%tF %<tT", new Date()));//下单时间
		order.setStatus(1);//设置状态，1表示未付款
		order.setAddress(req.getParameter("address"));//设置收货地址
		User owner=(User)req.getSession().getAttribute("sessionUser");
		order.setOwner(owner);//设置订单所有者
		
		BigDecimal total=new BigDecimal("0");
		for(CartItem cartItem:cartItemList){
			total=total.add(new BigDecimal(cartItem.getSubtotal()+""));
		}
		order.setTotal(total.doubleValue());//设置总计
		
		//创建List<Orderitem>
		List<OrderItem>orderItemList=new ArrayList<OrderItem>();
		for(CartItem cartItem:cartItemList){
			OrderItem orderItem=new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());//设置主键
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setBook(cartItem.getBook());
			orderItem.setOrder(order);
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);
		
		//调用service完成添加
		orderService.createOrder(order);
		//删除购物车条目
		//CartItemService.batchDelete(cartItemIds);
		
		//保存订单，转发到ordersucc.jsp
		req.setAttribute("order", order);
		return "f:/jsps/order/ordersucc.jsp";
	}
	
	/**
	 * 我的订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 从当前session中获取user
		 */
		User user=(User)req.getSession().getAttribute("sessionUser");
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		PageBean<Order> pb = orderService.myOrders(user.getUid(), pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/order/list.jsp";
	}

}
