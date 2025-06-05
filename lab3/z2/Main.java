import java.awt.Point;

import javax.swing.JFrame;

public class Main {

  public static void main(String[] args){
    // MyFrame f = new MyFrame();
    // f.setVisible(true);

    TextEditorModel model = new TextEditorModel("Text sa \nvise redaka.\nJos jedan red!");

    TextEditor editor = new TextEditor(model);
    MyMenu menu = new MyMenu(model);
    StatusBar statusBar = new StatusBar(model);
    
    JFrame mainFrame = new JFrame();
    
    mainFrame.add(statusBar);
    
    mainFrame.setJMenuBar(menu);
    
    mainFrame.add(editor);
    mainFrame.setTitle("OOUP");
    mainFrame.setSize(700,500);
    mainFrame.setLocation(new Point(300,200));
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // mainFrame.setLayout(null);    
    mainFrame.setResizable(false);
    
    mainFrame.setVisible(true);
    
    // to recieve keyboard inputs
    editor.setFocusable(true);
    editor.requestFocus();
    
    
    statusBar.setBounds(0, mainFrame.getHeight()-90, mainFrame.getWidth(), 30);
  }
}