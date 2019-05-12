package main;

import javax.swing.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class Window extends JFrame {
    private Path path;

    public Window(String openOrSave){

        showFileDialog(openOrSave);
    }

    private void showFileDialog( String openOrSave) {
        JFrame dialog = new JFrame();
        JFileChooser chooser = new JFileChooser();
        dialog.setAlwaysOnTop(true);
        int userSelection;
        chooser.setDialogTitle("Wybierz");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if(openOrSave =="open"){
            userSelection = chooser.showOpenDialog(dialog);
        }
        else if(openOrSave =="save"){
            userSelection = chooser.showSaveDialog(dialog);
        }
        else{
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            userSelection=chooser.showDialog(dialog,"Wybierz");
        }

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().toPath();
            System.out.println("Wybrano: " + path.toString());
        }
        else{
            System.out.println("Nie udało się ustawić ścieżki");
            System.exit(-2);
        }
    }

    public Path getPath() {
        return path;
    }
}
