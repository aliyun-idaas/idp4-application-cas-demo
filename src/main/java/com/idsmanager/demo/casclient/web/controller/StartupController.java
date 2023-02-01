package com.idsmanager.demo.casclient.web.controller;

import com.idsmanager.demo.casclient.config.CasProperties;
import com.idsmanager.demo.casclient.infrastructure.JzytConstants;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * 2016/1/19
 *
 * @author Shengzhao Li
 * @since 1.2.0
 */
@Controller
public class StartupController {

    private static final Logger LOG = LoggerFactory.getLogger(StartupController.class);


    @Autowired
    private CasProperties casProperties;


    /*
     * CAS 登录成功后的页面， 显示 信息
     * */
    @RequestMapping(value = {"/index", "/"})
    public String dashboard(Model model, HttpServletRequest request) {
        //获取   CAS  Assertion
        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        if (LOG.isInfoEnabled()) {
            LOG.info("CAS Assertion: {}", assertion);
        }
        //CAS 用户名
        model.addAttribute("username", assertion.getPrincipal());
        model.addAttribute("validFromDate", assertion.getValidFromDate());
        model.addAttribute("authenticationDate", assertion.getAuthenticationDate());
        return "index";
    }


    /**
     * CAS Logout
     * 在 index.jsp中会调用
     */
    @RequestMapping(value = "signout", method = RequestMethod.POST)
    public String signout(HttpServletRequest request, boolean force) throws Exception {
        request.getSession().invalidate();
        //组装退出跳转到 idaas 中
        String url = casProperties.getLogoutUrl() + "?service=" + URLEncoder.encode(casProperties.getService(), JzytConstants.ENCODING) + "&force=" + force;
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Redirect to CAS Logout URL: {}", request.getSession().getId(), url);
        }
        return "redirect:" + url;
    }


}
