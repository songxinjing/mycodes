package com.songxinjing.base.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.songxinjing.base.constant.Constant;
import com.songxinjing.base.domain.Privilege;
import com.songxinjing.base.domain.Role;
import com.songxinjing.base.domain.User;
import com.songxinjing.base.service.PrivService;

/**
 * 权限判断filter
 * 
 * @author songxinjing
 */
public class PrivFilter implements Filter {

	protected static Logger logger = LoggerFactory.getLogger(PrivFilter.class);

	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	PrivService privService = (PrivService) wac.getBean("privService");

	/**
	 * 设置不需要被filter拦截的urls（url之间用逗号分隔）<br>
	 * 例：/version.jsp,/logout,/sso_logout<br>
	 * 配置：web.xml | SessionFilter | init-param | excludeUrl
	 */
	protected final static String EXCLUDE_URL = "excludeUrl";

	/**
	 * <b>excludeUrl</b> 要过滤的url路径
	 */
	private String excludeUrl = null;

	public void init(FilterConfig config) throws ServletException {
		String excludeUrl = config.getInitParameter(PrivFilter.EXCLUDE_URL);
		this.excludeUrl = excludeUrl;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
			throws IOException, ServletException {

		// make sure we've got an HTTP request
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("SessionFilter protects only HTTP resources");
		}

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		// 如果当前请求的context在配置的excludeUrl的context名单内，则放过，不拦截
		if (isExcludeUrl(req, excludeUrl)) {
			fc.doFilter(request, response);
			return;
		}

		String reqUrl = req.getRequestURI();

		// 获取请求url对应的权限
		Privilege privForUrl = findPrivForUrl(req, privService.findEnable());

		// 该请求不需要验证权限，放过，不拦截
		if (privForUrl == null) {
			fc.doFilter(request, response);
			return;
		}

		// 获取用户登录信息，用户组别判断
		User user = (User) session.getAttribute(Constant.SESSION_LOGIN_USER);

		// 用户访问权限判断
		if (user != null) {
			// 获取用户拥有的所有权限
			for (Role role : user.getRoles()) {
				// 拥有权限
				if (role.getPrivileges().contains(privForUrl)) {
					fc.doFilter(request, response);
					return;
				}
			}
			
			logger.info("用户" + user.getUserId() + "没有访问" + reqUrl + "的权限！");
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		logger.info(reqUrl + "需要访问权限！");
		resp.sendError(HttpServletResponse.SC_FORBIDDEN);
		return;
	}

	/**
	 * 判断当前request url是否和excludUrl中设置的url匹配
	 * 
	 * @return 匹配 -> true<br>
	 *         不匹配 -> false
	 */
	private boolean isExcludeUrl(HttpServletRequest request, String excludeUrl) throws ServletException {
		if (excludeUrl != null && !excludeUrl.trim().equalsIgnoreCase("")) {
			String contentPath = request.getContextPath();
			String url = request.getRequestURI();
			String[] urlArr = excludeUrl.split(",");
			for (int i = 0; i < urlArr.length; i++) {
				String excUrl = contentPath + urlArr[i].toString();
				if (excUrl.indexOf("*") > 0) {
					int a = excUrl.indexOf("*");
					excUrl = excUrl.substring(0, a);
				}
				if (url.startsWith(excUrl)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断当前请求url对应的权限
	 * 
	 * @return 存在 -> 权限对象<br>
	 *         不存在 -> null
	 */
	private Privilege findPrivForUrl(HttpServletRequest request, List<Privilege> privileges) throws ServletException {
		String contentPath = request.getContextPath();
		String url = request.getRequestURI();
		for (Privilege priv : privileges) {
			String privUrl = contentPath + priv.getUrl();
			if (url.startsWith(privUrl)) {
				return priv;
			}
		}
		return null;
	}

	public void destroy() {
		// do nothing
	}

}
