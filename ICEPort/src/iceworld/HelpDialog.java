package iceworld;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JTabbedPane;

public class HelpDialog extends JTabbedPane{


	public HelpDialog(){
		addTabs();
	}

	private void addTabs(){
		this.add("Basic", makeHtmlView());
		this.add("Advanced", makeHtmlView());
	}
	
	private JEditorPane makeHtmlView(){
		JEditorPane jep = new JEditorPane();
		jep.setEditable(false);
		
		try{
			jep.setPage("http://cerntainly.com/demo/n.html");
		} catch(IOException e){
			jep.setContentType("text/html");
			jep.setText("<html>Could not load</html>");
		}
		jep.setPreferredSize(new Dimension(500,500));
		return jep;
	}
	
	
}
