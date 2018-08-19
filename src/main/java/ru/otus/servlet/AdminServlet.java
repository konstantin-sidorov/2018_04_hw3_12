package ru.otus.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.cache.CacheEngine;
import ru.otus.cache.CacheEngineImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AdminServlet extends HttpServlet {

    private static final String DEFAULT_USER_NAME = "UNKNOWN";
    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";

    private final TemplateProcessor templateProcessor;
    private final CacheEngine cache;

    public AdminServlet() throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");
        this.cache = context.getBean("CacheEngine", CacheEngineImpl.class);
        this.templateProcessor = new TemplateProcessor();
    }

    @SuppressWarnings("WeakerAccess")
    public AdminServlet(TemplateProcessor templateProcessor, CacheEngine cache) {
        this.templateProcessor = templateProcessor;
        this.cache = cache;
    }

    @SuppressWarnings("WeakerAccess")
    public AdminServlet(CacheEngine cache) throws IOException {
        this(new TemplateProcessor(), cache);
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("hits", cache.getHitCount());
        pageVariables.put("misses", cache.getMissCount());
        pageVariables.put("maxElements", cache.getMaxElements());
        pageVariables.put("currElements", cache.getСurrElements());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());

        String login = (String) request.getSession().getAttribute(LoginServlet.LOGIN_PARAMETER_NAME);
        pageVariables.put("login", login != null ? login : DEFAULT_USER_NAME);

        return pageVariables;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request);

        response.setContentType("text/html;charset=utf-8");
        String page = templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables);
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
