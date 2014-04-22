package ptm;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.LayoutStyle.ComponentPlacement;

public class PtmUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField patternText;
	private JTextField sourceFilePath;
	private JTextField stopFilePath;

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
					System.out.println("Else");
					String so = sourceFilePath.getText();
					String st = stopFilePath.getText();
					String pa = patternText.getText();
					
//					System.out.println(so + " " + st + " " + pa);
					PtmAlgo obj = new PtmAlgo(so,st,pa);
					System.out.println(obj.getTimeWithPTM());
					System.out.println(obj.getTimeWithoutPTM());
				}
			}
		});
		patternText.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		patternText.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Pattern Text", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		sourceFilePath.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Source File Path", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(sourceFilePath, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
						.addComponent(stopFilePath, GroupLayout.PREFERRED_SIZE, 410, GroupLayout.PREFERRED_SIZE)
						.addComponent(patternText, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
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
					.addContainerGap(115, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
