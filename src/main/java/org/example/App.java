package org.example;

import org.example.service.Menu;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        String importFile = "";
        String exportFile = "";
        for (int i = 0; i < args.length;i++){
            if (args[i].equals("-import")){
                importFile=args[i+1];
            }else if (args[i].equals("-export")){
                exportFile=args[i+1];
            }
        }
        Menu menu = new Menu(importFile,exportFile);
        menu.menu();
    }
}
