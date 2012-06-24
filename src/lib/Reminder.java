package lib;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class Reminder {
	ArrayList<Phrase> phrases;
	JLabel text;
	private int count = 0;
	public Reminder(ArrayList<Phrase> phrases, JLabel text) {
		this.phrases = phrases;
		this.text = text;
	}

	public void start() {
		Timer timer = new Timer();
		if (count + 1 < phrases.size()) {
			String strText = phrases.get(count).getPhrase();
			float delay = phrases.get(count).getTime();
			timer.schedule(new Task(text, strText), (long) delay);
		}
	}
	
	class Scheduler {
		Timer timer;
		public Scheduler(float seconds, JLabel text, String strText) {
			timer = new Timer();
			timer.schedule(new Task(text, strText), (long) (seconds * 1000.0));
		}
	}

	class Task extends TimerTask {
		JLabel text;
		String strText;
		
		public Task(JLabel text, String strText) {
			this.text = text;
			this.strText = strText;
		}

		public void run() {
			if (text != null) {
				text.setText(strText);
			}
		}
	}
}
