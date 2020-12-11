package com.sg.flooring;

import com.sg.flooring.controller.Controller;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {
    public static void main(String[] args){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("com.sg.flooring");
        ctx.refresh();
        Controller controller = ctx.getBean("controller", Controller.class);
        controller.run();
    }
}
