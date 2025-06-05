import java.awt.Point;

import javax.swing.JFrame;

class frm {

  public static void main(String[] args){
    // MyFrame f = new MyFrame();
    // f.setVisible(true);

    TextEditorModel model = new TextEditorModel("Text sa \nvise redaka.\nJos jedan red! (Ali ovaj je malo dulji (jako jako jako dugacak))");

    TextEditor editor = new TextEditor(model);
    MyMenu menu = new MyMenu(model);
    
    
    JFrame mainFrame = new JFrame();

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
    
  }
}