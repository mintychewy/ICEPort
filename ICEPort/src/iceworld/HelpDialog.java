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
		this.add("Basic", makeHtmlView("basic"));
		this.add("Advanced", makeHtmlView("advanced"));
	}
	
	private JEditorPane makeHtmlView(String mode){
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
			if(mode.equals("basic")){
				jep.setPage("https://dl.dropboxusercontent.com/u/97755864/basic.html");
 
			}else if(mode.equals("advanced")){
				jep.setPage("https://dl.dropboxusercontent.com/u/97755864/advance/advance.html"); // this should work
			}
		} catch(IOException e){
			jep.setContentType("text/html");
			jep.setText("<html>Could not load</html>");
		}
		
		jep.setPreferredSize(new Dimension(500,500));
		return jep;
	}

	// okay.. please put this on pastebin for me thank you
	// and don't remove the html files from your dropbox yet na I'll have to copy
	// and put on my cerntainly.com host
	
	
}
