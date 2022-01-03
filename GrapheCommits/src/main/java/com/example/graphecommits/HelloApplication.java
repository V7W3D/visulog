package com.example.graphecommits;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {
    final static String austria = "Austria";
    final static String brazil = "Brazil";
    final static String france = "France";
    final static String italy = "Italy";
    final static String usa = "USA";
    Map<String,Integer> mp =new HashMap<>();



    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Bar Chart Sample");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Evolution commits");
        xAxis.setLabel("Dates");
        yAxis.setLabel("Nombre de commits");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Graphe commits");

        Object  s[] = null;
        Object  d = null;
        File file = new File("/home/khalifa/Documents/L3/commits2021.txt");
        File filedate = new File("/home/khalifa/Documents/L3/Date.txt");
        try  {
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedReader brdate = new BufferedReader(new FileReader(filedate));
            String linecommits;
            Object linedate;
            int i=0;
            while ((linecommits = br.readLine()) != null && (linedate = brdate.readLine()) != null) {
                //System.out.println(line);

                s=getIntegers(linecommits);

                series1.getData().add(new XYChart.Data(linedate, s[s.length-1]));

                //System.out.println(s[s.length-1]);
            }

        }catch(IOException e){
            System.out.println(e);
        }

        Scene scene  = new Scene(bc,1500,1300);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
    }
    public static Object[] getIntegers(String str) {
        ArrayList<Integer> list = new ArrayList<Integer>();

        //découper la phrase en mots
        String[] splited = str.split(" ");

        //parcourir les mots
        for (String current : splited) {
            try {
                //tenter de convertir le mot en int
                int parsedInt = Integer.parseInt (current);
                //ajouter l Integer à la list
                list.add(parsedInt);                    //un "auto boxing", une instance de Integer est créée à partir d'un int
            } catch (NumberFormatException e) {
                //c est pas un int
            }
        }

        //construire le résultat
        Object[] result = new Object[list.size()];
        for (int i = 0 ; i < list.size() ; i++) {
            //parcourir la list de Integer créée
            //result[list.size()-2] = list.get(list.size()-2);
            result[list.size()-1] = list.get(list.size()-1);
        }
        return result;
    }

    public Object commits(){
        Object  s[] = null;
        File file = new File("/home/khalifa/Documents/L3/commits2021.txt");
        try  {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linecommits;
            int i=0;
            while ((linecommits = br.readLine()) != null) {
                //System.out.println(line);

                s=getIntegers(linecommits);

                System.out.println(s[s.length-1]);
            }

        }catch(IOException e){
            System.out.println(e);
        }
        return s[s.length-1];
    }

    public static void main(String[] args) {
        launch();
        new HelloApplication().commits();
    }
}