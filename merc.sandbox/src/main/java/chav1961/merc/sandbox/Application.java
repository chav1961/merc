package chav1961.merc.sandbox;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URI;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.purelib.basic.ArgParser;
import chav1961.purelib.basic.ArgParserTest;
import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.SystemErrLoggerFacade;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.exceptions.ConsoleCommandException;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.LoggerFacade.Severity;
import chav1961.purelib.i18n.LocalizerFactory;
import chav1961.purelib.i18n.PureLibLocalizer;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.i18n.interfaces.Localizer.LocaleChangeListener;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;
import chav1961.purelib.nanoservice.NanoServiceFactory;
import chav1961.purelib.ui.XMLDescribedApplication;
import chav1961.purelib.ui.swing.SwingModelUtils;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JStateString;

public class Application extends JFrame implements LocaleChangeListener {
	private static final long serialVersionUID = 2476913896592024861L;
	
	private final Localizer			localizer;
	private final int				localHelpPort;
	private final CountDownLatch 	latch;
	private final JMenuBar			menu;
	private final JTabbedPane		tabbed = new JTabbedPane();
	private final Screen			screen;
	private final DevelopmentTab	devTab;
	private final Console			console = new Console();
	private final JStateString		state;
	
	public Application(final ContentMetadataInterface app, final Localizer parent, final int localHelpPort, final CountDownLatch latch) throws NullPointerException, IllegalArgumentException, EnvironmentException, MercContentException, IOException {
		if (app == null) {
			throw new NullPointerException("Application descriptor can't be null");
		}
		else if (parent == null) {
			throw new NullPointerException("Parent localizer can't be null");
		}
		else if (latch == null) {
			throw new NullPointerException("Latch can't be null");
		}
		else {
			this.localizer = LocalizerFactory.getLocalizer(app.byUIPath(URI.create(ContentMetadataInterface.UI_SCHEME+":/model")).getLocalizerAssociated());
			this.localHelpPort = localHelpPort;
			this.latch = latch;
			this.state = new JStateString(localizer,1);
			
			parent.push(localizer);
			localizer.addLocaleChangeListener(this);
			
			this.menu = SwingModelUtils.toMenuEntity(app.byUIPath(URI.create(ContentMetadataInterface.UI_SCHEME+":/model/navigation.top.mainmenu")),JMenuBar.class); 
			SwingUtils.assignActionListeners(this.menu,this);
		
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
			
			final JSplitPane	split = new JSplitPane();
			final JSplitPane	leftPart = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			
			tabbed.addTab("Program",devTab = new DevelopmentTab(PureLibSettings.PURELIB_LOCALIZER));
			tabbed.addTab("Economics",new Dashboard(PureLibSettings.PURELIB_LOCALIZER));

			split.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(SwingUtils.KS_HELP,SwingUtils.ACTION_HELP);
			split.getActionMap().put(SwingUtils.ACTION_HELP,new AbstractAction() {private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e) {
					if (Desktop.isDesktopSupported()) {
						try{Desktop.getDesktop().browse(URI.create("http://localhost:"+localHelpPort+"/index.html"));
						} catch (IOException exc) {
							exc.printStackTrace();
						}
					}
					else {
						try{console.message(Severity.warning,localizer.getValue(Constants.WARNING_NO_DESKTOP_AVAILABLE));
						} catch (LocalizationException exc) {
							exc.printStackTrace();
						}
					}
				}
			});
			
			
			screen = new Screen(); 
			final Dimension	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				
			screen.setPreferredSize(new Dimension(screenSize.width/2,screenSize.height/2));
			leftPart.setLeftComponent(screen);
			leftPart.setRightComponent(console);
			
			split.setLeftComponent(leftPart);
			split.setRightComponent(tabbed);

			getContentPane().add(menu,BorderLayout.NORTH);
			getContentPane().add(split,BorderLayout.CENTER);
			getContentPane().add(state,BorderLayout.SOUTH);
			state.message(Severity.info,"Welcome!");
			fillLocalizedStrings();
		}
	}

	@OnAction("action:/newProgram")
	private void newProgram() throws IOException {
		devTab.getContentManipulator().newFile(state);
	}

	@OnAction("action:/openProgram")
	private void openProgram() throws IOException {
		devTab.getContentManipulator().openFile(state);
	}

	@OnAction("action:/saveProgram")
	private void saveProgram() throws IOException {
		devTab.getContentManipulator().saveFile(state);
	}

	@OnAction("action:/saveProgramAs")
	private void saveProgramAs() throws IOException {
		devTab.getContentManipulator().saveFileAs(state);
	}
	
	@OnAction("action:/exit")
	private void exitApplication () {
		try{devTab.getContentManipulator().close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			setVisible(false);
			dispose();
			latch.countDown();
		}
	}
	
	@Override
	public void localeChanged(final Locale oldLocale, final Locale newLocale) throws LocalizationException {
		fillLocalizedStrings();
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
		setTitle(localizer.getValue(Constants.APPLICATION_TITLE));
		if (menu instanceof LocaleChangeListener) {
			((LocaleChangeListener)menu).localeChanged(localizer.currentLocale().getLocale(),localizer.currentLocale().getLocale());
		}
	}

	private static int getFreePort() throws IOException {
		try (ServerSocket 	socket = new ServerSocket(0)) {
			return socket.getLocalPort();
		}
	}
	
	public static void main(final String[] args) {		
		try{final ArgParser						parser = new ApplicationArgParser();
			final int							helpPort = getFreePort();
			final SubstitutableProperties		props = new SubstitutableProperties(Utils.mkProps("nanoservicePort",""+helpPort,"nanoserviceRoot","fsys:fsys:jar:/mercury/merc/merc.static/target/merc.static.0.0.1-SNAPSHOT.jar"));
			
			parser.parse(args);
			
			try(final LoggerFacade				logger = new SystemErrLoggerFacade();
				final InputStream				is = Application.class.getResourceAsStream("application.xml");
				final Localizer					localizer = new PureLibLocalizer();
				final NanoServiceFactory		service = new NanoServiceFactory(logger,props)) {
//				final XMLDescribedApplication	xda = new XMLDescribedApplication(is,logger);
				final ContentMetadataInterface 	metadata = ContentModelFactory.forXmlDescription(is);				
				final CountDownLatch			latch = new CountDownLatch(1);
				
				new Application(metadata,localizer,helpPort,latch).setVisible(true);
				service.start();
				latch.await();
				service.stop();
			} catch (EnvironmentException | ContentException | InterruptedException e) {
				e.printStackTrace();
				System.exit(128);
			}
		} catch (IOException | ConsoleCommandException e) {
			e.printStackTrace();
			System.exit(128);
		}
		System.exit(0);
	}

}
