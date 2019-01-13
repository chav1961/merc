package chav1961.merc.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import chav1961.merc.lang.merc.LexemaType;
import chav1961.merc.lang.merc.MercHighlighter;
import chav1961.merc.lang.merc.MercHighlighter.HighlightItem;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.ui.swing.interfaces.OnAction;

public class DevelopmentTab extends JPanel {
	private static final long 		serialVersionUID = 8411656157804278365L;
	private static final Icon		RUN_ICON = new ImageIcon(DevelopmentTab.class.getResource("run.png")); 
	private static final Icon		DEBUG_ICON = new ImageIcon(DevelopmentTab.class.getResource("debug.png")); 
	private static final Icon		STOP_ICON = new ImageIcon(DevelopmentTab.class.getResource("stop.png")); 
	private static final Icon		STEP_INTO_ICON = new ImageIcon(DevelopmentTab.class.getResource("stepInto.png")); 
	private static final Icon		STEP_OVER_ICON = new ImageIcon(DevelopmentTab.class.getResource("stepOver.png")); 
	private static final Icon		STEP_RETURN_ICON = new ImageIcon(DevelopmentTab.class.getResource("stepReturn.png")); 
	private static final KeyStroke	KS_RUN = KeyStroke.getKeyStroke(KeyEvent.VK_F11, InputEvent.CTRL_DOWN_MASK);
	private static final KeyStroke	KS_DEBUG = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
	private static final KeyStroke	KS_STOP = KeyStroke.getKeyStroke(KeyEvent.VK_F11, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
	private static final KeyStroke	KS_STEP_INTO = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
	private static final KeyStroke	KS_STEP_OVER = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
	private static final KeyStroke	KS_STEP_RETURN = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);

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

	private final JLabel			message = new JLabel();
	private final StyleContext		content = new StyleContext();
	private final StyledDocument	doc = new DefaultStyledDocument(content);
	private final JTextPane			area = new JTextPane(doc);
	private final DocumentListener	listener = new DocumentListener() {
										@Override public void removeUpdate(final DocumentEvent e) {highlight(doc,area.getText());}
										@Override public void insertUpdate(final DocumentEvent e) {highlight(doc,area.getText());}
										@Override public void changedUpdate(final DocumentEvent e) {highlight(doc,area.getText());}
									}; 
	private final JButton			run = new SmartButton(RUN_ICON,true,(e)->{run();});
	private final JButton			debug = new SmartButton(DEBUG_ICON,true,(e)->{debug();});
	private final JButton			stop = new SmartButton(STOP_ICON,false,(e)->{stop();});
	private final JButton			stepInto = new SmartButton(STEP_INTO_ICON,false,(e)->{stepInto();});
	private final JButton			stepOver = new SmartButton(STEP_OVER_ICON,false,(e)->{stepOver();});
	private final JButton			stepReturn = new SmartButton(STEP_RETURN_ICON,false,(e)->{stepReturn();});
	private final Localizer			localizer;
	
	@FunctionalInterface
	interface MyActionCall {
		void call(ActionEvent event);
	}
	
	public DevelopmentTab(final Localizer localizer) throws NullPointerException {
		super(new BorderLayout());
		if (localizer == null) {
			throw new NullPointerException("Localizer can't be null");
		}
		else {
			final JPanel	statePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			final JToolBar	toolBar = new JToolBar();
			
			this.localizer = localizer;
			
			toolBar.setFloatable(false);
			toolBar.add(run);
			toolBar.add(debug);
			toolBar.addSeparator();
			toolBar.add(stop);
			toolBar.addSeparator();
			toolBar.add(stepInto);
			toolBar.add(stepOver);
			toolBar.add(stepReturn);
	
			assignKeys(KS_RUN,(e)->{run();});
			assignKeys(KS_DEBUG,(e)->{debug();});
			assignKeys(KS_STOP,(e)->{stop();});
			assignKeys(KS_STEP_INTO,(e)->{stepInto();});
			assignKeys(KS_STEP_OVER,(e)->{stepOver();});
			assignKeys(KS_STEP_RETURN,(e)->{stepReturn();});
			
			doc.addDocumentListener(listener);
			statePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
			add(toolBar,BorderLayout.NORTH);
			add(new JScrollPane(area),BorderLayout.CENTER);
			add(statePanel,BorderLayout.SOUTH);
		}
	}

	public String getProgramText() {
		return area.getText();
	}
	
	public void setProgramText(final String text) {
		area.setText(text);
	}

	public void message(final String format, final Object... parameters) {
		message.setText(String.format(format,parameters));
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

	private void assignKeys(final KeyStroke ks, final MyActionCall call) {
		final String	action = "action"+(int)(100000*Math.random());
		
		area.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ks,action);
		area.getActionMap().put(action,
			new AbstractAction() {
				private static final long 		serialVersionUID = 8411656157804278365L;
				@Override
				public void actionPerformed(ActionEvent event) {
					call.call(event);
				}
			}
		);
	}

@OnAction("run")	
	private void run() {
		run.setEnabled(false);
		debug.setEnabled(false);
		stop.setEnabled(true);
		stepInto.setEnabled(false);
		stepOver.setEnabled(false);
		stepReturn.setEnabled(false);
	}

@OnAction("debug")	
	private void debug() {
		run.setEnabled(false);
		debug.setEnabled(false);
		stop.setEnabled(true);
		stepInto.setEnabled(true);
		stepOver.setEnabled(true);
		stepReturn.setEnabled(true);
	}

@OnAction("stop")	
	private void stop() {
		run.setEnabled(true);
		debug.setEnabled(true);
		stop.setEnabled(false);
		stepInto.setEnabled(false);
		stepOver.setEnabled(false);
		stepReturn.setEnabled(false);
	}

@OnAction("stepInto")	
	private void stepInto() {
		
	}

@OnAction("stepOver")	
	private void stepOver() {
		
	}

@OnAction("stepReturn")	
	private void stepReturn() {
		
	}
	
	private static class SmartButton extends JButton {
		private static final long serialVersionUID = -5996829209625137306L;

		private SmartButton(final Icon icon, final boolean initialState, final MyActionCall callback) {
			super(icon);
			setEnabled(initialState);
			addActionListener((e)->{callback.call(e);});
		}
	}
}
