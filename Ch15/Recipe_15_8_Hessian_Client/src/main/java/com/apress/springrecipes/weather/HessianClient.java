package com.apress.springrecipes.weather;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.caucho.HessianClientInterceptor;

public class HessianClient {

    public static void main(String[] args) {
        ApplicationContext context =
            new GenericXmlApplicationContext("appContext.xml");

        WeatherServiceClient client = (WeatherServiceClient) context.getBean("weatherClient");
        TemperatureInfo temperature = client.getTodayTemperature("Houston");
        System.out.println("Min temperature : " + temperature.getMin());
        System.out.println("Max temperature : " + temperature.getMax());
        System.out.println("Average temperature : " + temperature.getAverage());
    }
}
