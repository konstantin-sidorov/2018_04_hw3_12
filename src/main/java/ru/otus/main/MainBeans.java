package ru.otus.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.cache.CacheEngine;
import ru.otus.dataSets.AddressDataSet;
import ru.otus.dataSets.DataSet;
import ru.otus.dataSets.PhoneDataSet;
import ru.otus.dataSets.UserDataSet;
import ru.otus.dbService.DBService;
import ru.otus.dbService.DBServiceImpl;
import ru.otus.servlet.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainBeans {
    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";
    /*@Autowired
    private DBService db;
    @Autowired
    private CacheEngine cache;*/

    public static void main(String[] args) throws Exception {
        int size = 3;
        DataSet u1 = new UserDataSet(1,
                "Даша", 25, new AddressDataSet("Ленина ул."),
                Arrays.asList(new PhoneDataSet("45-48-44"), new PhoneDataSet("11-11-11"))
        );

        DataSet u2 = new UserDataSet(2, "Маша", 23, new AddressDataSet("Куйбышева ул."),
                Arrays.asList(new PhoneDataSet("22-22-22"), new PhoneDataSet("xx-xx-xx")));

        DataSet u3 = new UserDataSet(3, "Паша", 43, new AddressDataSet("Арбат ул."),
                Arrays.asList(new PhoneDataSet("33-33-33"), new PhoneDataSet("yy-yy-yy")));
        ApplicationContext appContext = new ClassPathXmlApplicationContext("SpringBeans.xml");
       /* try (DBServiceImpl db = appContext.getBean("DBServiceImpl", DBServiceImpl.class)) {
        //try (DBServiceImpl dataBase = appContext.getBean("DBServiceImpl", DBServiceImpl.class)) {
            List<DataSet> users = new ArrayList<>();
            users.add(u1);
            users.add(u2);
            users.add(u3);
            db.addUsers(users);
            for (int i = 1; i < 4; i++) {
                DataSet element = db.loadUser(i, UserDataSet.class);
                System.out.println("element " + i + ": " + (element != null ? element : "null"));
            }
            Thread.sleep(1000);

            for (int i = 1; i < 4; i++) {
                DataSet element = db.loadUser(i, UserDataSet.class);
                System.out.println("element " + i + ": " + (element != null ? element : "null"));
            }

            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setResourceBase(PUBLIC_HTML);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            //TemplateProcessor templateProcessor = appContext.getBean("TemplateProcessor", TemplateProcessor.class);
            TemplateProcessor templateProcessor = new TemplateProcessor();
            context.addServlet(new ServletHolder(new LoginServlet(templateProcessor, "anonymous", db)), "/login");
            context.addServlet(new ServletHolder(new AdminServlet(db.getCache())), "/admin");

            Server server = new Server(PORT);
            server.setHandler(new HandlerList(resourceHandler, context));

            server.start();
            server.join();


            db.shutdown();
        }*/
    }
}
