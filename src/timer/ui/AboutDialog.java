package timer.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

/**
 * A dialog containing information about Zephyr Text Editor.
 * 
 * @author Fuad Saud
 * 
 */
public class AboutDialog extends JDialog {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -5597623599632133128L;

	public AboutDialog(JFrame owner) {
		super(owner, "About Timer", true);

		setLayout(new MigLayout("wrap 1", "[center][right][left][c]", "[top][center][b]"));

		initLabels();

		pack();

		Dimension d = getSize();
		setSize(d.width - 20, d.height);
		setResizable(false);
		setLocationRelativeTo(owner);
	}

	private void browserCall() {
		try {
			Desktop.getDesktop().browse(new URI("http://twitter.com/fuadsaud"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initLabels() {
		add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/resources/timer-icon256.png")))), "align center");

		JLabel timer = new JLabel("Timer");
		timer.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(timer, "align center");

		JLabel version = new JLabel("0.0.1");
		version.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		add(version, "align center");

		add(new JLabel("Copyleft (c) 2012, Fuad Saud. Couple of rights reserved :)"),
				"align center");

		JLabel url = new JLabel("http://twitter.com/fuadsaud");
		url.setForeground(Color.BLUE);
		url.setCursor(new Cursor(Cursor.HAND_CURSOR));
		url.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				browserCall();
			}

		});

		add(url, "align center");
	}
}
