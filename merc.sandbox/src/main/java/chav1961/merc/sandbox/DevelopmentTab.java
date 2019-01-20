package chav1961.merc.sandbox;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import chav1961.merc.lang.merc.MercHighlighter;
import chav1961.merc.lang.merc.interfaces.LexemaType;
import chav1961.purelib.fsys.FileSystemFactory;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.ui.HighlightItem;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JFileContentManipulator;
import chav1961.purelib.ui.swing.useful.JTextPaneHighlighter;

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

	private final JLabel					message = new JLabel();
	private final ProgramEditor				area = new ProgramEditor();
	private final JButton					run = new SmartButton(RUN_ICON,true,(e)->{run();});
	private final JButton					debug = new SmartButton(DEBUG_ICON,true,(e)->{debug();});
	private final JButton					stop = new SmartButton(STOP_ICON,false,(e)->{stop();});
	private final JButton					stepInto = new SmartButton(STEP_INTO_ICON,false,(e)->{stepInto();});
	private final JButton					stepOver = new SmartButton(STEP_OVER_ICON,false,(e)->{stepOver();});
	private final JButton					stepReturn = new SmartButton(STEP_RETURN_ICON,false,(e)->{stepReturn();});
	private final Localizer					localizer;
	private final JFileContentManipulator	fileManipulator;
	
	@FunctionalInterface
	interface MyActionCall {
		void call(ActionEvent event);
	}
	
	public DevelopmentTab(final Localizer localizer) throws NullPointerException, IllegalArgumentException, IOException {
		super(new BorderLayout());
		if (localizer == null) {
			throw new NullPointerException("Localizer can't be null");
		}
		else {
			final JPanel	statePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			final JToolBar	toolBar = new JToolBar();
			
			this.localizer = localizer;
			this.fileManipulator = new JFileContentManipulator(FileSystemFactory.createFileSystem(URI.create("fsys:file:./")),localizer,area);
			this.area.getDocument().addDocumentListener(new DocumentListener() {
				@Override public void removeUpdate(DocumentEvent e) {fileManipulator.setModificationFlag();}				
				@Override public void insertUpdate(DocumentEvent e) {fileManipulator.setModificationFlag();}
				@Override public void changedUpdate(DocumentEvent e) {fileManipulator.setModificationFlag();}
			});
			
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
			
			statePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
			add(toolBar,BorderLayout.NORTH);
			add(new JScrollPane(area),BorderLayout.CENTER);
			add(statePanel,BorderLayout.SOUTH);
		}
	}

	public JFileContentManipulator getContentManipulator() {
		return fileManipulator;
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
	
	private static class ProgramEditor extends JTextPaneHighlighter<LexemaType> {
		private static final long 	serialVersionUID = 6195635634177844711L;

		{	SimpleAttributeSet	sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.MAGENTA);
			StyleConstants.setBold(sas,true);
			
			characterStyles.put(LexemaType.If,sas); 
			characterStyles.put(LexemaType.Then,sas); 
			characterStyles.put(LexemaType.Else,sas); 
			characterStyles.put(LexemaType.For,sas); 
			characterStyles.put(LexemaType.In,sas); 
			characterStyles.put(LexemaType.Do,sas); 
			characterStyles.put(LexemaType.While,sas); 
			characterStyles.put(LexemaType.Var,sas); 
			characterStyles.put(LexemaType.Func,sas); 
			characterStyles.put(LexemaType.Brick,sas); 
			characterStyles.put(LexemaType.Break,sas); 
			characterStyles.put(LexemaType.Continue,sas); 
			characterStyles.put(LexemaType.Return,sas); 
			characterStyles.put(LexemaType.Print,sas); 
			characterStyles.put(LexemaType.TypeDef,sas); 
			characterStyles.put(LexemaType.Lock,sas);

			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.PINK);
			StyleConstants.setBold(sas,true);
			StyleConstants.setItalic(sas,true);
			
			characterStyles.put(LexemaType.Type,sas); 
			
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.BLACK);
			StyleConstants.setItalic(sas,true);
			
			characterStyles.put(LexemaType.Name,sas);
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.BLACK);
			StyleConstants.setItalic(sas,true);
			StyleConstants.setUnderline(sas,true);
			
			characterStyles.put(LexemaType.PredefinedName,sas); 
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.BLUE);
			StyleConstants.setBold(sas,true);
			
			characterStyles.put(LexemaType.IntConst,sas); 
			characterStyles.put(LexemaType.RealConst,sas);
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.GREEN);

			characterStyles.put(LexemaType.StrConst,sas); 
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.BLACK);
			StyleConstants.setBold(sas,true);
			
			characterStyles.put(LexemaType.BoolConst,sas); 
			characterStyles.put(LexemaType.NullConst,sas);
			characterStyles.put(LexemaType.RefConst,sas);
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.BLACK);
			
			characterStyles.put(LexemaType.Open,sas); 
			characterStyles.put(LexemaType.Close,sas); 
			characterStyles.put(LexemaType.OpenB,sas); 
			characterStyles.put(LexemaType.CloseB,sas); 
			characterStyles.put(LexemaType.OpenF,sas); 
			characterStyles.put(LexemaType.CloseF,sas); 
			characterStyles.put(LexemaType.Dot,sas); 
			characterStyles.put(LexemaType.Colon,sas); 
			characterStyles.put(LexemaType.Semicolon,sas); 
			characterStyles.put(LexemaType.Period,sas); 
			characterStyles.put(LexemaType.Div,sas); 
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.BLACK);
			StyleConstants.setBold(sas,true);
			StyleConstants.setUnderline(sas,true);
			
			characterStyles.put(LexemaType.Pipe,sas); 
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.ORANGE);
			
			characterStyles.put(LexemaType.Operator,sas); 
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.RED);
			StyleConstants.setBold(sas,true);
			StyleConstants.setStrikeThrough(sas,true);
			
			characterStyles.put(LexemaType.Unknown,sas);
			
			sas = new SimpleAttributeSet();
			
			StyleConstants.setForeground(sas,Color.LIGHT_GRAY);
			StyleConstants.setItalic(sas,true);
			
			characterStyles.put(LexemaType.EOF,sas); 
			characterStyles.put(LexemaType.Comment,sas);
		}
		
		@Override
		protected HighlightItem<LexemaType>[] parseString(final String program) {
			return MercHighlighter.parseString(program.endsWith("\n") ? program : program+"\n");
		}
	}
}
