package cn.edu.cqu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cn.edu.cqu.model.*;


/**
 * Servlet implementation class CheckUserController
 */
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println("from servlet:"+request.getRequestURL());
		User user = new User(username,password);
		// �û���������ڣ�ӦΪmodel����
		if (User.checkUser(username, password)) {
			// ��װ������һ��sessionid
			String sessionid = "CAS_TGC";
			// ����TGT
			session.setAttribute(sessionid, user);
			// ����TGC
			Cookie cookie = new Cookie("sso",sessionid);
			cookie.setMaxAge(-1);
			cookie.setPath("/");
			response.addCookie(cookie);
			// ����ST����ST����model���´μ���ʱ��������Ƚ�ST�Ƿ�Ϊ��ǰ��Чֵ
			String CAS_ST = ServiceTicket.generateST();
			// prep redirect���ص���תǰ��Ӧ��ϵͳ��STͨ��url��������
			Cookie [] cookies = request.getCookies();
			for (Cookie cooky: cookies) {
				if (cooky.getName().equals("redirect_url")) {
					String service = cooky.getValue();
					if(service != null && !service.equals("")) {
						response.sendRedirect(service+"?ticket="+CAS_ST);
					}
					else {
						response.sendRedirect("systema.jsp");
					}
				}
			}	
		}
	}

}
