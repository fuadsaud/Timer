package timer.event;

import java.util.Calendar;
import java.util.EventObject;

public class TimeEvent extends EventObject {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 6242176290910728182L;

	private final Calendar time;

	public TimeEvent(Object source, Calendar time) {
		super(source);
		this.time = time;
	}

	public Calendar getTime() {
		return time;
	}

}
