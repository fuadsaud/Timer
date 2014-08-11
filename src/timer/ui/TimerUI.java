package timer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import timer.Timer;
import timer.event.TimeEvent;
import timer.event.TimeListener;
import timer.sound.Ringer;

public class TimerUI extends JFrame {

	private static Calendar today;

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -9215858720955149622L;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new TimerUI().setVisible(true);
	}

	private final SimpleDateFormat timeSecondsFormat = new SimpleDateFormat("HH:mm:ss");

	private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private JPanel tooglePanel;

	private JPanel dateTimePanel;

	private JLabel currentTimeLabel;

	private JLabel currentDateLabel;

	private JLabel ringTimeLabel;

	private JTextField newTimeTextField;

	private JButton toogle;

	private Timer timer;

	private Ringer ringer;

	private JCheckBoxMenuItem active;

	private JFileChooser fileChooser;

	public TimerUI() {
		super("Timer by @fuadsaud");

		timer = new Timer();
		timer.addTimeListener(new TimeListener() {

			@Override
			public void timeReached(TimeEvent e) {
				ring();
			}
		});

		initComponents();

		setSize(500, 250);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				TimerUI.class.getResource("/resources/timer-icon256.png")));

		try {
			ringer = new Ringer(new File("./beep.wav"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		new TimeWorker().execute();
	}

	private void ring() {
		ringer.ring();
	}

	private void updateTime() {
		Calendar agora = Calendar.getInstance();

		currentTimeLabel.setText(timeSecondsFormat.format(agora.getTime()));

		if (today.get(Calendar.HOUR_OF_DAY) > agora.get(Calendar.HOUR_OF_DAY)) {
			currentDateLabel.setText(dateFormat.format(agora.getTime()));
		}
	}

	private void chooseSound() {
		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				ringer.setSound(file);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Invalid file", "Error",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	private void initComponents() {
		initMenu();
		initDateTimePanel();
		initTooglePanel();
		initFileChooser();
	}

	private void initDateTimePanel() {
		dateTimePanel = new JPanel(new MigLayout(new LC().noGrid()));

		Calendar now = Calendar.getInstance();

		currentTimeLabel = new JLabel(timeSecondsFormat.format(now.getTime()));
		currentTimeLabel.setFont(currentTimeLabel.getFont().deriveFont(28f));

		currentDateLabel = new JLabel(dateFormat.format(now.getTime()));
		currentDateLabel.setFont(currentDateLabel.getFont().deriveFont(28f));
		today = now;

		ringTimeLabel = new JLabel("__:__");
		ringTimeLabel.setFont(ringTimeLabel.getFont().deriveFont(45f));
		ringTimeLabel.setForeground(Color.RED);

		try {
			MaskFormatter formatter = new MaskFormatter("##:##");
			formatter.setPlaceholderCharacter('_');
			formatter.setOverwriteMode(true);

			newTimeTextField = new JFormattedTextField(formatter);
			newTimeTextField.setFont(newTimeTextField.getFont().deriveFont(45f));
			newTimeTextField.setBackground(dateTimePanel.getBackground());
			newTimeTextField.setBorder(null);

			newTimeTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						setRingTime();
					}
				}
			});
		} catch (ParseException e) {
			e.printStackTrace();
		}

		dateTimePanel.add(currentTimeLabel, "gapbefore 80, gapafter 70, gapbottom 30");
		dateTimePanel.add(currentDateLabel, "wrap");
		dateTimePanel.add(ringTimeLabel, "gapbefore 80, gapafter 70");
		dateTimePanel.add(newTimeTextField);

		add(dateTimePanel, BorderLayout.CENTER);
	}

	private void initFileChooser() {
		fileChooser = new JFileChooser();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("WAV Audio", "wav", "wave");

		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setFileFilter(filter);
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu timer = new JMenu("Timer");

		active = new JCheckBoxMenuItem("Ativar");
		active.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toogle();
			}
		});
		timer.add(active);

		timer.addSeparator();

		final JCheckBoxMenuItem alwaysOnTop = new JCheckBoxMenuItem("Always on top");
		alwaysOnTop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAlwaysOnTop(alwaysOnTop.isSelected());
			}
		});
		timer.add(alwaysOnTop);

		timer.addSeparator();

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		timer.add(exit);

		menuBar.add(timer);

		JMenu sound = new JMenu("Sound");

		JMenuItem chooseSound = new JMenuItem("Choose Sound...");
		chooseSound.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chooseSound();
			}
		});
		sound.add(chooseSound);

		JMenuItem testSound = new JMenuItem("Test sound");
		testSound.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ring();
				JOptionPane.showMessageDialog(null, "Testing...", null,
						JOptionPane.INFORMATION_MESSAGE);
				ringer.stop();
			}
		});
		sound.add(testSound);

		menuBar.add(sound);

		JMenu help = new JMenu("Help");

		JMenuItem about = new JMenuItem("About Timer...");
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				about();
			}
		});
		help.add(about);

		menuBar.add(help);

		setJMenuBar(menuBar);
	}

	private void initTooglePanel() {
		toogle = new JButton("Activate");
		toogle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toogle();
			}
		});

		tooglePanel = new JPanel(new MigLayout(new LC().fillX()));
		tooglePanel.add(toogle, "grow");

		add(tooglePanel, BorderLayout.SOUTH);
	}

	private void setActive(boolean active) {
		if (timer.getRingTime() != null) {
			timer.setAtivo(active);

			toogle.setText(active ? "Deactivate" : "Activate");
			this.active.setSelected(active);

			try {
				ringer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}

			ringTimeLabel.setForeground(active ? new Color(0, 125, 0) : Color.RED);
		} else {
			newTimeTextField.requestFocus();
		}
	}

	private void setRingTime() {
		String[] newTime = newTimeTextField.getText().split(":");

		int hour = Integer.parseInt(newTime[0]);
		int minute = Integer.parseInt(newTime[1]);

		if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
			return;
		}

		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, hour);
		date.set(Calendar.MINUTE, minute);

		timer.setHoraAlarme(date);

		ringTimeLabel.setText(timeFormat.format(timer.getRingTime().getTime()));

		setActive(true);
	}

	private void about() {
		new AboutDialog(this).setVisible(true);
	}

	private void toogle() {
		setActive(!timer.isActive());
	}

	class TimeWorker extends SwingWorker<String, Object> {

		@Override
		protected String doInBackground() throws Exception {
			while (true) {
				updateTime();

				Thread.sleep(100);
			}
		}
	}
}
