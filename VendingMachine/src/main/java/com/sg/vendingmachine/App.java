package com.sg.vendingmachine;

import com.sg.vendingmachine.controller.Controller;
import com.sg.vendingmachine.dao.VendingAuditDao;
import com.sg.vendingmachine.dao.VendingAuditDaoFileImpl;
import com.sg.vendingmachine.dao.VendingDao;
import com.sg.vendingmachine.dao.VendingDaoFileImpl;
import com.sg.vendingmachine.service.VendingServiceLayer;
import com.sg.vendingmachine.service.VendingServiceLayerImpl;
import com.sg.vendingmachine.ui.UserIO;
import com.sg.vendingmachine.ui.UserIOConsoleImpl;
import com.sg.vendingmachine.ui.View;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args){
        int di = 2; //(0-2) for dependancy injection method
        Controller controller;

        switch(di){
            case 0: //----MANUAL WIRING SETUP----
                UserIO myIO = new UserIOConsoleImpl();
                View myView = new View(myIO);
                VendingDao myDao = new VendingDaoFileImpl("inventory.txt");
                VendingAuditDao myAuditDao = new VendingAuditDaoFileImpl();
                VendingServiceLayer myService = new VendingServiceLayerImpl(myDao, myAuditDao);
                controller = new Controller(myView, myService);
                controller.run();
                break;
            case 1: //----Spring DI with xml----
                ClassPathXmlApplicationContext ctx =
                        new ClassPathXmlApplicationContext("applicationContext.xml");
                controller = ctx.getBean("controller", Controller.class);
                controller.run();
                break;
            case 2: //----Spring DI with Annotations----
                AnnotationConfigApplicationContext ctx2 = new AnnotationConfigApplicationContext();
                ctx2.scan("com.sg.vendingmachine");
                ctx2.refresh();
                controller = ctx2.getBean("controller", Controller.class);
                controller.run();
        }
    }
}
