package chav1961.merc.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.lang.merc.LexemaType;
import chav1961.merc.lang.merc.MercHighlighter;
import chav1961.merc.lang.merc.MercHighlighter.HighlightItem;

public class Application extends JFrame {
	private static final long serialVersionUID = 2476913896592024861L;
	
	private static final Map<LexemaType,AttributeSet>	STYLES = new HashMap<>();
	private static final SimpleAttributeSet				ORDINAL_STYLE = new SimpleAttributeSet();
	
	static {
		SimpleAttributeSet	sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.MAGENTA);
		StyleConstants.setBold(sas,true);
		
		STYLES.put(LexemaType.If,sas); 
		STYLES.put(LexemaType.Then,sas); 
		STYLES.put(LexemaType.Else,sas); 
		STYLES.put(LexemaType.For,sas); 
		STYLES.put(LexemaType.In,sas); 
		STYLES.put(LexemaType.Do,sas); 
		STYLES.put(LexemaType.While,sas); 
		STYLES.put(LexemaType.Var,sas); 
		STYLES.put(LexemaType.Func,sas); 
		STYLES.put(LexemaType.Brick,sas); 
		STYLES.put(LexemaType.Break,sas); 
		STYLES.put(LexemaType.Continue,sas); 
		STYLES.put(LexemaType.Return,sas); 
		STYLES.put(LexemaType.Print,sas); 
		STYLES.put(LexemaType.TypeDef,sas); 
		STYLES.put(LexemaType.Lock,sas);

		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.PINK);
		StyleConstants.setBold(sas,true);
		StyleConstants.setItalic(sas,true);
		
		STYLES.put(LexemaType.Type,sas); 
		
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.BLACK);
		StyleConstants.setItalic(sas,true);
		
		STYLES.put(LexemaType.Name,sas);
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.BLACK);
		StyleConstants.setItalic(sas,true);
		StyleConstants.setUnderline(sas,true);
		
		STYLES.put(LexemaType.PredefinedName,sas); 
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.BLUE);
		StyleConstants.setBold(sas,true);
		
		STYLES.put(LexemaType.IntConst,sas); 
		STYLES.put(LexemaType.RealConst,sas);
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.GREEN);

		STYLES.put(LexemaType.StrConst,sas); 
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.BLACK);
		StyleConstants.setBold(sas,true);
		
		STYLES.put(LexemaType.BoolConst,sas); 
		STYLES.put(LexemaType.NullConst,sas);
		STYLES.put(LexemaType.RefConst,sas);
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.BLACK);
		
		STYLES.put(LexemaType.Open,sas); 
		STYLES.put(LexemaType.Close,sas); 
		STYLES.put(LexemaType.OpenB,sas); 
		STYLES.put(LexemaType.CloseB,sas); 
		STYLES.put(LexemaType.OpenF,sas); 
		STYLES.put(LexemaType.CloseF,sas); 
		STYLES.put(LexemaType.Dot,sas); 
		STYLES.put(LexemaType.Colon,sas); 
		STYLES.put(LexemaType.Semicolon,sas); 
		STYLES.put(LexemaType.Period,sas); 
		STYLES.put(LexemaType.Div,sas); 
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.BLACK);
		StyleConstants.setBold(sas,true);
		StyleConstants.setUnderline(sas,true);
		
		STYLES.put(LexemaType.Pipe,sas); 
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.ORANGE);
		
		STYLES.put(LexemaType.Operator,sas); 
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.RED);
		StyleConstants.setBold(sas,true);
		StyleConstants.setStrikeThrough(sas,true);
		
		STYLES.put(LexemaType.Unknown,sas);
		
		sas = new SimpleAttributeSet();
		
		StyleConstants.setForeground(sas,Color.LIGHT_GRAY);
		StyleConstants.setItalic(sas,true);
		
		STYLES.put(LexemaType.EOF,sas); 
		STYLES.put(LexemaType.Comment,sas);
	}
	
	
	private final StyleContext		content = new StyleContext();
	private final StyledDocument	doc = new DefaultStyledDocument(content);
	private final JTextPane			area = new JTextPane(doc);
	private final DocumentListener	listener = new DocumentListener() {
										@Override
										public void removeUpdate(final DocumentEvent e) {
											highlight(doc,area.getText());
										}
										
										@Override
										public void insertUpdate(final DocumentEvent e) {
											highlight(doc,area.getText());
										}
										
										@Override
										public void changedUpdate(final DocumentEvent e) {
											highlight(doc,area.getText());
										}
									}; 
	
	public Application() {
		super("test");
		final Dimension	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(new Dimension(screenSize.width*3/4,screenSize.height*3/4));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		final JSplitPane	split = new JSplitPane();
		
		getContentPane().add(split,BorderLayout.CENTER);
		try{final Screen	screen = new Screen(); 
			
			screen.setPreferredSize(new Dimension(screenSize.width/2,screenSize.height/2));
			split.setLeftComponent(screen);
			split.setRightComponent(new JScrollPane(area));
		} catch (MercContentException | MercEnvironmentException e) {
			e.printStackTrace();
		}
		
		doc.addDocumentListener(listener);
	}

	private void highlight(final StyledDocument doc, final String text) {
		SwingUtilities.invokeLater(()->{
			int	lastEnd = 0;
			
			doc.removeDocumentListener(listener);
			for (HighlightItem item : MercHighlighter.parseString(text.endsWith("\n") ? text : text+'\n')) {
				if (item.from - lastEnd > 1) {
					doc.setCharacterAttributes(lastEnd,item.from - lastEnd,ORDINAL_STYLE,true);
				}
				doc.setCharacterAttributes(item.from,item.length,STYLES.get(item.type),true);
				lastEnd = item.from + item.length;
			}
			doc.addDocumentListener(listener);
		});
	}

	public static void main(String[] args) {
		new Application().setVisible(true);
	}
}
