package chav1961.merc.content;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import chav1961.purelib.basic.ArgParser;
import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.exceptions.CommandLineParametersException;
import chav1961.purelib.basic.exceptions.ConsoleCommandException;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.LoggerFacade.Severity;
import chav1961.purelib.i18n.LocalizerFactory;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.i18n.interfaces.Localizer.LocaleChangeListener;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JStateString;

public class Application extends JFrame implements LocaleChangeListener {
	private static final long 		serialVersionUID = -3061028320843379171L;

	public static final String		ARG_HELP_PORT = "helpport";
	
	public static final String		APPLICATION_TITLE = "Application.title";
	public static final String		MESSAGE_FILE_LOADED = "Application.message.fileLoaded";
	public static final String		MESSAGE_FILE_SAVED = "Application.message.fileSaved";
	
	private final ContentMetadataInterface 	app;
	private final Localizer			localizer;
	private final int 				localHelpPort;
	private final CountDownLatch	latch;
	private final JMenuBar			menu;
	private final JTabbedPane		tabber = new JTabbedPane(); 
	private final List<CreoleEditorTab>	tabs = new ArrayList<>();
	private final JStateString		state;
																		
	
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
			this.menu = SwingUtils.toJComponent(app.byUIPath(URI.create("ui:/model/navigation.top.mainmenu")), JMenuBar.class);
			
			SwingUtils.assignActionListeners(menu,this);
			SwingUtils.centerMainWindow(this,0.75f);
			SwingUtils.assignExitMethod4MainWindow(this,()->{exitApplication();});
			
			getContentPane().add(menu,BorderLayout.NORTH);
			getContentPane().add(tabber,BorderLayout.CENTER);
			getContentPane().add(state,BorderLayout.SOUTH);

			fillLocalizedStrings();
		}
	}
	
	@Override
	public void localeChanged(final Locale oldLocale, final Locale newLocale) throws LocalizationException {
		fillLocalizedStrings();
	}

	@OnAction(value="action:/newFile",async=true)
	private void newFile () throws IOException {
		final CreoleEditorTab	newTab = new CreoleEditorTab(localizer, state);
		
		tabs.add(newTab);
		tabber.addTab("(*)",new JScrollPane(newTab.editor));
		tabber.setSelectedIndex(tabs.size()-1);
		newTab.manipulator.newFile();
	}

	@OnAction(value="action:/openFile",async=true)
	private void openFile() throws IOException, LocalizationException {
		if (tabber.getTabCount() == 0) {
			newFile();
		}
		
		final CreoleEditorTab	currentTab = tabs.get(tabber.getSelectedIndex()); 
		
		if (currentTab.manipulator.openFile(state)) {
			state.message(Severity.info, localizer.getValue(MESSAGE_FILE_LOADED), currentTab.manipulator.getCurrentPathOfTheFile());
			currentTab.turnOffDocumentListener();
			currentTab.manipulator.clearModificationFlag();
			SwingUtilities.invokeLater(()->{currentTab.turnOnDocumentListener();});
			tabber.setTitleAt(tabber.getSelectedIndex(), currentTab.manipulator.getCurrentNameOfTheFile());
			tabber.setToolTipTextAt(tabber.getSelectedIndex(), currentTab.manipulator.getCurrentPathOfTheFile());
			refillLru();
		}
	}

	private void openFile(final String file) throws IOException, LocalizationException {
		if (tabber.getTabCount() == 0) {
			newFile();
		}
		
		final CreoleEditorTab	currentTab = tabs.get(tabber.getSelectedIndex());
		
		if (currentTab.manipulator.openFile(file,state)) {
			state.message(Severity.info, localizer.getValue(MESSAGE_FILE_LOADED), currentTab.manipulator.getCurrentPathOfTheFile());
			currentTab.turnOffDocumentListener();
			currentTab.manipulator.clearModificationFlag();
			SwingUtilities.invokeLater(()->{currentTab.turnOnDocumentListener();});
			tabber.setTitleAt(tabber.getSelectedIndex(), currentTab.manipulator.getCurrentNameOfTheFile());
			tabber.setToolTipTextAt(tabber.getSelectedIndex(), currentTab.manipulator.getCurrentPathOfTheFile());
		}
	}
	
	@OnAction(value="action:/saveFile",async=true)
	private void saveFile() throws IOException, LocalizationException {
		if (tabber.getTabCount() == 0) {
			newFile();
		}		

		final CreoleEditorTab	currentTab = tabs.get(tabber.getSelectedIndex());
		
		if (currentTab.manipulator.saveFile(state)) {
			state.message(Severity.info, localizer.getValue(MESSAGE_FILE_SAVED), currentTab.manipulator.getCurrentPathOfTheFile());
		}
	}

	@OnAction(value="action:/saveFileAs",async=true)
	private void saveFileAs() throws IOException, LocalizationException {
		if (tabber.getTabCount() == 0) {
			newFile();
		}		

		final CreoleEditorTab	currentTab = tabs.get(tabber.getSelectedIndex());
		
		if (currentTab.manipulator.saveFileAs(state)) {
			state.message(Severity.info, localizer.getValue(MESSAGE_FILE_SAVED), currentTab.manipulator.getCurrentPathOfTheFile());
			tabber.setTitleAt(tabber.getSelectedIndex(), currentTab.manipulator.getCurrentNameOfTheFile());
			tabber.setToolTipTextAt(tabber.getSelectedIndex(), currentTab.manipulator.getCurrentPathOfTheFile());
			refillLru();
		}
	}
	
	@OnAction("action:/exit")
	private void exitApplication () {
		try{for (CreoleEditorTab currentTab : tabs) {
				currentTab.manipulator.close();
			}
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
		setTitle(localizer.getValue(APPLICATION_TITLE));
		if (menu instanceof LocaleChangeListener) {
			((LocaleChangeListener)menu).localeChanged(localizer.currentLocale().getLocale(),localizer.currentLocale().getLocale());
		}
		for (CreoleEditorTab currentTab : tabs) {
			((LocaleChangeListener)currentTab).localeChanged(localizer.currentLocale().getLocale(),localizer.currentLocale().getLocale());
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
		final ContentNodeMetadata	node = app.byUIPath(URI.create("ui:/model/navigation.top.mainmenu/navigation.node.menu.file/navigation.node.menu.file.lru"));
		final JMenu					lru = (JMenu)SwingUtils.findComponentByName(this.menu,node.getName());

		lru.removeAll();
		for (CreoleEditorTab currentTab : tabs) {
			for (String item : currentTab.manipulator.getLastUsed()) {
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
	}
	
	public static void main(String[] args) {
		try{final ArgParser						parser = new ApplicationArgParser().parse(args);
			final int							helpPort = !parser.isTyped(ARG_HELP_PORT) ? getFreePort() : parser.getValue(ARG_HELP_PORT, int.class);
		
			try(final LoggerFacade				logger = PureLibSettings.CURRENT_LOGGER;
				final InputStream				is = Application.class.getResourceAsStream("application.xml");
				final Localizer					localizer = PureLibSettings.PURELIB_LOCALIZER) {
				final ContentMetadataInterface	xda = ContentModelFactory.forXmlDescription(is);
				final CountDownLatch			latch = new CountDownLatch(1);
					
				new Application(xda,localizer,helpPort,latch).setVisible(true);
				latch.await();
			} catch (IOException | EnvironmentException | InterruptedException  e) {
				e.printStackTrace();
				System.exit(129);
			}
		} catch (CommandLineParametersException e) {
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
			super(new IntegerArg(ARG_HELP_PORT,false,"help system port",0));
		}
	}
}
