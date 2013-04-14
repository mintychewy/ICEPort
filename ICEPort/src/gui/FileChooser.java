package gui;

import java.io.File;


import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileChooser {
  public String FileChooser() {
  
	  String path ;
 
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("."));

    chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".jpg")
            || f.isDirectory();
      }

      public String getDescription() {
        return "JPEG Images";
      }
    });

    int r = chooser.showOpenDialog(new JFrame());
    if (r == JFileChooser.APPROVE_OPTION) {
      String name = chooser.getSelectedFile().getName();
      //System.out.println(name); return name of the file
      
    }
    
    path =""+ chooser.getSelectedFile();
    return path;//for server
  }
  public static void main(String [] args){
	  FileChooser a = new FileChooser();
	  String ans =a.FileChooser();
	  System.out.print(ans);
	  }
}