package goodss.book.web.servlet;




import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pager.PageBean;

import goodss.book.domain.Book;
import goodss.book.service.BookService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {

	private BookService bookService=new BookService();
	
	/**
	 * 截取url
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req) {
		// TODO Auto-generated method stub
		String url=req.getRequestURI()+"?"+req.getQueryString();
		//如果url中存在pc参数，截取掉
		int index=url.lastIndexOf("&pc=");
		if(index !=-1){
			url=url.substring(0,index);
		}
		return url;
	}


	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		// TODO Auto-generated method stub
		int pc=1;
		String param=req.getParameter("pc");
		if(param !=null&&!param.trim().isEmpty()){
			try {
				pc=Integer.parseInt(param);
			} catch (RuntimeException e) {
				// TODO: handle exception
			}
		}
		return pc;
	}
	
	/**
	 * 按bid查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		String bid=req.getParameter("bid");
		Book book=bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
	
	/**
	 * 按分类查
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req,HttpServletResponse resp)
	throws ServletException,IOException{
		//得到pc:如果页面传递，使用页面的，如果没传，pc=1
		int pc=getPc(req);
		
		//得到url
		String url=getUrl(req);
		
		//获取查询条件，本方法就是cid,即分类的id
		String cid=req.getParameter("cid");
		
		//使用pc和cid调用
		PageBean<Book> pb=bookService.findByCategory(cid, pc);
		
		//设置url,保存pagebean
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
		
	}

	/**
	 * 按作者查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor(HttpServletRequest req,HttpServletResponse resp)
			throws ServletException,IOException{
				//得到pc:如果页面传递，使用页面的，如果没传，pc=1
				int pc=getPc(req);
				
				//得到url
				String url=getUrl(req);
				
				//获取查询条件，本方法就是cid,即分类的id
				String author=req.getParameter("author");
				
				//使用pc和cid调用
				PageBean<Book> pb=bookService.findByAuthor(author, pc);
				
				//设置url,保存pagebean
				pb.setUrl(url);
				req.setAttribute("pb", pb);
				return "f:/jsps/book/list.jsp";
				
			}
	
	/**
	 * 按出版社查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress(HttpServletRequest req,HttpServletResponse resp)
			throws ServletException,IOException{
				//得到pc:如果页面传递，使用页面的，如果没传，pc=1
				int pc=getPc(req);
				
				//得到url
				String url=getUrl(req);
				
				//获取查询条件，本方法就是cid,即分类的id
				String press=req.getParameter("press");
				
				//使用pc和cid调用
				PageBean<Book> pb=bookService.findByPress(press, pc);
				
				//设置url,保存pagebean
				pb.setUrl(url);
				req.setAttribute("pb", pb);
				return "f:/jsps/book/list.jsp";
				
			}

	public String findByBname(HttpServletRequest req,HttpServletResponse resp)
			throws ServletException,IOException{
				//得到pc:如果页面传递，使用页面的，如果没传，pc=1
				int pc=getPc(req);
				
				//得到url
				String url=getUrl(req);
				
				//获取查询条件，本方法就是cid,即分类的id
				String bname=req.getParameter("bname");
				
				//使用pc和cid调用
				PageBean<Book> pb=bookService.findByBname(bname, pc);
				
				//设置url,保存pagebean
				pb.setUrl(url);
				req.setAttribute("pb", pb);
				return "f:/jsps/book/list.jsp";
				
		}
	
	public String findByCombination(HttpServletRequest req,HttpServletResponse resp)
			throws ServletException,IOException{
				//得到pc:如果页面传递，使用页面的，如果没传，pc=1
				int pc=getPc(req);
				
				//得到url
				String url=getUrl(req);
				
				//获取查询条件，本方法就是cid,即分类的id
				Book criteria=CommonUtils.toBean(req.getParameterMap(),Book.class);
				
				//使用pc和cid调用
				PageBean<Book> pb=bookService.findByCombination(criteria, pc);
				
				//设置url,保存pagebean
				pb.setUrl(url);
				req.setAttribute("pb", pb);
				return "f:/jsps/book/list.jsp";
				
			}


}
