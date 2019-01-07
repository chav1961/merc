package chav1961.merc.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;

public class Application extends JFrame {
	private static final long serialVersionUID = 2476913896592024861L;

	public Application() {
		super("test");
		setSize(new Dimension(800,800));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try{getContentPane().add(new Screen(),BorderLayout.CENTER);
		} catch (MercContentException | MercEnvironmentException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Application().setVisible(true);
	}
}
