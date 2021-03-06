/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ips.epos.gui;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.io.Tcp;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ips.epos.actors.TcpServerActor;
import com.ips.epos.actors.tcpClient;
import com.ips.epos.protocols.*;
import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.logging.*;
import javax.swing.JFrame;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Pranav
 */
public class SIMULATORx extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 8748836741109262420L;
	public static volatile boolean isConnected = false;
	private final ObjectMapper mapper;
	private ActorSystem system;
	private ActorRef tcpSender;
	private String printFlag;
	private ActorSystem systemX = null;
	private Properties config;
	private static final String propertiesPath = "config/config.properties";
	boolean isServer = false;

	/**
	 * Creates new form SIMULATORx
	 */
	public SIMULATORx() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(SIMULATORx.class.getResource("/IPSLOGO.png")));
		getContentPane().setSize(new Dimension(1300, 900));
		getContentPane().setMaximumSize(new Dimension(1300, 2147483647));
		getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		getContentPane().setName("MyContentPane");
		loadProperties();
		setResizable(false);
		setSize(new Dimension(800, 600));
		this.mapper = new ObjectMapper();
		this.mapper.setSerializationInclusion(Include.NON_NULL);
		initComponents();
		startStatusMessageListener();
	}

	private void loadProperties() {
		config = new Properties();
		try {
			InputStream in = new FileInputStream(propertiesPath);
			config.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("config missing exception -> " + e.getMessage());
			System.exit(ABORT);
		} catch (IOException e) {
			System.err.println("config missing exception -> " + e.getMessage());
			System.exit(ABORT);
		}
		//if(config.getProperty("Connection_Type").equalsIgnoreCase("TCP_IP")){
	}

	private void initComponents() {
		if (config!=null||config.getProperty("Mode").equalsIgnoreCase("Server")) {
			isServer = true;
		}

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("IPS Link SIMULATOR v 1.0.0");
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setLocation(new java.awt.Point(250, 250));
		setLocationByPlatform(true);
		setType(java.awt.Window.Type.POPUP);
		getContentPane().setLayout(new BorderLayout(0, 0));
		mainPanel = new javax.swing.JPanel();

		mainPanel.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainXpannel = new javax.swing.JPanel();
		mainXpannel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		statusReceiptPanel = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jScrollPane2.setBounds(17, 141, 321, 472);
		receiptField = new javax.swing.JTextArea();
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane1.setBounds(17, 23, 321, 107);
		statusMessageField = new javax.swing.JTextArea();
		statusMessageField.setLineWrap(true);
		statusMessageField.setFont(new Font("Monospaced", Font.BOLD, 12));
		buttonsPannel = new javax.swing.JPanel();
		paymentButton = new javax.swing.JButton();
		paymentButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		paymentButton.setBounds(17, 18, 240, 33);
		reversalButton = new javax.swing.JButton();
		reversalButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		reversalButton.setBounds(313, 18, 240, 33);
		refundButton = new javax.swing.JButton();
		refundButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		refundButton.setBounds(17, 62, 240, 33);
		firstDllButton = new javax.swing.JButton();
		firstDllButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		firstDllButton.setBounds(17, 150, 240, 33);
		xReportButton = new javax.swing.JButton();
		xReportButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		xReportButton.setBounds(17, 106, 240, 33);
		zReportButton = new javax.swing.JButton();
		zReportButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		zReportButton.setBounds(313, 106, 240, 33);
		terminalStatusButton = new javax.swing.JButton();
		terminalStatusButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		terminalStatusButton.setBounds(17, 194, 240, 33);
		reprintReceiptButton = new javax.swing.JButton();
		reprintReceiptButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		reprintReceiptButton.setBounds(313, 62, 240, 33);
		startButton = new javax.swing.JButton();
		startButton.setBounds(27, 353, 107, 21);
		stopButton = new javax.swing.JButton();
		stopButton.setBounds(430, 353, 107, 21);
		jScrollPane3 = new javax.swing.JScrollPane();
		jScrollPane3.setBounds(17, 277, 536, 65);
		details = new javax.swing.JTextArea();
		details.setFont(new Font("Monospaced", Font.PLAIN, 12));
		jLabel2 = new javax.swing.JLabel();
		jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel1 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jLabel4 = new javax.swing.JLabel();
		jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jLabel3 = new javax.swing.JLabel();
		jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		terminalIpField = new javax.swing.JTextField();
		terminalIpField.setHorizontalAlignment(SwingConstants.CENTER);
		terminalIpField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		terminalPortField = new javax.swing.JTextField();
		terminalPortField.setHorizontalAlignment(SwingConstants.CENTER);
		terminalPortField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		amountField = new javax.swing.JTextField();
		amountField.setHorizontalAlignment(SwingConstants.CENTER);
		amountField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jLabel5 = new javax.swing.JLabel();
		jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		statusMessageIpField = new javax.swing.JTextField();
		statusMessageIpField.setHorizontalAlignment(SwingConstants.CENTER);
		statusMessageIpField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		statusMessagePortField = new javax.swing.JTextField();
		statusMessagePortField.setHorizontalAlignment(SwingConstants.CENTER);
		statusMessagePortField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jLabel6 = new javax.swing.JLabel();
		jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GTmessage = new javax.swing.JTextField();
		GTmessage.setHorizontalAlignment(SwingConstants.CENTER);
		GTmessage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jLabel7 = new javax.swing.JLabel();
		jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel7.setFont(new Font("Tempus Sans ITC", Font.BOLD, 16));

		mainXpannel.setBackground(new java.awt.Color(153, 153, 153));
		mainXpannel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
				new java.awt.Color(240, 240, 240), new java.awt.Color(240, 240, 240), new java.awt.Color(240, 240, 240),
				new java.awt.Color(240, 240, 240)));

		statusReceiptPanel.setBackground(new java.awt.Color(204, 204, 204));
		statusReceiptPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.WHITE, new Color(240, 240, 240), Color.BLACK, new Color(102, 102, 102)));

		receiptField.setFont(new Font("Monospaced", Font.PLAIN, 12)); // NOI18N
		receiptField.setLineWrap(true);
		receiptField.setRows(20);
		receiptField.setWrapStyleWord(true);
		receiptField.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.WHITE, new Color(240, 240, 240), new Color(0, 0, 0), new Color(102, 102, 102)));
		jScrollPane2.setViewportView(receiptField);
		statusMessageField.setRows(3);
		statusMessageField.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED,
				new java.awt.Color(255, 255, 255), new java.awt.Color(240, 240, 240), new java.awt.Color(0, 0, 0),
				new java.awt.Color(102, 102, 102)));
		jScrollPane1.setViewportView(statusMessageField);

		buttonsPannel.setBackground(new java.awt.Color(204, 204, 204));
		buttonsPannel.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(255, 255, 255), new Color(240, 240, 240), Color.BLACK, new Color(102, 102, 102)));

		paymentButton.setText("PAYMENT");
		paymentButton.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, new Color(0, 0, 0), new Color(102, 102, 102)));
		paymentButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				paymentButtonMouseClicked(evt);
			}
		});

		reversalButton.setText("REVERSAL");
		reversalButton.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), Color.WHITE, new Color(0, 0, 0), new Color(102, 102, 102)));
		reversalButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				reversalButtonMouseClicked(evt);
			}
		});

		refundButton.setText("REFUND");
		refundButton.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), Color.WHITE, new Color(0, 0, 0), new Color(102, 102, 102)));
		refundButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				refundButtonMouseClicked(evt);
			}
		});

		firstDllButton.setText("FIRST DLL");
		firstDllButton.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), Color.WHITE, new Color(0, 0, 0), new Color(102, 102, 102)));
		firstDllButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				firstDllButtonMouseClicked(evt);
			}
		});

		xReportButton.setText("PED BALANCE");
		xReportButton.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), Color.WHITE, new Color(0, 0, 0), new Color(102, 102, 102)));
		xReportButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				xReportButtonMouseClicked(evt);
			}
		});

		zReportButton.setText("END OF DAY");
		zReportButton.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), Color.WHITE, new Color(0, 0, 0), new Color(102, 102, 102)));
		zReportButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				zReportButtonMouseClicked(evt);
			}
		});

		terminalStatusButton.setText("PED STATUS");
		terminalStatusButton.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), Color.WHITE, new Color(0, 0, 0), new Color(102, 102, 102)));
		terminalStatusButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				terminalStatusButtonMouseClicked(evt);
			}
		});

		reprintReceiptButton.setText("REPRINT RECEIPT");
		reprintReceiptButton.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), Color.WHITE, new Color(0, 0, 0), new Color(102, 102, 102)));
		reprintReceiptButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				reprintReceiptButtonMouseClicked(evt);
			}
		});

		startButton.setFont(new Font("Tahoma", Font.BOLD, 12)); // NOI18N
		startButton.setForeground(new java.awt.Color(0, 153, 51));
		startButton.setText("START");
		startButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
				java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));
		startButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				startButtonMouseClicked(evt);
			}
		});

		stopButton.setBackground(new java.awt.Color(255, 255, 255));
		stopButton.setFont(new Font("Tahoma", Font.BOLD, 12)); // NOI18N
		stopButton.setForeground(java.awt.Color.red);
		stopButton.setText("STOP");
		stopButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
				java.awt.Color.white, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));
		stopButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				stopButtonMouseClicked(evt);
			}
		});

		details.setColumns(20);
		details.setRows(5);
		details.setText("WELCOME! CHECK THE DETAILS ABOVE THEN ->\nPRESS START TO START SERVICES!");
		details.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED,
				java.awt.Color.white, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));
		jScrollPane3.setViewportView(details);

		jLabel2.setFont(new Font("Gill Sans Ultra Bold Condensed", Font.BOLD, 28)); // NOI18N
		jLabel2.setForeground(java.awt.SystemColor.menu);
		jLabel2.setText("POINT OF SALE...!!");

		jLabel1.setBackground(new java.awt.Color(255, 255, 255));
		jLabel1.setFont(new java.awt.Font("Eras Bold ITC", 1, 48)); // NOI18N
		jLabel1.setForeground(new java.awt.Color(255, 255, 255));
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setIcon(new ImageIcon(SIMULATORx.class.getResource("/IPSLOGO.png"))); // NOI18N
		jLabel1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		jLabel1.setOpaque(true);

		jPanel1.setBackground(new java.awt.Color(204, 204, 204));
		jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED,
				java.awt.Color.white, new java.awt.Color(240, 240, 240), new java.awt.Color(0, 0, 0),
				new java.awt.Color(102, 102, 102)));

		jLabel4.setText("AMOUNT (IN PENCE)");
		jLabel4.setOpaque(true);

		jLabel3.setText(isServer ? "PED IP+PORT" : "PED COM-PORT");
		jLabel3.setOpaque(true);

		printFlagField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"ECR", "PED"}));
		printFlagField.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
				java.awt.Color.white, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));

		terminalIpField.setText(config.getProperty("Ped_Ip", "000.000.000.000"));
		terminalIpField.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED,
				new java.awt.Color(204, 204, 204), null, java.awt.Color.black, new java.awt.Color(102, 102, 102)));
		terminalIpField.setVisible(isServer ? true : false);

		terminalPortField.setText(config.getProperty(isServer ? "Ped_Port" : "Ped_COM-Port", "0000"));
		terminalPortField.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED,
				java.awt.Color.lightGray, java.awt.Color.white, java.awt.Color.black, java.awt.Color.darkGray));

		amountField.setText("10");
		amountField.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED,
				java.awt.Color.white, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));

		jLabel5.setText("STATUSMESSAGE IP+PORT");
		jLabel5.setOpaque(true);

		statusMessageIpField.setText(config.getProperty("Status_Message_Ip", "000.000.000.000"));
		statusMessageIpField
		.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED,
				java.awt.Color.white, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));

		statusMessagePortField.setText(config.getProperty("Status_Message_Port", "0000"));
		statusMessagePortField
		.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED,
				java.awt.Color.white, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));

		jLabel6.setText("Data for GT");
				jLabel6.setOpaque(true);

				GTmessage.setText("test_gt");
				GTmessage.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED,
						java.awt.Color.white, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));

				jLabel7.setText("IPS LINK SERVER EDITION");
				jLabel7.setOpaque(true);
				printFlagField.setFont(new Font("Tahoma", Font.PLAIN, 12));
				buttonsPannel.setLayout(null);
				buttonsPannel.add(jScrollPane3);
				buttonsPannel.add(refundButton);
				buttonsPannel.add(paymentButton);
				buttonsPannel.add(xReportButton);
				buttonsPannel.add(terminalStatusButton);
				buttonsPannel.add(zReportButton);
				buttonsPannel.add(reprintReceiptButton);
				buttonsPannel.add(reversalButton);
				buttonsPannel.add(firstDllButton);
				buttonsPannel.add(startButton);
				buttonsPannel.add(stopButton);
				statusReceiptPanel.setLayout(null);
				statusReceiptPanel.add(jScrollPane2);
				statusReceiptPanel.add(jScrollPane1);

				JButton manualDllButton = new JButton("UPDATE DLL");
				manualDllButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						manualDllButtonClicked(e);
					}
				});
				manualDllButton.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), Color.WHITE, new Color(0, 0, 0), new Color(128, 128, 128)));
				manualDllButton.setFont(new Font("Tahoma", Font.BOLD, 12));
				manualDllButton.setBounds(313, 150, 240, 33);
				buttonsPannel.add(manualDllButton);

				JButton lastTransStatusButton = new JButton("LAST TRANS STATUS");
				lastTransStatusButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						lastTransStatusButtonClicked(e);
					}
				});
				lastTransStatusButton.setFont(new Font("Tahoma", Font.BOLD, 12));
				lastTransStatusButton.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), Color.WHITE, new Color(0, 0, 0), Color.GRAY));
				lastTransStatusButton.setBounds(313, 194, 240, 33);
				buttonsPannel.add(lastTransStatusButton);

				JButton btnProbePed = new JButton("PROBE PED");
				btnProbePed.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						probePedButtonClicked(e);
					}
				});
				btnProbePed.setFont(new Font("Tahoma", Font.BOLD, 12));
				btnProbePed.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, Color.DARK_GRAY, Color.BLACK));
				btnProbePed.setBounds(17, 238, 536, 28);
				buttonsPannel.add(btnProbePed);

				JLabel lblWaitCard = new JLabel("Wait 4 Card Removed");
				lblWaitCard.setHorizontalAlignment(SwingConstants.CENTER);
				lblWaitCard.setOpaque(true);
				lblWaitCard.setFont(new Font("Tahoma", Font.PLAIN, 12));

				wait4CardRemoved = new JCheckBox("ON/OFF");
				wait4CardRemoved.setFont(new Font("Tahoma", Font.PLAIN, 10));
				wait4CardRemoved.setHorizontalAlignment(SwingConstants.CENTER);

				JLabel lblServerIpport = new JLabel("SERVER IP+PORT");
				lblServerIpport.setHorizontalAlignment(SwingConstants.CENTER);
				lblServerIpport.setOpaque(true);
				lblServerIpport.setFont(new Font("Tahoma", Font.PLAIN, 12));

				serverIp = new JTextField();
				serverIp.setHorizontalAlignment(SwingConstants.CENTER);
				serverIp.setFont(new Font("Tahoma", Font.PLAIN, 12));
				serverIp.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, UIManager.getColor("RadioButtonMenuItem.acceleratorSelectionForeground"), Color.DARK_GRAY));
				serverIp.setText(config.getProperty("Service_Ip", "000.000.000.000"));
				serverIp.setColumns(10);

				serverPort = new JTextField();
				serverPort.setHorizontalAlignment(SwingConstants.CENTER);
				serverPort.setFont(new Font("Tahoma", Font.PLAIN, 12));
				serverPort.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, Color.BLACK, Color.DARK_GRAY));
				serverPort.setText(config.getProperty("Service_Port", "0000"));
				serverPort.setColumns(10);

				JLabel lblPrintDevice = new JLabel("PRINT DEVICE");
				lblPrintDevice.setHorizontalAlignment(SwingConstants.CENTER);
				lblPrintDevice.setOpaque(true);
				lblPrintDevice.setFont(new Font("Tahoma", Font.PLAIN, 12));
				GroupLayout gl_jPanel1 = new GroupLayout(jPanel1);
				gl_jPanel1.setHorizontalGroup(
						gl_jPanel1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jPanel1.createSequentialGroup()
								.addGap(36)
								.addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_jPanel1.createSequentialGroup()
												.addComponent(lblServerIpport, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGap(156))
										.addGroup(gl_jPanel1.createSequentialGroup()
												.addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_jPanel1.createSequentialGroup()
																.addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
																.addGap(42))
														.addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
														.addGroup(gl_jPanel1.createSequentialGroup()
																.addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
																.addGap(69)))
												.addGap(97))
										.addGroup(gl_jPanel1.createSequentialGroup()
												.addComponent(jLabel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGap(196))
										.addGroup(gl_jPanel1.createSequentialGroup()
												.addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING)
														.addComponent(lblWaitCard, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
														.addGroup(gl_jPanel1.createSequentialGroup()
																.addComponent(lblPrintDevice, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addGap(48)))
												.addGap(130)))
								.addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING)
										.addComponent(printFlagField, 0, 111, Short.MAX_VALUE)
										.addComponent(wait4CardRemoved, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
										.addComponent(GTmessage, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
										.addComponent(amountField, 111, 111, 111)
										.addComponent(statusMessageIpField, 111, 111, 111)
										.addComponent(terminalIpField, 111, 111, 111)
										.addComponent(serverIp, 111, 111, 111))
								.addGap(56)
								.addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_jPanel1.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(serverPort, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
										.addComponent(terminalPortField)
										.addComponent(statusMessagePortField))
								.addGap(89))
						);
				gl_jPanel1.setVerticalGroup(
						gl_jPanel1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jPanel1.createSequentialGroup()
								.addGap(14)
								.addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_jPanel1.createSequentialGroup()
												.addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE)
														.addComponent(serverIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(serverPort, GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE)
														.addComponent(terminalIpField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(terminalPortField, GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE)
														.addComponent(statusMessageIpField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(statusMessagePortField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(amountField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(GTmessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(wait4CardRemoved, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(printFlagField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGap(57))
										.addGroup(gl_jPanel1.createSequentialGroup()
												.addComponent(lblServerIpport, GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(jLabel6)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(lblWaitCard)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(lblPrintDevice, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGap(65))))
						);
				jPanel1.setLayout(gl_jPanel1);

				JLabel lblPranavkvalitorcom = new JLabel("pranav.kapoor@ips-inter.co.uk");
				lblPranavkvalitorcom.setForeground(new Color(240, 255, 255));
				lblPranavkvalitorcom.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
				printButton = new javax.swing.JButton();
				printButton.setAlignmentY(Component.TOP_ALIGNMENT);
				printButton.setBounds(238, 624, 100, 21);
				statusReceiptPanel.add(printButton);

				printButton.setBackground(new java.awt.Color(0, 102, 51));
				printButton.setFont(new Font("Tahoma", Font.BOLD, 12)); // NOI18N
				printButton.setText("PRINT");
				printButton.setMaximumSize(new java.awt.Dimension(67, 40));
				printButton.setMinimumSize(new java.awt.Dimension(67, 40));
				printButton.setPreferredSize(new java.awt.Dimension(67, 40));
				dual_channel = new javax.swing.JCheckBox();
				dual_channel.setAlignmentY(Component.TOP_ALIGNMENT);
				dual_channel.setHorizontalAlignment(SwingConstants.CENTER);
				dual_channel.setBounds(17, 624, 100, 21);
				statusReceiptPanel.add(dual_channel);
				dual_channel.setFont(new Font("Tahoma", Font.PLAIN, 12));

				dual_channel.setText("Dual-Channel");
				printButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						printButtonActionPerformed(evt);
					}
				});
				GroupLayout gl_mainXpannel = new GroupLayout(mainXpannel);
				gl_mainXpannel.setHorizontalGroup(
						gl_mainXpannel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_mainXpannel.createSequentialGroup()
								.addGroup(gl_mainXpannel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_mainXpannel.createSequentialGroup()
												.addGap(2)
												.addGroup(gl_mainXpannel.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_mainXpannel.createSequentialGroup()
																.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)
																.addGap(144)
																.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 323, GroupLayout.PREFERRED_SIZE))
														.addGroup(gl_mainXpannel.createSequentialGroup()
																.addGroup(gl_mainXpannel.createParallelGroup(Alignment.LEADING, false)
																		.addGroup(gl_mainXpannel.createSequentialGroup()
																				.addGap(182)
																				.addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE))
																		.addComponent(buttonsPannel, GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
																		.addComponent(jPanel1, 0, 0, Short.MAX_VALUE))
																.addGap(53)
																.addComponent(statusReceiptPanel, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE))))
										.addGroup(gl_mainXpannel.createSequentialGroup()
												.addGap(423)
												.addComponent(lblPranavkvalitorcom)))
								.addContainerGap(77, Short.MAX_VALUE))
						);
				gl_mainXpannel.setVerticalGroup(
						gl_mainXpannel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_mainXpannel.createSequentialGroup()
								.addGroup(gl_mainXpannel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_mainXpannel.createSequentialGroup()
												.addGap(11)
												.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE))
										.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
								.addGap(26)
								.addGroup(gl_mainXpannel.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_mainXpannel.createSequentialGroup()
												.addComponent(jLabel7)
												.addGap(18)
												.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(buttonsPannel, GroupLayout.PREFERRED_SIZE, 393, GroupLayout.PREFERRED_SIZE))
										.addComponent(statusReceiptPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblPranavkvalitorcom)
								.addGap(58))
						);
				mainXpannel.setLayout(gl_mainXpannel);
				GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
				gl_mainPanel.setHorizontalGroup(
						gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_mainPanel.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(mainXpannel, GroupLayout.PREFERRED_SIZE, 1072, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
						);
				gl_mainPanel.setVerticalGroup(
						gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_mainPanel.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(mainXpannel, GroupLayout.PREFERRED_SIZE, 856, GroupLayout.PREFERRED_SIZE))
						);
				mainPanel.setLayout(gl_mainPanel);

				getAccessibleContext().setAccessibleName("FRAMEX");

				pack();
	}// </editor-fold>//GEN-END:initComponents

	protected void lastTransStatusButtonClicked(MouseEvent e) {
		if (isConnected) {
			setAllDisplays("", "", "");
			details.setForeground(Color.BLUE);
			details.setText("starting LAST TRANS STATUS REQUEST");
			printFlag = "1";
			IpsJson message = new IpsJson();

			message.setOperationType("LastTransactionStatus");

			message.setPrintFlag(printFlag);
			message.setPedIp(terminalIpField.getText());
			message.setPedPort(terminalPortField.getText());

			String statusMessageIp = null;
			String statusMessagePort = null;
			if (dual_channel.isSelected()) {
				statusMessageIp = statusMessageIpField.getText();
				statusMessagePort = statusMessagePortField.getText();
			}
			message.setStatusMessageIp(statusMessageIp);
			message.setStatusMessagePort(statusMessagePort);
			message.setTimeOut(config.getProperty("timeout_Reprint", "0"));
			String json;
			try {
				json = mapper.writeValueAsString(message);
				tcpSender.tell(json, ActorRef.noSender());
			} catch (JsonProcessingException ex) {
				System.exit(0);
			}
		} else {
			details.setText("NO CONNECTION!");
		}

	}

	protected void manualDllButtonClicked(java.awt.event.MouseEvent evt) {
		if (isConnected) {
			setAllDisplays("", "", "");
			details.setForeground(Color.BLUE);
			details.setText("starting manual DLL request");
			if (printFlagField.getSelectedIndex() == 0) {
				printFlag = "1";
			} else {
				printFlag = "0";
			}
			IpsJson message = new IpsJson();

			message.setOperationType("UpdateDll");
			message.setPrintFlag(printFlag);
			message.setPedIp(terminalIpField.getText());
			message.setPedPort(terminalPortField.getText());

			String statusMessageIp = null;
			String statusMessagePort = null;
			if (dual_channel.isSelected()) {
				statusMessageIp = statusMessageIpField.getText();
				statusMessagePort = statusMessagePortField.getText();
			}
			message.setStatusMessageIp(statusMessageIp);
			message.setStatusMessagePort(statusMessagePort);
			message.setTimeOut(config.getProperty("timeout_Dll", "0"));
			String json;
			try {
				json = mapper.writeValueAsString(message);
				tcpSender.tell(json, ActorRef.noSender());
			} catch (JsonProcessingException ex) {
				System.exit(0);
			}
		} else {
			details.setText("NO CONNECTION!");
		}

	}

	private void paymentButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_paymentButtonMouseClicked
		if (isConnected) {
			setAllDisplays("", "", "");
			String amount = amountField.getText();
			if (amount.equalsIgnoreCase(null) || amount.equalsIgnoreCase("")) {
				details.setForeground(Color.red);
				details.setText("ENTER SOME AMOUNT IN PENCE");
			} else {
				details.setForeground(Color.BLUE);
				details.setText("starting Payment with " + amount + " pence");
				if (printFlagField.getSelectedIndex() == 0) {
					printFlag = "1";
				} else {
					printFlag = "0";
				}
				IpsJson message = new IpsJson();

				message.setAmount(amount);
				message.setOperationType("Payment");
				message.setTransactionReference(GTmessage.getText());
				message.setPrintFlag(printFlag);
				message.setPedIp(terminalIpField.getText());
				message.setPedPort(terminalPortField.getText());
				String statusMessageIp = null;
				String statusMessagePort = null;
				if (dual_channel.isSelected()) {
					statusMessageIp = statusMessageIpField.getText();
					statusMessagePort = statusMessagePortField.getText();
				}
				if (wait4CardRemoved.isSelected()) {
					message.setWait4CardRemoved("true");
				}
				message.setStatusMessageIp(statusMessageIp);
				message.setStatusMessagePort(statusMessagePort);
				message.setTimeOut(config.getProperty("timeout_Payment", "0"));
				String json;
				try {
					json = mapper.writeValueAsString(message);
					tcpSender.tell(json, ActorRef.noSender());
				} catch (JsonProcessingException ex) {
					System.exit(0);
				}
			}
		} else {
			details.setText("NO CONNECTION!");
		} // log.trace("sending json to TCP sender :" + json);

	}// GEN-LAST:event_paymentButtonMouseClicked

	private void refundButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_refundButtonMouseClicked
		if (isConnected) {
			setAllDisplays("", "", "");
			String amount = amountField.getText();
			if (amount.equalsIgnoreCase(null) || amount.equalsIgnoreCase("")) {
				details.setForeground(Color.red);
				details.setText("ENTER SOME AMOUNT IN PENCE");
			} else {
				details.setForeground(Color.BLUE);
				details.setText("starting REFUND with " + amount + " pence");
				if (printFlagField.getSelectedIndex() == 0) {
					printFlag = "1";
				} else {
					printFlag = "0";
				}
				IpsJson message = new IpsJson();

				message.setAmount(amount);
				message.setOperationType("Refund");
				message.setTransactionReference(GTmessage.getText());
				if (wait4CardRemoved.isSelected()) {
					message.setWait4CardRemoved("true");
				}
				message.setPrintFlag(printFlag);
				message.setPedIp(terminalIpField.getText());
				message.setPedPort(terminalPortField.getText());

				String statusMessageIp = null;
				String statusMessagePort = null;
				if (dual_channel.isSelected()) {
					statusMessageIp = statusMessageIpField.getText();
					statusMessagePort = statusMessagePortField.getText();
				}
				message.setStatusMessageIp(statusMessageIp);
				message.setStatusMessagePort(statusMessagePort);
				message.setTimeOut(config.getProperty("timeout_Refund", "0"));
				String json;
				try {
					json = mapper.writeValueAsString(message);
					tcpSender.tell(json, ActorRef.noSender());
				} catch (JsonProcessingException ex) {
					System.exit(0);
				}
			}
		} else {
			details.setText("NO CONNECTION!");
		} // log.trace("sending
	}// GEN-LAST:event_refundButtonMouseClicked

	private void reversalButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_reversalButtonMouseClicked
		if (isConnected) {
			setAllDisplays("", "", "");
			details.setForeground(Color.BLUE);
			details.setText("starting REVERSAL");
			if (printFlagField.getSelectedIndex() == 0) {
				printFlag = "1";
			} else {
				printFlag = "0";
			}
			IpsJson message = new IpsJson();

			message.setOperationType("Reversal");
			message.setTransactionReference(GTmessage.getText());
			if (wait4CardRemoved.isSelected()) {
				message.setWait4CardRemoved("true");
			}
			message.setPrintFlag(printFlag);
			message.setPedIp(terminalIpField.getText());
			message.setPedPort(terminalPortField.getText());

			String statusMessageIp = null;
			String statusMessagePort = null;
			if (dual_channel.isSelected()) {
				statusMessageIp = statusMessageIpField.getText();
				statusMessagePort = statusMessagePortField.getText();
			}
			message.setStatusMessageIp(statusMessageIp);
			message.setStatusMessagePort(statusMessagePort);
			message.setTimeOut(config.getProperty("timeout_Reversal", "0"));
			String json;
			try {
				json = mapper.writeValueAsString(message);
				tcpSender.tell(json, ActorRef.noSender());
			} catch (JsonProcessingException ex) {
				System.exit(0);
			}
		} else {
			details.setText("NO CONNECTION!");
		}
	}

	private void firstDllButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_dllButtonMouseClicked
		if (isConnected) {
			setAllDisplays("", "", "");
			details.setForeground(Color.BLUE);
			details.setText("starting 1ST DLL request");
			if (printFlagField.getSelectedIndex() == 0) {
				printFlag = "1";
			} else {
				printFlag = "0";
			}
			IpsJson message = new IpsJson();

			message.setOperationType("FirstDll");
			message.setPrintFlag(printFlag);
			message.setPedIp(terminalIpField.getText());
			message.setPedPort(terminalPortField.getText());

			String statusMessageIp = null;
			String statusMessagePort = null;
			if (dual_channel.isSelected()) {
				statusMessageIp = statusMessageIpField.getText();
				statusMessagePort = statusMessagePortField.getText();
			}
			message.setStatusMessageIp(statusMessageIp);
			message.setStatusMessagePort(statusMessagePort);
			message.setTimeOut(config.getProperty("timeout_Dll", "0"));
			String json;
			try {
				json = mapper.writeValueAsString(message);
				tcpSender.tell(json, ActorRef.noSender());
			} catch (JsonProcessingException ex) {
				System.exit(0);
			}
		} else {
			details.setText("NO CONNECTION!");
		}
	}

	private void xReportButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_xReportButtonMouseClicked
		if (isConnected) {
			setAllDisplays("", "", "");
			details.setForeground(Color.BLUE);
			details.setText("starting PedBalance REQUEST");
			if (printFlagField.getSelectedIndex() == 0) {
				printFlag = "1";
			} else {
				printFlag = "0";
			}
			IpsJson message = new IpsJson();

			message.setOperationType("PedBalance");

			message.setPrintFlag(printFlag);
			message.setPedIp(terminalIpField.getText());
			message.setPedPort(terminalPortField.getText());

			String statusMessageIp = null;
			String statusMessagePort = null;
			if (dual_channel.isSelected()) {
				statusMessageIp = statusMessageIpField.getText();
				statusMessagePort = statusMessagePortField.getText();
			}
			message.setStatusMessageIp(statusMessageIp);
			message.setStatusMessagePort(statusMessagePort);
			message.setTimeOut(config.getProperty("timeout_Report", "0"));
			String json;
			try {
				json = mapper.writeValueAsString(message);
				tcpSender.tell(json, ActorRef.noSender());
			} catch (JsonProcessingException ex) {
				System.exit(0);
			}
		} else {
			details.setText("NO CONNECTION!");
		}
	}

	private void zReportButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_zReportButtonMouseClicked
		if (isConnected) {
			setAllDisplays("", "", "");
			details.setForeground(Color.BLUE);
			details.setText("starting EndOfDay REQUEST");
			if (printFlagField.getSelectedIndex() == 0) {
				printFlag = "1";
			} else {
				printFlag = "0";
			}
			IpsJson message = new IpsJson();

			message.setOperationType("EndOfDay");

			message.setPrintFlag(printFlag);
			message.setPedIp(terminalIpField.getText());
			message.setPedPort(terminalPortField.getText());

			String statusMessageIp = null;
			String statusMessagePort = null;
			if (dual_channel.isSelected()) {
				statusMessageIp = statusMessageIpField.getText();
				statusMessagePort = statusMessagePortField.getText();
			}
			message.setStatusMessageIp(statusMessageIp);
			message.setStatusMessagePort(statusMessagePort);
			message.setTimeOut(config.getProperty("timeout_Report", "0"));
			String json;
			try {
				json = mapper.writeValueAsString(message);
				tcpSender.tell(json, ActorRef.noSender());
			} catch (JsonProcessingException ex) {
				System.exit(0);
			}
		} else {
			details.setText("NO CONNECTION!");
		}
	}

	private void terminalStatusButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_terminalStatusButtonMouseClicked

		if (isConnected) {
			setAllDisplays("", "", "");
			details.setForeground(Color.BLUE);
			details.setText("starting PED STATUS REQUEST");
			printFlag = "1";
			IpsJson message = new IpsJson();

			message.setOperationType("PedStatus");

			message.setPrintFlag(printFlag);
			message.setPedIp(terminalIpField.getText());
			message.setPedPort(terminalPortField.getText());
			String statusMessageIp = null;
			String statusMessagePort = null;
			if (dual_channel.isSelected()) {
				statusMessageIp = statusMessageIpField.getText();
				statusMessagePort = statusMessagePortField.getText();
			}
			message.setStatusMessageIp(statusMessageIp);
			message.setStatusMessagePort(statusMessagePort);
			message.setTimeOut(config.getProperty("timeout_Report", "0"));
			String json;
			try {
				json = mapper.writeValueAsString(message);
				tcpSender.tell(json, ActorRef.noSender());
			} catch (JsonProcessingException ex) {
				System.exit(0);
			}
		} else {
			details.setText("NO CONNECTION!");
		}
	}

	private void reprintReceiptButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_reprintReceiptButtonMouseClicked
		if (isConnected) {
			setAllDisplays("", "", "");
			details.setForeground(Color.BLUE);
			details.setText("starting REPRINT RECEIPT REQUEST");
			printFlag = "1";
			IpsJson message = new IpsJson();

			message.setOperationType("ReprintReceipt");

			message.setPrintFlag(printFlag);
			message.setPedIp(terminalIpField.getText());
			message.setPedPort(terminalPortField.getText());

			String statusMessageIp = null;
			String statusMessagePort = null;
			if (dual_channel.isSelected()) {
				statusMessageIp = statusMessageIpField.getText();
				statusMessagePort = statusMessagePortField.getText();
			}
			message.setStatusMessageIp(statusMessageIp);
			message.setStatusMessagePort(statusMessagePort);
			message.setTimeOut(config.getProperty("timeout_Reprint", "0"));
			String json;
			try {
				json = mapper.writeValueAsString(message);
				tcpSender.tell(json, ActorRef.noSender());
			} catch (JsonProcessingException ex) {
				System.exit(0);
			}
		} else {
			details.setText("NO CONNECTION!");
		}
	}

	private void probePedButtonClicked(MouseEvent e) {
		if (isConnected) {
			setAllDisplays("", "", "");
			details.setForeground(Color.BLUE);
			details.setText("starting PROBE PED REQUEST");
			printFlag = "0";
			IpsJson message = new IpsJson();

			message.setOperationType("ProbePed");

			message.setPrintFlag(printFlag);
			message.setPedIp(terminalIpField.getText());
			message.setPedPort(terminalPortField.getText());

			String statusMessageIp = null;
			String statusMessagePort = null;
			if (dual_channel.isSelected()) {
				statusMessageIp = statusMessageIpField.getText();
				statusMessagePort = statusMessagePortField.getText();
			}
			message.setStatusMessageIp(statusMessageIp);
			message.setStatusMessagePort(statusMessagePort);
			message.setTimeOut(config.getProperty("timeout_Probe", "0"));
			String json;
			try {
				json = mapper.writeValueAsString(message);
				tcpSender.tell(json, ActorRef.noSender());
			} catch (JsonProcessingException ex) {
				System.exit(0);
			}
		} else {
			details.setText("NO CONNECTION!");
		}

	}

	private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {
		if (tcpSender != null) {
			if (isConnected) {
				details.setText("TCP CONNECTION ALREADY RUNNING..!!");
			} else {
				tcpSender = null;
				startConnection();
			}
		} else {
			startConnection();
		}
	}

	private void startConnection() {
		String[] cloudIpAndPort = {serverIp.getText(), serverPort.getText()};

		if (cloudIpAndPort.length == 2) {
			this.system = ActorSystem.create("IPS_Simulator");
			this.tcpSender = system.actorOf(
					tcpClient.props(new InetSocketAddress(cloudIpAndPort[0], Integer.parseInt(cloudIpAndPort[1])), mapper));
		} else {
			details.setText("FILL IN CLOUD, TERMINAL AND STATUS MESSAGE DETAILS!!");
		}
	}

	private void stopButtonMouseClicked(java.awt.event.MouseEvent evt) {
		if (tcpSender != null) {
			details.setText("STOPPING SERVICE");
			system.stop(tcpSender);
			system.terminate();
			tcpSender = null;
			details.setText("PRESS START TO RESTART SERVICES");
		} else {
			details.setText("NO CONNECTION ACTOR RUNNING ALREADY!");
		}
	}

	private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_printButtonActionPerformed

		PrinterJob pjob = PrinterJob.getPrinterJob();
		PageFormat preformat = pjob.defaultPage();
		preformat.setOrientation(PageFormat.PORTRAIT);
		PageFormat postformat = pjob.pageDialog(preformat);
		if (preformat != postformat) {
			pjob.setPrintable(new Printer(receiptField), postformat);
			if (pjob.printDialog()) {
				try {
					pjob.print();
				} catch (PrinterException ex) {
					Logger.getLogger(SIMULATORx.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	private void startStatusMessageListener() {
		systemX = ActorSystem.create("system");
		String statusMsgIp = statusMessageIpField.getText();
		String statusMsgPort = statusMessagePortField.getText();
		if (!statusMsgIp.equals("") && !statusMsgPort.equals("")) {
			ActorRef tcpMnager = Tcp.get(systemX).manager();
			systemX.actorOf(TcpServerActor.props(tcpMnager, new InetSocketAddress(statusMsgIp, Integer.parseInt(statusMsgPort)), mapper));
		} else {
			details.setText("FILL IN STATUS MESSAGE DETAILS!!");
		}
	}

	private void setAllDisplays(String detailsMessage, String statusMessage, String receiptMessage) {
		details.setText(detailsMessage);
		statusMessageField.setText(statusMessage);
		receiptField.setText(receiptMessage);
	}
	private javax.swing.JComboBox<String> printFlagField = new javax.swing.JComboBox<>();
	private javax.swing.JTextField GTmessage;
	private javax.swing.JTextField amountField;
	private javax.swing.JPanel buttonsPannel;
	public static javax.swing.JTextArea details;
	private javax.swing.JButton firstDllButton;
	private javax.swing.JCheckBox dual_channel;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JPanel mainXpannel;
	private javax.swing.JButton paymentButton;
	private javax.swing.JButton printButton;
	public static javax.swing.JTextArea receiptField;
	private javax.swing.JButton refundButton;
	private javax.swing.JButton reprintReceiptButton;
	private javax.swing.JButton reversalButton;
	private javax.swing.JButton startButton;
	public static javax.swing.JTextArea statusMessageField;
	private javax.swing.JTextField statusMessageIpField;
	private javax.swing.JTextField statusMessagePortField;
	private javax.swing.JPanel statusReceiptPanel;
	private javax.swing.JButton stopButton;
	private javax.swing.JTextField terminalIpField;
	private javax.swing.JTextField terminalPortField;
	private javax.swing.JButton terminalStatusButton;
	private javax.swing.JButton xReportButton;
	private javax.swing.JButton zReportButton;
	private static JCheckBox wait4CardRemoved;
	private JTextField serverIp;
	private JTextField serverPort;

	public static class Printer implements Printable {

		final Component comp;

		public Printer(Component comp) {
			this.comp = comp;
		}

		@Override
		public int print(Graphics g, PageFormat format, int page_index) throws PrinterException {
			if (page_index > 0) {
				return Printable.NO_SUCH_PAGE;
			}

			Dimension dim = comp.getSize();
			double cHeight = dim.getHeight();
			double cWidth = dim.getWidth();

			double pHeight = format.getImageableHeight();
			double pWidth = format.getImageableWidth();

			double pXStart = format.getImageableX();
			double pYStart = format.getImageableY();

			double xRatio = pWidth / cWidth;
			double yRatio = pHeight / cHeight;

			Graphics2D g2 = (Graphics2D) g;
			g2.translate(pXStart, pYStart);
			g2.scale(xRatio, yRatio);
			comp.paint(g2);

			return Printable.PAGE_EXISTS;
		}
	}

	public JCheckBox getWait4CardRemoved() {
		return wait4CardRemoved;
	}

}
