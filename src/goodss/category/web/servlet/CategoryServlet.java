package goodss.category.web.servlet;



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import goodss.category.domain.Category;
import goodss.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

/**
 * 分类模块web层
 * @author lsp11
 *
 */
public class CategoryServlet extends BaseServlet {
	private CategoryService categoryService=new CategoryService();
	
	public String findAll(HttpServletRequest req,HttpServletResponse resp)
	throws ServletException,IOException{
		List<Category>parents=categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/jsps/left.jsp";
	}
}
