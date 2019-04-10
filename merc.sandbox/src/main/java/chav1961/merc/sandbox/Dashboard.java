package chav1961.merc.sandbox;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import chav1961.purelib.i18n.interfaces.LocaleResource;
import chav1961.purelib.i18n.interfaces.LocaleResourceLocation;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.ui.interfaces.Format;

public class Dashboard extends JPanel {
	private static final long serialVersionUID = -2493056491796618065L;

	private final Localizer		localizer;
	private final JSeparator	afterExternal = new JSeparator(SwingConstants.HORIZONTAL);

	public Dashboard(final Localizer localizer) {
		if (localizer == null) {
			throw new NullPointerException("Localizer can't be null");
		}
		else {
			final SpringLayout	spring = new SpringLayout();
			
			this.localizer = localizer;
			setLayout(spring);
			
			
			add(afterExternal);
			
		}
	}

	@LocaleResourceLocation(Localizer.LOCALIZER_SCHEME+":prop:chav1961/calc/schemes/powerfactor/mc34262plugin/MC34262")
	public static class ExternalSources {
		
	@LocaleResource(value="innerVoltage",tooltip="innerVoltageTooltip")	
	@Format("10.3ms")
		private float					innerVoltage = 220.0f;
	}
}
