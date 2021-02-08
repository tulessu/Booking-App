    
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookingSystem extends JFrame implements ActionListener {
private JTextField numberseat, agent, wait;
private JButton createseat, book;
private ArrayList<JTextField> seatList;
private JPanel panel;
private ArrayList<Thread> agents = new ArrayList<Thread>();
private Random random = new Random();
public ArrayList<Integer> randomNumbers;
private ReentrantLock kilit = new ReentrantLock();

public BookingSystem() {
	numberseat = new JTextField();
	numberseat.setSize(100, 25);
	numberseat.setLocation(80, 30);
	numberseat.setText("Number of seats");
	add(numberseat);
	
	agent = new JTextField();
	agent.setSize(110, 25);
	agent.setLocation(220, 30);
	agent.setText("Number of agents");
	add(agent);
	
	wait = new JTextField();
	wait.setSize(100, 25);
	wait.setLocation(360, 30);
	wait.setText("Max waiting time");
	add(wait);
	
	createseat = new JButton();
	createseat.setSize(110,25);
	createseat.setLocation(490, 30);
	createseat.setText("Create seats");
	createseat.addActionListener(this);
	add(createseat);
	
	book = new JButton();
	book.setSize(80,25);
	book.setLocation(630, 30);
	book.setText("Book");
	book.addActionListener(this);
	add(book);
	
	panel = new JPanel();
	panel.setLocation(0, 70);
	panel.setSize(800, 800);
	panel.setLayout(new GridLayout(10,0,10,10));
	
	add(panel);
	
	seatList = new ArrayList<JTextField>();
	
	setLayout(null);
	setTitle("Assignment-2");
	setSize(900, 1000);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true);
}

@Override
public void actionPerformed(ActionEvent e) {
	if(e.getSource().equals(createseat)) {
		panel.setVisible(false);
		int a = Integer.parseInt(numberseat.getText().trim());
		for(int i=0; i<a; i++) {
			seatList.add(i, new JTextField());
			seatList.get(i).setText("Not booked");
			seatList.get(i).setSize(150, 70);
			seatList.get(i).setOpaque(true);
			panel.add(seatList.get(i));
						
		}
		panel.setVisible(true);
		
	}
	
	else if(e.getSource().equals(book)) {
		int b = Integer.parseInt(agent.getText().trim());
		int c = Integer.parseInt(wait.getText().trim());
		randomNumbers = new ArrayList<Integer>();
		
		for(int i=0; i<b; i++) {
			randomNumbers.add(random.nextInt(c) + 1); //Avoiding 0 case
		}
		
		for(int i=0; i<b; i++) {
			agents.add(new Thread(new Runnable() {
				
				@Override
				public void run() {
					book();
				}
			}));
			agents.get(i).start();
		}	
		if(seatList.get(seatList.size()-1).equals(Color.red)) {
			System.out.println("Bitttiii");
		}
		}
}

public void book() {
	String a = Thread.currentThread().getName();
	String[] a2 = a.split("-");
	int myorder = Integer.parseInt(a2[1]) - 1;
	for(int i=0; i<seatList.size(); i++) {
		if(!(seatList.get(i).getBackground().equals(Color.red))) {
			kilit.lock();
			seatList.get(i).setBackground(Color.red);
			seatList.get(i).setText(String.valueOf("Booked by Agent " + myorder));
			kilit.unlock();
			try {
				Thread.currentThread().sleep(randomNumbers.get(myorder-1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	if(Thread.currentThread().equals(agents.get(agents.size() - 1))) {
		createPane();
	}
}

public void createPane() {
	String message = "";
	ArrayList<Integer> paneList = new ArrayList<Integer>();
	int order = 1;
	
	for(int i=0; i<Integer.parseInt(agent.getText().trim()); i++) {
		paneList.add(0);
	}
	
	while(order<=agents.size()) {
		message += "Agent " + order + " booked " + learn(order) + " seats.\n";
		order++;
	}

	JOptionPane.showMessageDialog(null, message);
}

public int learn(int a) {
	int count = 0;
	for(int i=0; i<seatList.size(); i++) {
		if(seatList.get(i).getText().equals("Booked by Agent " + a)) {
			count++;
		}
	}
	return count;
}


public static void main(String[] args) {
	BookingSystem visual = new BookingSystem();
}
}



