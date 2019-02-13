package chav1961.merc.content;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URI;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import chav1961.purelib.basic.ArgParser;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.SystemErrLoggerFacade;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.exceptions.CommandLineParametersException;
import chav1961.purelib.basic.exceptions.ConsoleCommandException;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.LoggerFacade.Severity;
import chav1961.purelib.fsys.FileSystemFactory;
import chav1961.purelib.i18n.LocalizerFactory;
import chav1961.purelib.i18n.PureLibLocalizer;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.i18n.interfaces.Localizer.LocaleChangeListener;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;
import chav1961.purelib.nanoservice.NanoServiceFactory;
import chav1961.purelib.ui.swing.SwingModelUtils;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JCreoleEditor;
import chav1961.purelib.ui.swing.useful.JFileContentManipulator;
import chav1961.purelib.ui.swing.useful.JStateString;

public class Application extends JFrame implements LocaleChangeListener {
	private static final long 		serialVersionUID = -3061028320843379171L;
	
	public static final String		APPLICATION_TITLE = "Application.title";
	public static final String		MESSAGE_FILE_LOADED = "Application.message.fileLoaded";
	public static final String		MESSAGE_FILE_SAVED = "Application.message.fileSaved";
	
	private final ContentMetadataInterface 	app;
	private final Localizer			localizer;
	private final int 				localHelpPort;
	private final CountDownLatch	latch;
	private final JMenuBar			menu;
	private final JCreoleEditor		editor = new JCreoleEditor();
	private final JStateString		state;
	private final JFileContentManipulator	manipulator;
	private final DocumentListener	listener = new DocumentListener() {
										@Override public void removeUpdate(DocumentEvent e) {manipulator.setModificationFlag();}
										@Override public void insertUpdate(DocumentEvent e) {manipulator.setModificationFlag();}				
										@Override public void changedUpdate(DocumentEvent e) {manipulator.setModificationFlag();}
									};
																		
	
	public Application(final ContentMetadataInterface app, final Localizer parent, final int localHelpPort, final CountDownLatch latch) throws EnvironmentException, NullPointerException, IllegalArgumentException, IOException {
		if (app == null) {
			throw new NullPointerException("Application descriptor can't be null");
		}
		else if (parent == null) {
			throw new NullPointerException("Parent localizer can't be null");
		}
		else if (latch == null) {
			throw new NullPointerException("Latch to notify closure can't be null");
		}
		else {
			this.app = app;
			this.localizer = LocalizerFactory.getLocalizer(app.getRoot().getLocalizerAssociated());
			this.localHelpPort = localHelpPort;
			this.latch = latch;
			this.state = new JStateString(this.localizer,10);
			
			parent.push(localizer);
			localizer.addLocaleChangeListener(this);
			this.menu = SwingModelUtils.toMenuEntity(app.byUIPath(URI.create("ui:/model/navigation.top.mainmenu")), JMenuBar.class);
			
			SwingUtils.assignActionListeners(menu,this);
			SwingUtils.centerMainWindow(this,0.75f);
			SwingUtils.assignExitMethod4MainWindow(this,()->{exitApplication();});
			
			getContentPane().add(menu,BorderLayout.NORTH);
			getContentPane().add(new JScrollPane(editor),BorderLayout.CENTER);
			getContentPane().add(state,BorderLayout.SOUTH);

			this.manipulator = new JFileContentManipulator(FileSystemFactory.createFileSystem(URI.create("fsys:file:./")),localizer,editor);
			editor.getDocument().addDocumentListener(listener);
			fillLocalizedStrings();
		}
	}
	
	@Override
	public void localeChanged(final Locale oldLocale, final Locale newLocale) throws LocalizationException {
		fillLocalizedStrings();
	}

	@OnAction(value="action:/newFile",async=true)
	private void newFile () throws IOException {
		manipulator.newFile();
	}

	@OnAction(value="action:/openFile",async=true)
	private void openFile() throws IOException, LocalizationException {
		if (manipulator.openFile(state)) {
			state.message(Severity.info, localizer.getValue(MESSAGE_FILE_LOADED), manipulator.getCurrentPathOfTheFile());
			editor.getDocument().removeDocumentListener(listener);
			manipulator.clearModificationFlag();
			SwingUtilities.invokeLater(()->{editor.getDocument().addDocumentListener(listener);});
			refillLru();
		}
	}

