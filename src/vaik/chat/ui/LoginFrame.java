package vaik.chat.ui;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {
	private JLabel jl;
	private JButton jbtn;
	private JTextField jtf;

	public LoginFrame() {
		this.setLocation(100, 100);
		this.setSize(400, 100);
		this.setTitle("登陆");
		this.setResizable(false);
		this.setLayout(new FlowLayout());

		jl = new JLabel("用户名");
		jtf = new JTextField(25);
		jbtn = new JButton("连接");
		jbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		jtf.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
		this.add(jl);
		this.add(jtf);
		this.add(jbtn);

		this.setVisible(true);
	}

	public void login() {
		String userName = jtf.getText();
		if (userName == null || userName.trim().equals("")) {
			JOptionPane.showMessageDialog(null, "请输入用户名");
			return;
		}
		new ChatFrame(userName);
		close();
	}

	public void close() {
		this.setVisible(false);
	}
}
