package goodss.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;

import goodss.cart.dao.CartItemDao;
import goodss.cart.domain.CartItem;

public class CartItemService {
	private CartItemDao cartItemDao=new CartItemDao();
	
	/**
	 * 加载多个cartItem
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem>loadCartItems(String cartItemIds){
		try {
			return cartItemDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改购物车条目数量
	 * @param cartItemId
	 * @param quantity
	 * @return
	 */
	public CartItem updateQuantity(String cartItemId,int quantity){
		try {
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 批量删除
	 * @param cartItemIds
	 */
	public void batchDelete(String cartItemIds){
		try {
			cartItemDao.batchDelete(cartItemIds);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加条目
	 * @param cartItem
	 */
	public void add(CartItem cartItem){
		try {
			//使用uid和bid去数据库中查询这个条目是否存在
			CartItem _cartItem=cartItemDao.finByUidAndBid(
					cartItem.getUser().getUid(),cartItem.getBook().getBid());
			if(_cartItem==null){//如果没有就添加
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			}else{//如果有，修改数量
				int quantity=cartItem.getQuantity()+_cartItem.getQuantity();
				//修改老条目的数量
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), quantity);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 我的购物车
	 * @param uid
	 * @return
	 */
	public List<CartItem>myCart(String uid){
		try {
			return cartItemDao.findByUser(uid);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
}
