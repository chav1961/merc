package chav1961.merc.content;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import chav1961.purelib.basic.SystemErrLoggerFacade;
import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.fsys.FileSystemFactory;
import chav1961.purelib.i18n.PureLibLocalizer;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.i18n.interfaces.Localizer.LocaleChangeListener;
import chav1961.purelib.ui.XMLDescribedApplication;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JCreoleEditor;
import chav1961.purelib.ui.swing.useful.JFileContentManipulator;

public class Application extends JFrame implements LocaleChangeListener {
	private static final long 	serialVersionUID = -3061028320843379171L;
	
	private final Localizer			localizer;
	private final JMenuBar			menu;
	private final JCreoleEditor		editor = new JCreoleEditor();
	private final JLabel			state = new JLabel();
	private final JFileContentManipulator	manipulator;
	
	public Application(final XMLDescribedApplication app, final Localizer parent) throws EnvironmentException, NullPointerException, IllegalArgumentException, IOException {
		if (app == null) {
			throw new NullPointerException("Application descriptor can't be null");
		}
		else if (parent == null) {
			throw new NullPointerException("Parent localizer can't be null");
		}
		else {
			this.localizer = app.getLocalizer();
			parent.push(localizer);
			localizer.addLocaleChangeListener(this);
			menu = app.getEntity("mainmenu",JMenuBar.class,null);
			
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			SwingUtils.assignActionListeners(menu,this);
			SwingUtils.centerMainWindow(this,0.75f);
			addWindowListener(new WindowListener() {
				@Override 
				public void windowClosing(WindowEvent e) {
					exitApplication();
				}

				@Override public void windowOpened(WindowEvent e) {}
				@Override public void windowClosed(WindowEvent e) {}
				@Override public void windowIconified(WindowEvent e) {}
				@Override public void windowDeiconified(WindowEvent e) {}
				@Override public void windowActivated(WindowEvent e) {}
				@Override public void windowDeactivated(WindowEvent e) {}
			});
			
//			split.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(SwingUtils.KS_HELP,SwingUtils.ACTION_HELP);
//			split.getActionMap().put(SwingUtils.ACTION_HELP,new AbstractAction() {private static final long serialVersionUID = 1L;
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					if (Desktop.isDesktopSupported()) {
//						try{Desktop.getDesktop().browse(URI.create("http://localhost:"+localHelpPort+"/index.html"));
//						} catch (IOException exc) {
//							exc.printStackTrace();
//						}
//					}
//					else {
//						try{console.message(Severity.warning,localizer.getValue(Constants.WARNING_NO_DESKTOP_AVAILABLE));
//						} catch (LocalizationException exc) {
//							exc.printStackTrace();
//						}
//					}
//				}
//			});

//			editor.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(SwingUtils.KS_HELP,SwingUtils.ACTION_HELP);
//			editor.getActionMap().put(SwingUtils.ACTION_HELP,new AbstractAction() {private static final long serialVersionUID = 1L;
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
			
			final JPanel	statePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			statePanel.setPreferredSize(new Dimension(25,25));
			statePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			
			getContentPane().add(menu,BorderLayout.NORTH);
			getContentPane().add(new JScrollPane(editor),BorderLayout.CENTER);
			getContentPane().add(statePanel,BorderLayout.SOUTH);

			this.manipulator = new JFileContentManipulator(FileSystemFactory.createFileSystem(URI.create("fsys:file:./")),localizer,editor);
			editor.getDocument().addDocumentListener(new DocumentListener() {
				@Override public void removeUpdate(DocumentEvent e) {manipulator.setModificationFlag();}
				@Override public void insertUpdate(DocumentEvent e) {manipulator.setModificationFlag();}				
				@Override public void changedUpdate(DocumentEvent e) {manipulator.setModificationFlag();}
			});
			fillLocalizedStrings();
		}
	}
	
	@Override
	public void localeChanged(final Locale oldLocale, final Locale newLocale) throws LocalizationException {
		fillLocalizedStrings();
	}

	@OnAction("newFile")
	private void newFile () throws IOException {
		manipulator.newFile();
	}

	@OnAction("openFile")
	private void openFile() throws IOException {
		manipulator.openFile();
	}

	@OnAction("saveFile")
	private void saveFile() throws IOException {
		manipulator.saveFile();
	}

	@OnAction("saveFileAs")
	private void saveFileAs() throws IOException {
		manipulator.saveFileAs();
	}
	
	@OnAction("exit")
	private void exitApplication () {
		try{manipulator.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setVisible(false);
		dispose();
	}
	
	@OnAction("builtin.languages:en")
	private void selectEnglish() throws LocalizationException, NullPointerException {
		localizer.setCurrentLocale(Locale.forLanguageTag("en"));
	}
	
	@OnAction("builtin.languages:ru")
	private void selectRussian() throws LocalizationException, NullPointerException {
		localizer.setCurrentLocale(Locale.forLanguageTag("ru"));
	}
	
	private void fillLocalizedStrings() throws LocalizationException {
//		setTitle(localizer.getValue(Constants.APPLICATION_TITLE));
		if (menu instanceof LocaleChangeListener) {
			((LocaleChangeListener)menu).localeChanged(localizer.currentLocale().getLocale(),localizer.currentLocale().getLocale());
		}
	}
	
	
	public static void main(String[] args) {
		try(final LoggerFacade				logger = new SystemErrLoggerFacade();
			final InputStream				is = Application.class.getResourceAsStream("application.xml");
			final Localizer					localizer = new PureLibLocalizer()) {
			final XMLDescribedApplication	xda = new XMLDescribedApplication(is,logger);
				
			new Application(xda,localizer).setVisible(true);
		} catch (EnvironmentException  e) {
			e.printStackTrace();
//			System.exit(128);
		} catch (IOException e) {
			e.printStackTrace();
	//		System.exit(129);
		}
		//System.exit(0);
	}

}
