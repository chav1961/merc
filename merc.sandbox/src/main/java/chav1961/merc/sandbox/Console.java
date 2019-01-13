package chav1961.merc.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import chav1961.purelib.basic.interfaces.LoggerFacade.Severity;

public class Console extends JPanel {
	private static final long 						serialVersionUID = 4439430893893928782L;
	private static final Map<Severity,AttributeSet>	ATTRIBUTES = new HashMap<>();
	private static final SimpleAttributeSet			DEFAULT_ATTRIBUTES = new SimpleAttributeSet();
	
	static {
		StyleConstants.setForeground(DEFAULT_ATTRIBUTES,Color.BLACK);
		
		SimpleAttributeSet	sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.LIGHT_GRAY);
		ATTRIBUTES.put(Severity.debug,sas);

		sas = new SimpleAttributeSet();
		StyleConstants.setForeground(sas,Color.RED);
		StyleConstants.setBold(sas,true);
		ATTRIBUTES.put(Severity.error,sas);

		sas = new SimpleAttributeSet();
		StyleConstants.setForeground(sas,Color.BLACK);
		ATTRIBUTES.put(Severity.info,sas);
		
		sas = new SimpleAttributeSet();
		StyleConstants.setForeground(sas,Color.RED);
		StyleConstants.setBold(sas,true);
		StyleConstants.setUnderline(sas,true);
		ATTRIBUTES.put(Severity.severe,sas);

		sas = new SimpleAttributeSet();
		StyleConstants.setForeground(sas,Color.LIGHT_GRAY);
		StyleConstants.setItalic(sas,true);
		ATTRIBUTES.put(Severity.trace,sas);

		sas = new SimpleAttributeSet();
		StyleConstants.setForeground(sas,Color.BLUE);
		ATTRIBUTES.put(Severity.warning,sas);
	}
	
	
	private final StyleContext		content = new StyleContext();
	private final StyledDocument	doc = new DefaultStyledDocument(content);
	private final JTextPane			area = new JTextPane(doc);

	public Console() {
		super(new BorderLayout());
		final DefaultCaret 			caret = (DefaultCaret)area.getCaret();

		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);		
		area.setEditable(false);
		add(new JScrollPane(area),BorderLayout.CENTER);
	}
	
	public void clear() {
		area.setText("");
	}
	
	public void message(final Severity severity, final String message, final Object... parameters) throws NullPointerException {
		if (severity == null) {
			throw new NullPointerException("Severity can't be null"); 
		}
		else if (message == null) {
			throw new NullPointerException("Message can't be null"); 
		}
		else if (parameters == null) {
			throw new NullPointerException("Parameters can't be null reference"); 
		}
		else {
			final String		text = (parameters.length == 0 ? message : String.format(message,parameters)) + '\n';
			final AttributeSet	attr = ATTRIBUTES.getOrDefault(severity,DEFAULT_ATTRIBUTES);

			synchronized(doc) {
				final int 		from = doc.getLength();
				
				try{doc.insertString(from, text, attr);
					area.setCaretPosition(from+text.length());
				} catch (BadLocationException e) {
				}
			}
		}
	}
}
