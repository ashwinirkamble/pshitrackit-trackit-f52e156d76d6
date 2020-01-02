package com.premiersolutionshi.old.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

/**
 * Initializes Logger
 */
public class Log4jInit extends HttpServlet {
    private static final long serialVersionUID = -6745314663661839586L;

    public void init() {
        ServletContext servletContext = getServletContext();
        String prefix = servletContext == null ? "" : servletContext.getRealPath("/");
        String file = getInitParameter("log4j-init-file");
        if (file != null) {
            PropertyConfigurator.configure(prefix + file);
        }
    }
}