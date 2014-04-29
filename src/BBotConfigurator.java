import giovynet.serial.Baud;
import giovynet.serial.Com;
import giovynet.serial.Parameters;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;

public class BBotConfigurator extends JFrame implements SerialPortEventListener {

	public LinkedList<String> getAvailablePorts() {
		LinkedList<String> list = new LinkedList<>();
	    Enumeration portList = CommPortIdentifier.getPortIdentifiers();
	    while (portList.hasMoreElements()) {
	        CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
	        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	            list.add(portId.getName());
	        }
	    }
	    return list;
	}
	
	private OutputStream out = null;
	private InputStream in = null;
	private BufferedReader input = null;
	
	private void send(char val) {
		try {
			out.write(val);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void send(String val) {
		try {
			for (int i = 0; i < val.length(); i++) {
				out.write(val.charAt(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String read() {
		String val = "";
		try {
			int c = 0;
			while(c != ';') {
				while(in.available() == 0);
				c = in.read();
				val += (char)c;
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return val;
	}
	
	private void initializePort() {
		String port = "COM12";
		try {
			Parameters param = new Parameters();
			param.setPort(port);
			param.setBaudRate(Baud._115200);
			Com com = new Com(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BBotConfigurator() {
		initializePort();
		initialize();
//		System.out.println("Initializing port . . . .");
//		String port = "COM13";
//		try {
//			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
//			SerialPort serialPort =
//					  (SerialPort) portId.open("BBot configurator", 5000);
//			serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
//			out = serialPort.getOutputStream();
//			in = serialPort.getInputStream();
//			input = new BufferedReader(new InputStreamReader(in));
//			serialPort.addEventListener(this);
//			serialPort.notifyOnDataAvailable(true);
//			Timer timer = new Timer();
//			timer.schedule(new TimerTask() {
//				public void run() {
//					System.out.println("Retrieving Parameters");
//					String msg = "";
//					for (int i = 0; i < 89; i++) {
//						send("B:"+i+";");
//						String m = read();
//						msg += m;
//					}
//					System.out.println(msg);
//					initialize();
//					Scanner scan = new Scanner(msg);
//					scan.useDelimiter(";");
//					KpOne.setText(String.format("%.4f", scan.nextDouble())+"");
//					KiOne.setText(String.format("%.4f", scan.nextDouble())+"");
//					KdOne.setText(String.format("%.4f", scan.nextDouble())+"");
//					inPGain.setText(String.format("%.4f", scan.nextDouble())+"");
//					inDGain.setText(String.format("%.4f", scan.nextDouble())+"");
//					outPGain.setText(String.format("%.4f", scan.nextDouble())+"");
//					outIGain.setText(String.format("%.4f", scan.nextDouble())+"");
//					outDGain.setText(String.format("%.4f", scan.nextDouble())+"");
//					for (int i = 0; i < pRules.length; i++) {
//						pRules[i].setText(String.format("%.4f", scan.nextDouble())+"");
//					}
//					for (int i = 0; i < iRules.length; i++) {
//						iRules[i].setText(String.format("%.4f", scan.nextDouble())+"");
//					}
//					for (int i = 0; i < dRules.length; i++) {
//						dRules[i].setText(String.format("%.4f", scan.nextDouble())+"");
//					}
//					speedKp.setText(String.format("%.4f", scan.nextDouble())+"");
//					speedKi.setText(String.format("%.4f", scan.nextDouble())+"");
//					speedKd.setText(String.format("%.4f", scan.nextDouble())+"");
//					posKp.setText(String.format("%.4f", scan.nextDouble())+"");
//					posKi.setText(String.format("%.4f", scan.nextDouble())+"");
//					posKd.setText(String.format("%.4f", scan.nextDouble())+"");
//				}
//			}, 1500);
//		} catch (NoSuchPortException e) {
//			e.printStackTrace();
//		} catch (PortInUseException e) {
//			e.printStackTrace();
//		} catch (UnsupportedCommOperationException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (TooManyListenersException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			out.write(97);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void initialize() {
		initComponents();
	}
	
	private JComboBox<String> stabilizerControllerSelector = null;
	private JComboBox<String> rulesSelector = null;
	private JTextField KpOne = null;
	private JTextField KiOne = null;
	private JTextField KdOne = null;
	private JPanel pidPanel = null;
	private JPanel gsfpidPanel = null;
	private JTextField inPGain = null;
	private JTextField inDGain = null;
	private JTextField outPGain = null;
	private JTextField outIGain = null;
	private JTextField outDGain = null;
	private JTextField[] pRules = null;
	private JTextField[] iRules = null;
	private JTextField[] dRules = null;
	private JPanel pRulesPanel = null;
	private JPanel iRulesPanel = null;
	private JPanel dRulesPanel = null;
	private JTextField speedKp = null;
	private JTextField speedKi = null;
	private JTextField speedKd = null;
	private JTextField posKp = null;
	private JTextField posKi = null;
	private JTextField posKd = null;
	
	private JButton send = null;
	
	private void initComponents() {
		setTitle("BBot Configurator");
		
		Container c = getContentPane();
		GroupLayout layout = new GroupLayout(c);
		c.setLayout(layout);
		
		String[] controllers = {"PID","Gain Scheduled Fuzzy PID"};
		stabilizerControllerSelector = new JComboBox<>(controllers);
		stabilizerControllerSelector.setSelectedIndex(0);
		stabilizerControllerSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selection = stabilizerControllerSelector.getSelectedIndex();
				if(selection==1) {
					pidPanel.setVisible(false);
					gsfpidPanel.setVisible(true);
				}
				else {
					pidPanel.setVisible(true);
					gsfpidPanel.setVisible(false);
				}
			}
		});
		
		KpOne = new JTextField();
		KiOne = new JTextField();
		KdOne = new JTextField();
		JLabel labelOne = new JLabel("Kp");
		JLabel labelTwo = new JLabel("Ki");
		JLabel labelThree = new JLabel("Kd");
		
		pidPanel = new JPanel();
		GroupLayout pLayout = new GroupLayout(pidPanel);
		pidPanel.setLayout(pLayout);
		pLayout.setVerticalGroup(pLayout.createSequentialGroup()
			.addGroup(pLayout.createParallelGroup()
				.addComponent(labelOne, 25, 25, 25)
				.addComponent(KpOne, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(pLayout.createParallelGroup()
				.addComponent(labelTwo, 25, 25, 25)
				.addComponent(KiOne, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(pLayout.createParallelGroup()
				.addComponent(labelThree, 25, 25, 25)
				.addComponent(KdOne, 25, 25, 25)
			)
	    );
		
		pLayout.setHorizontalGroup(pLayout.createSequentialGroup()
			.addGroup(pLayout.createParallelGroup()
				.addComponent(labelOne, 30, 30, 30)
				.addComponent(labelTwo, 30, 30, 30)
				.addComponent(labelThree, 30, 30, 30)
			)
			.addGap(5)
			.addGroup(pLayout.createParallelGroup()
				.addComponent(KpOne, 100, 100, Short.MAX_VALUE)
				.addComponent(KiOne, 100, 100, Short.MAX_VALUE)
				.addComponent(KdOne, 100, 100, Short.MAX_VALUE)
			)
		);
		
		inPGain = new JTextField();
		inDGain = new JTextField();
		outPGain = new JTextField();
		outIGain = new JTextField();
		outDGain = new JTextField();
		
		JLabel labelFour = new JLabel("Input P Gain");
		JLabel labelFive = new JLabel("Input D Gain");
		JLabel labelSix = new JLabel("Output P Gain");
		JLabel labelSeven = new JLabel("Output I Gain");
		JLabel labelEight = new JLabel("Output D Gain");
		
		rulesSelector = new JComboBox<String>(new String[]{"P Rule Base","I Rule Base","D Rule Base"});
		pRules = new JTextField[25];
		for (int i = 0; i < pRules.length; i++) {
			pRules[i] = new JTextField();
			pRules[i].setHorizontalAlignment(JTextField.CENTER);
		}
		pRulesPanel = new JPanel();
		pRulesPanel.setPreferredSize(new Dimension(280,120));
		pRulesPanel.setLayout(new GridLayout(5, 5));
		for (int i = 0; i < pRules.length; i++) {
			pRulesPanel.add(pRules[i]);
		}
		iRules = new JTextField[25];
		for (int i = 0; i < iRules.length; i++) {
			iRules[i] = new JTextField();
			iRules[i].setHorizontalAlignment(JTextField.CENTER);
		}
		iRulesPanel = new JPanel();
		iRulesPanel.setPreferredSize(new Dimension(280,120));
		iRulesPanel.setLayout(new GridLayout(5, 5));
		for (int i = 0; i < iRules.length; i++) {
			iRulesPanel.add(iRules[i]);
		}
		dRules = new JTextField[25];
		for (int i = 0; i < dRules.length; i++) {
			dRules[i] = new JTextField();
			dRules[i].setHorizontalAlignment(JTextField.CENTER);
		}
		dRulesPanel = new JPanel();
		dRulesPanel.setPreferredSize(new Dimension(280,120));
		dRulesPanel.setLayout(new GridLayout(5, 5));
		for (int i = 0; i < dRules.length; i++) {
			dRulesPanel.add(dRules[i]);
		}
		
		rulesSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selection = rulesSelector.getSelectedIndex();
				if(selection == 0) {
					pRulesPanel.setVisible(true);
					iRulesPanel.setVisible(false);
					dRulesPanel.setVisible(false);
				}
				else if(selection == 1) {
					pRulesPanel.setVisible(false);
					iRulesPanel.setVisible(true);
					dRulesPanel.setVisible(false);
				}
				else {
					pRulesPanel.setVisible(false);
					iRulesPanel.setVisible(false);
					dRulesPanel.setVisible(true);
				}
			}
		});
		
		gsfpidPanel = new JPanel();
		GroupLayout gLayout = new GroupLayout(gsfpidPanel);
		gsfpidPanel.setLayout(gLayout);
		gLayout.setVerticalGroup(gLayout.createSequentialGroup()
			.addGroup(gLayout.createParallelGroup()
				.addComponent(labelFour, 25, 25, 25)
				.addComponent(inPGain, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(gLayout.createParallelGroup()
					.addComponent(labelFive, 25, 25, 25)
				.addComponent(inDGain, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(gLayout.createParallelGroup()
					.addComponent(labelSix, 25, 25, 25)
				.addComponent(outPGain, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(gLayout.createParallelGroup()
					.addComponent(labelSeven, 25, 25, 25)
				.addComponent(outIGain, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(gLayout.createParallelGroup()
					.addComponent(labelEight, 25, 25, 25)
				.addComponent(outDGain, 25, 25, 25)
			)
			.addGap(5)
			.addComponent(rulesSelector, 25, 25, 25)
			.addGap(5)
			.addComponent(pRulesPanel)
			.addComponent(iRulesPanel)
			.addComponent(dRulesPanel)
	    );
		gLayout.setHorizontalGroup(gLayout.createParallelGroup()
			.addGroup(gLayout.createSequentialGroup()
				.addGroup(gLayout.createParallelGroup()
					.addComponent(labelFour, 80, 80, 80)
					.addComponent(labelFive)
					.addComponent(labelSix)
					.addComponent(labelSeven)
					.addComponent(labelEight)
				)
				.addGap(5)
				.addGroup(gLayout.createParallelGroup()
					.addComponent(inPGain, 100, 100, Short.MAX_VALUE)
					.addComponent(inDGain, 100, 100, Short.MAX_VALUE)
					.addComponent(outPGain, 100, 100, Short.MAX_VALUE)
					.addComponent(outIGain, 100, 100, Short.MAX_VALUE)
					.addComponent(outDGain, 100, 100, Short.MAX_VALUE)
				)
			)
			.addComponent(rulesSelector, 100, 100, 100)
			.addComponent(pRulesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			.addComponent(iRulesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			.addComponent(dRulesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			
		);
		
		gsfpidPanel.setVisible(false);
		iRulesPanel.setVisible(false);
		dRulesPanel.setVisible(false);
		
		JPanel stabilizerPane = new JPanel();
		GroupLayout layoutOne = new GroupLayout(stabilizerPane);
		stabilizerPane.setLayout(layoutOne);
		layoutOne.setVerticalGroup(layoutOne.createSequentialGroup()
			.addGap(5)
			.addComponent(stabilizerControllerSelector, 25, 25, 25)
			.addGap(5)
			.addComponent(pidPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
			.addComponent(gsfpidPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
			.addGap(5)
	    );
		layoutOne.setHorizontalGroup(layoutOne.createSequentialGroup()
			.addGap(5)
			.addGroup(layoutOne.createParallelGroup()
					.addComponent(stabilizerControllerSelector, 175, 175, 175)
					.addComponent(pidPanel)
					.addComponent(gsfpidPanel)
			)
			.addGap(5)
		);
		JPanel positionPane = new JPanel();
		
		stabilizerPane.setBorder(BorderFactory.createTitledBorder("Stabilization"));
		stabilizerPane.setPreferredSize(new Dimension(300,350));
		
		positionPane.setBorder(BorderFactory.createTitledBorder("Position Control"));
		positionPane.setPreferredSize(new Dimension(300,190));
		
		speedKp = new JTextField();
		speedKi = new JTextField();
		speedKd = new JTextField();
		
		posKp = new JTextField();
		posKi = new JTextField();
		posKd = new JTextField();
		
		JLabel lOne = new JLabel("Speed Kp");
		JLabel lTwo = new JLabel("Speed Ki");
		JLabel lThree = new JLabel("Speed Kd");
		JLabel lFour = new JLabel("Position Kp");
		JLabel lFive = new JLabel("Position Ki");
		JLabel lSix = new JLabel("Position Kd");
		
		GroupLayout posLayout = new GroupLayout(positionPane);
		positionPane.setLayout(posLayout);
		
		posLayout.setVerticalGroup(posLayout.createSequentialGroup()
			.addGroup(posLayout.createParallelGroup()
				.addComponent(lOne, 25, 25, 25)
				.addComponent(speedKp, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(posLayout.createParallelGroup()
				.addComponent(lTwo, 25, 25, 25)
				.addComponent(speedKi, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(posLayout.createParallelGroup()
				.addComponent(lThree, 25, 25, 25)
				.addComponent(speedKd, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(posLayout.createParallelGroup()
				.addComponent(lFour, 25, 25, 25)
				.addComponent(posKp, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(posLayout.createParallelGroup()
				.addComponent(lFive, 25, 25, 25)
				.addComponent(posKi, 25, 25, 25)
			)
			.addGap(2)
			.addGroup(posLayout.createParallelGroup()
				.addComponent(lSix, 25, 25, 25)
				.addComponent(posKd, 25, 25, 25)
			)
		);
		
		posLayout.setHorizontalGroup(posLayout.createSequentialGroup()
			.addGap(5)
			.addGroup(posLayout.createParallelGroup()
				.addComponent(lOne, 100, 100, 100)
				.addComponent(lTwo)
				.addComponent(lThree)
				.addComponent(lFour)
				.addComponent(lFive)
				.addComponent(lSix)
			)
			.addGap(5)
			.addGroup(posLayout.createParallelGroup()
				.addComponent(speedKp, 100, 100, Short.MAX_VALUE)
				.addComponent(speedKi, 100, 100, Short.MAX_VALUE)
				.addComponent(speedKd, 100, 100, Short.MAX_VALUE)
				.addComponent(posKp, 100, 100, Short.MAX_VALUE)
				.addComponent(posKi, 100, 100, Short.MAX_VALUE)
				.addComponent(posKd, 100, 100, Short.MAX_VALUE)
			)
			.addGap(5)
		);
		
		send = new JButton("Save");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LinkedList<String> dataList = new LinkedList<>();
				dataList.add(KpOne.getText());
				dataList.add(KiOne.getText());
				dataList.add(KdOne.getText());
				dataList.add(inPGain.getText());
				dataList.add(inDGain.getText());
				dataList.add(outPGain.getText());
				dataList.add(outIGain.getText());
				dataList.add(outDGain.getText());
				for (int i = 0; i < pRules.length; i++) {
					dataList.add(pRules[i].getText());
				}
				for (int i = 0; i < iRules.length; i++) {
					dataList.add(iRules[i].getText());
				}
				for (int i = 0; i < dRules.length; i++) {
					dataList.add(dRules[i].getText());
				}
				dataList.add(speedKp.getText());
				dataList.add(speedKi.getText());
				dataList.add(speedKd.getText());
				dataList.add(posKp.getText());
				dataList.add(posKi.getText());
				dataList.add(posKd.getText());
				Iterator<String> iter = dataList.iterator();
				int index = 0;
				while(iter.hasNext()) {
					double val = Double.parseDouble(iter.next());
					String msg = "C:"+index;
					index++;
					msg += ":";
					msg += String.format("%.4f", val);
					msg += ";";
					System.out.println(msg);
					send(msg);
				}
				System.out.println("Settings Sent");
			}
		});
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGap(5, 5, Short.MAX_VALUE)
			.addComponent(stabilizerPane, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			.addGap(5, 5, 5)
			.addComponent(positionPane, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			.addGap(5, 5, 5)
			.addComponent(send, 30, 30, 30)
			.addGap(5, 5, Short.MAX_VALUE)
	    );
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGap(5, 5, Short.MAX_VALUE)
			.addGroup(layout.createParallelGroup(Alignment.CENTER)
					.addComponent(stabilizerPane, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(positionPane, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(send, 80, 80, 80)
			)
			.addGap(5, 5, Short.MAX_VALUE)
				
		);
		
		pack();
		setResizable(false);
		setAlwaysOnTop(true);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new BBotConfigurator();
	}

	public void serialEvent(SerialPortEvent eve) {
	}
}
