package ptm;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PtmUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField patternText;
	private JTextField sourceFilePath;
	private JTextField stopFilePath;
	private JTextPane timeOrigTextPane;
	
	private long timePtm=0;
	private long timeOrig=0;
	
	private static String leftTextPane = ""
			+ "<html>"
			+ "<font class = \"Text1\">"
			+ "<br>";
	private static String rightTextPane = "<img  align = \"right\" src=" + PtmUI.class.getResource("/ptm/images/up.png" + "")	
			+ "</span>"
			+ "</html>";
	private static String rightTextPane2 = "<img  align = \"right\" src=" + PtmUI.class.getResource("/ptm/images/down.png" + "")	
			+ "</span>"
			+ "</html>";
	private JTextPane timePTMTextPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PtmUI frame = new PtmUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PtmUI() {
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				File f1 = new File("SourceFile.dat");
				f1.deleteOnExit();
				File f2 = new File("result1.txt");
				f2.deleteOnExit();
				File f3 = new File("resultFinal.dat");
				f3.deleteOnExit();
				File f4 = new File("WithoutStop.dat");
				f4.deleteOnExit();
				File f5 = new File("WithStop.dat");
				f5.deleteOnExit();
			}
		});
		setResizable(false);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		
		setTitle("Effective Pattern Mining");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 456, 382);
		contentPane = new JPanel();
		contentPane.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		patternText = new JTextField();
		patternText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//  Perform PTM Algorithm.
				if (sourceFilePath.getText().isEmpty() || patternText.getText().isEmpty()) {
					
					System.out.println("If");
				}
				else {
					
					Thread t1 = new Thread(PtmUI.this);
					t1.start();
					
					try {
						t1.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					timeOrigTextPane.setText(leftTextPane + " " + timeOrig + " ns " + rightTextPane);
					timePTMTextPane.setText(leftTextPane + " " + timePtm + " ns " + rightTextPane2);
					
					
				}
			}
		});
		patternText.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		patternText.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Pattern Text *", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		patternText.setColumns(10);
		
		sourceFilePath = new JTextField();
		sourceFilePath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(null);
				File f;
				f = fc.getSelectedFile();
				sourceFilePath.setText(f.getAbsolutePath());
			}
		});
		
		sourceFilePath.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		sourceFilePath.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Source File Path *", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		sourceFilePath.setColumns(10);
		
		stopFilePath = new JTextField();
		stopFilePath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(null);
				File f;
				f = fc.getSelectedFile();
				stopFilePath.setText(f.getAbsolutePath());
			}
		});
		stopFilePath.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		stopFilePath.setColumns(10);
		stopFilePath.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Stop Words File Path", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		timeOrigTextPane = new JTextPane();
		timeOrigTextPane.setEditable(false);
		timeOrigTextPane.setBorder(new TitledBorder(null, "Original Time", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		timeOrigTextPane.setContentType("text/html");
		
		/*
		 * StyleSheet for the jTextPane
		 */
		
		HTMLEditorKit kit = new HTMLEditorKit();
		timeOrigTextPane.setEditorKit(kit);
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule(".Text1 {font-family:" + FontUIResource.getFont("Segoe UI Light")+";}");
//				+ "")+"; font-size: 15px;}");
				
		timeOrigTextPane.setText(leftTextPane + " " + timeOrig + " ns " + rightTextPane);
		
//		timeOrigTextPane.set
//		imageTop.setIcon(new ImageIcon(PageHome.class.getResource("/com/cprakashagr/WE/Images/women_rolesApp.jpg")));

		timePTMTextPane = new JTextPane();
		timePTMTextPane.setBorder(new TitledBorder(null, "PTM Time", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		timePTMTextPane.setEditable(false);
		timePTMTextPane.setContentType("text/html");
		timePTMTextPane.setEditorKit(kit);
		
		timePTMTextPane.setText(leftTextPane + " " + timePtm + " ns " +rightTextPane2);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(timeOrigTextPane, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(timePTMTextPane, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
						.addComponent(sourceFilePath, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
						.addComponent(patternText, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
						.addComponent(stopFilePath, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(sourceFilePath, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(stopFilePath, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(patternText, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(timePTMTextPane, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
						.addComponent(timeOrigTextPane, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}

	@Override
	public void run() {
		System.out.println("Else");
		String so = sourceFilePath.getText();
		String st;
		URL s = PtmUI.class.getResource("/ptm/db/stop.txt");
		st = new File(s.getFile()).getAbsolutePath();
		if (!stopFilePath.getText().isEmpty())
			st = stopFilePath.getText();
		String pa = patternText.getText();
		
//		System.out.println(so + " " + st + " " + pa);
		PtmAlgo obj = new PtmAlgo(so,st,pa);
		
		timePtm = obj.getTimePtm();
		timeOrig = obj.getTimeOrig();
		
		System.out.println(timeOrig+ " ... " + timePtm);
	}
}
