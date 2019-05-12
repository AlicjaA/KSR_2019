package main;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class UiManager {

    public Path getPath() throws InvocationTargetException, InterruptedException {
        final Path[] path = new Path[1];
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                Window w =new Window("dir");
                path[0] = w.getPath();
            }
        });
        return path[0];
    }

    public ArrayList<String> getAsStringArrayList(String label){
        System.out.println(label);
        Scanner scanner2 = new Scanner(System. in).useDelimiter("\\n");
        String[] keyLabelsTmp = scanner2.nextLine().split(" ");
        ArrayList<String> stringList = new ArrayList<>();
        for(String s: keyLabelsTmp){
            stringList.add(s);
        }
        return stringList;
    }

    public char[] getAsCharArray(String label){
        System.out.println(label);
        Scanner scanner = new Scanner(System. in).useDelimiter("\\n");
        char[] c = scanner.next().toCharArray();
        return c;
    }

    public String getAsString(String label){
        System.out.println(label);
        Scanner scanner = new Scanner(System. in).useDelimiter("\\n");
        String s = scanner.next();
        return s;
    }

    public ArrayList<Integer> getAsIntList(String label){
        System.out.println(label);
        Scanner scanner = new Scanner(System. in).useDelimiter("\\n");
        char[] s = scanner.nextLine().toCharArray();
        ArrayList<Integer> integers=new ArrayList<>();
        for(char c: s){
            integers.add(Character.getNumericValue(c));
        }
        return integers;
    }

    public Double getAsFraction(String label){
        System.out.println(label);
        Scanner scanner = new Scanner(System. in).useDelimiter("\\n");
        Double i = Double.valueOf(scanner.nextInt());
        return i/100;
    }
}
