package ru.otus.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.dataSets.AddressDataSet;
import ru.otus.dataSets.PhoneDataSet;
import ru.otus.dataSets.UserDataSet;
import ru.otus.dbService.DBServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configurable
public class LoginServlet extends AbstractServlet {

    public static final String LOGIN_PARAMETER_NAME = "login";
    private static final String LOGIN_VARIABLE_NAME = "login";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String ADMIN_NAME = "admin";
    @Autowired
    private TemplateProcessor templateProcessor;
    private String login;
    @Autowired
    private CacheEngineImpl cache;
    @Autowired
    private DBServiceImpl dataBase;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        /*this.dataBase=new DBServiceImpl(cache);*/
        this.dataBase.save(new UserDataSet(3, "Паша", 43, new AddressDataSet("Арбат ул."),
                Arrays.asList(new PhoneDataSet("33-33-33"), new PhoneDataSet("yy-yy-yy"))));
    }

    /*public LoginServlet() throws IOException {
        //ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");
        this.templateProcessor = new TemplateProcessor();
        //this.dataBase = context.getBean("DBServiceImpl", DBServiceImpl.class);
        this.login = "anonymous";
        this.dataBase.save(new UserDataSet(3, "Паша", 43, new AddressDataSet("Арбат ул."),
                Arrays.asList(new PhoneDataSet("33-33-33"), new PhoneDataSet("yy-yy-yy"))));


    }*/

    /*
    public LoginServlet(TemplateProcessor templateProcessor, String login, DBService dataBase) {
        this.login = login;
        this.templateProcessor = templateProcessor;
        this.dataBase = dataBase;
    }
    */
    private String getPage(String login) throws IOException {
        String admin = "";
        if (login != null && !String.valueOf("").equals(login)) {
            if (dataBase.readByName(login, UserDataSet.class) != null) {
                admin = "Admin";
            } else {
                login = "Пользователь: " + login + " не найден";
            }
        }
        ;
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(LOGIN_VARIABLE_NAME, login == null ? "" : login);
        pageVariables.put(ADMIN_NAME, admin);
        return templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String requestLogin = request.getParameter(LOGIN_PARAMETER_NAME);

        if (requestLogin != null) {
            saveToSession(request, requestLogin); //request.getSession().getAttribute("login");
        }
        setOK(response);
        String l = (String) request.getSession().getAttribute("login");
        String page = getPage(l); //save to the page
        response.getWriter().println(page);
    }

    private void saveToSession(HttpServletRequest request, String requestLogin) {
        request.getSession().setAttribute("login", requestLogin);
    }

    private void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
