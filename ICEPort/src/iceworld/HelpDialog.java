package iceworld;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpDialog extends JTabbedPane{


	public HelpDialog(){
		addTabs();
	}

	private void addTabs(){
		this.add("Basic", makeHtmlView());
		this.add("Advanced", makeHtmlView());
	}
	
	private JEditorPane makeHtmlView(){
		final JEditorPane jep = new JEditorPane();
		jep.setEditable(false);
		 ToolTipManager.sharedInstance().registerComponent(jep);

		    HyperlinkListener l = new HyperlinkListener() {
		        @Override
		        public void hyperlinkUpdate(HyperlinkEvent e) {
		            if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
		                try {
		                    jep.setPage(e.getURL());
		                } catch (IOException e1) {
		                    e1.printStackTrace();
		                }
		            }

		        }

		    };
		    jep.addHyperlinkListener(l);
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
