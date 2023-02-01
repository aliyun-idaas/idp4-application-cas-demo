package com.idsmanager.demo.casclient.config;

import com.idsmanager.demo.casclient.infrastructure.JzytConstants;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 2018/3/20
 * <p>
 * 项目自身的配置
 *
 * @author Shengzhao Li
 */
@Configuration
@EnableConfigurationProperties(CasProperties.class)
public class ProjectConfiguration {
    @Autowired
    private CasProperties casProperties;

    /**
     * 字符编码 Filter,
     * 包括获取IP
     */
    @Bean
    public FilterRegistrationBean<Filter> characterFilter() {
        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding(JzytConstants.ENCODING);

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(characterEncodingFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("characterEncodingIPFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    /**
     * 退出, 注意顺序,  cas-logout 必须配置在最上面
     * CAS logout
     */
    @Bean
    public FilterRegistrationBean<Filter> filterSingleRegistration() {
        final FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SingleSignOutFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", casProperties.getUrlPrefix());
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(2);
        return registration;
    }

    /**
     * CAS 认证
     */
    @Bean
    public FilterRegistrationBean<Filter> filterAuthenticationRegistration() {
        final FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthenticationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", casProperties.getLoginUrl());
        initParameters.put("serverName", casProperties.getService());
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(3);
        return registration;
    }

    /**
     * CAS 检测 状态,  validate ticket
     */
    @Bean
    public FilterRegistrationBean<Filter> filterValidationRegistration() {
        final FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", casProperties.getUrlPrefix());
        initParameters.put("serverName", casProperties.getService());
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(4);
        return registration;
    }


    /**
     * 统一退出的 session 监听
     */
    @Bean
    public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration() {
        ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener());
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
