package com.idsmanager.casclient.web.context;

import com.idsmanager.casclient.infrastructure.JzytConstants;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * 2017/1/4
 *
 * @author Shengzhao Li
 * @since 1.2.0
 */
public class JzytContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);

        final ServletContext servletContext = event.getServletContext();
        //set version
        servletContext.setAttribute("currVersion", JzytConstants.VERSION);
    }
}
