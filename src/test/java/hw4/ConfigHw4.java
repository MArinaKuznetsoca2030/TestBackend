package hw4;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigHw4 {

    public static String apiKey, apiKey2, urlComplexSearch, urlBase, urlRecipesCuisin;

    public static void main(String[] args)

    {
        readPropirties();
    }

        public static void readPropirties() {
        FileInputStream fileInputStream;

        //инициализируем специальный объект Properties
        Properties prop = new Properties();

        try {
            //обращаемся к файлу и получаем данные
            fileInputStream = new FileInputStream("src/main/resources/config.properties");
            prop.load(fileInputStream);

            urlComplexSearch = prop.getProperty("urlComplexSearch");
           // apiKey2 = prop.getProperty("apiKey2");
            apiKey = prop.getProperty("apiKey");
            urlBase = prop.getProperty("urlBase");
            urlRecipesCuisin = prop.getProperty("urlRecipesCuisin");

        } catch (IOException e) {
            System.out.println("Ошибка в программе: файл c параметрами не обнаружен");
            e.printStackTrace();
        }

    }
}
