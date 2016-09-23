package vaik.chat.ui;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {
	private JLabel jl;
	private JButton jbtn;
	private JTextField jtf;
	public LoginFrame(){
		this.setLocation(100,100);
		this.setSize(400,100);
		this.setTitle("用户连接");
		this.setResizable(false);
		this.setLayout(new FlowLayout());
		
		jl = new JLabel("输入用户");
		jtf = new JTextField(25);
		jbtn = new JButton("连接");
		jbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChatFrame();
				close();
			}
		});
		this.add(jl);this.add(jtf);this.add(jbtn);
		
		this.setVisible(true);
	}
	
	public void close(){
		this.setVisible(false);
	}
}
