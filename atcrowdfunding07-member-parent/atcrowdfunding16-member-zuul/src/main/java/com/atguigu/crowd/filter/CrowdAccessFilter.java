package com.atguigu.crowd.filter;

import com.atguigu.crowd.constant.AccessPassResource;
import com.atguigu.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Component
public class CrowdAccessFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 得到当前线程请求
        HttpServletRequest request = currentContext.getRequest();
        String servletPath = request.getServletPath();
        if (AccessPassResource.PASS_RES_SET.contains(servletPath)) {
            return false;// 放行
        }

        return !AccessPassResource.judgeCurrentServletPathWetherStaticResource(servletPath);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 得到当前线程请求
        HttpServletRequest request = currentContext.getRequest();
        HttpSession session = request.getSession();
        //zuul工程必须依赖entity，redis中存储的是LoginMember类型的数据，到redis中反序列化时会在zuul工程中找LoginMember类，即便最后是Object类型的对象
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        if (loginMember == null) {
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
            HttpServletResponse response = currentContext.getResponse();
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

}
