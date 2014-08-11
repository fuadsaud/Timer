package timer;

import java.util.ArrayList;
import java.util.Calendar;

import timer.event.TimeEvent;
import timer.event.TimeListener;

public class Timer {

	private ArrayList<TimeListener> listeners;

	private Calendar horaAlarme;

	private boolean ativo;

	public Timer() {
		ativo = false;

		listeners = new ArrayList<TimeListener>();
	}

	public void addTimeListener(TimeListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public Calendar getRingTime() {
		return horaAlarme;
	}

	public boolean isActive() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
		if (ativo) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					checaTempo();
				}
			}).start();
		}

	}

	public void setHoraAlarme(Calendar horaAlarme) {
		this.horaAlarme = horaAlarme;
	}

	private void checaTempo() {
		while (isActive()) {
			if (eHoraAtual(horaAlarme)) {
				TimeEvent te = new TimeEvent(this, horaAlarme);
				fireTimeReached(te);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean eHoraAtual(Calendar calendar) {
		return System.currentTimeMillis() / 100 == calendar.getTimeInMillis() / 100;
	}

	protected void fireTimeReached(TimeEvent event) {
		for (TimeListener listener : listeners) {
			listener.timeReached(event);
		}
	}
}
