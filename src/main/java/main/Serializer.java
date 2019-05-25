package main;

import java.io.*;

public class Serializer {
    public void netSaver (DataManager dataManager, String filePath) throws IOException {
        FileOutputStream myFileOutputStream = new FileOutputStream (filePath);
        ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
        myObjectOutputStream.writeObject(dataManager);
        myObjectOutputStream.close();
    }

    public DataManager netRestorer(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream myFileInputStream = new FileInputStream(filePath);
        ObjectInputStream myObjectInputStream = new ObjectInputStream (myFileInputStream);
        DataManager dataManager = (DataManager) myObjectInputStream.readObject ();
        myObjectInputStream.close();
        return dataManager;
    }
}