	private void openFile(final String file) throws IOException, LocalizationException {
		if (manipulator.openFile(file,state)) {
			state.message(Severity.info, localizer.getValue(MESSAGE_FILE_LOADED), manipulator.getCurrentPathOfTheFile());
			editor.getDocument().removeDocumentListener(listener);
			manipulator.clearModificationFlag();
			SwingUtilities.invokeLater(()->{editor.getDocument().addDocumentListener(listener);});
		}
	}
	
	@OnAction(value="action:/saveFile",async=true)
	private void saveFile() throws IOException, LocalizationException {
		if (manipulator.saveFile(state)) {
			state.message(Severity.info, localizer.getValue(MESSAGE_FILE_SAVED), manipulator.getCurrentPathOfTheFile());
		}
	}

	@OnAction(value="action:/saveFileAs",async=true)
	private void saveFileAs() throws IOException, LocalizationException {
		if (manipulator.saveFileAs(state)) {
			state.message(Severity.info, localizer.getValue(MESSAGE_FILE_SAVED), manipulator.getCurrentPathOfTheFile());
		}
	}
	
	@OnAction("action:/exit")
	private void exitApplication () {
		try{manipulator.close();
		} catch (IOException e) {
			state.message(Severity.error,e,e.getLocalizedMessage());
		} finally {
			setVisible(false);
			dispose();
			latch.countDown();
		}
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
	
	@OnAction("action:/startBrowser")
	private void startBrowser () {
		if (Desktop.isDesktopSupported()) {
			try{Desktop.getDesktop().browse(URI.create("http://localhost:"+localHelpPort+"/index.html"));
			} catch (IOException exc) {
				exc.printStackTrace();
			}
		}
	}

	private void refillLru() {
		final ContentNodeMetadata	node = app.byUIPath(URI.create("action"));
		final JMenu					lru = (JMenu)SwingUtils.findComponentByName(this.menu,node.getUIPath().toString());

		lru.removeAll();
		for (String item : manipulator.getLastUsed()) {
			final JMenuItem			menu = new JMenuItem(item);
			final String			fileItem = item;
			
			menu.addActionListener((e)->{
				try{openFile(fileItem);
				} catch (LocalizationException | IOException exc) {
					state.message(Severity.error,exc,exc.getLocalizedMessage());
				}
			});
			lru.add(menu);
		}
	}
	
	public static void main(String[] args) {
		try{final ArgParser						parser = new ApplicationArgParser().parse(args);
			final int							helpPort = parser.isTyped("helpport") ? getFreePort() : parser.getValue("hepport", int.class);
			final SubstitutableProperties		props = new SubstitutableProperties(Utils.mkProps(
																	 NanoServiceFactory.NANOSERVICE_PORT, ""+helpPort
																	,NanoServiceFactory.NANOSERVICE_ROOT, "fsys:file:/mercury/merc/merc.static/src/main/resources"
																	,NanoServiceFactory.NANOSERVICE_CREOLE_PROLOGUE_URI, Application.class.getResource("prolog.cre").toString() 
																	,NanoServiceFactory.NANOSERVICE_CREOLE_EPILOGUE_URI, Application.class.getResource("epilog.cre").toString() 
																	));
		
			try(final LoggerFacade				logger = new SystemErrLoggerFacade();
				final InputStream				is = Application.class.getResourceAsStream("application.xml");
				final Localizer					localizer = new PureLibLocalizer();
				final NanoServiceFactory		service = new NanoServiceFactory(logger,props)) {
				final ContentMetadataInterface	xda = ContentModelFactory.forXmlDescription(is);
				final CountDownLatch			latch = new CountDownLatch(1);
					
				new Application(xda,localizer,helpPort,latch).setVisible(true);
				service.start();
				latch.await();
				service.stop();
			} catch (IOException | EnvironmentException | InterruptedException  e) {
				e.printStackTrace();
				System.exit(129);
			}
		} catch (ConsoleCommandException | CommandLineParametersException e) {
			e.printStackTrace();
			System.exit(128);
		} catch (IOException | ContentException e) {
			e.printStackTrace();
			System.exit(129);
		}
		System.exit(0);
	}

	private static int getFreePort() throws IOException {
		try (ServerSocket 	socket = new ServerSocket(0)) {
			return socket.getLocalPort();
		}
	}
	
	static class ApplicationArgParser extends ArgParser {
		public ApplicationArgParser() {
			super(new IntegerArg("helpport",false,"help system port",0));
		}
	}
}
