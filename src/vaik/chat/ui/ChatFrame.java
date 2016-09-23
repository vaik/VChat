package vaik.chat.ui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatFrame extends JFrame {
	private JList jl;
	private JTextArea jta;
	private JPanel jp;
	private JButton jbtn;
	private JTextField jtf;
	private JScrollPane jsp1,jsp2;
	public ChatFrame(){
		this.setLocation(100,100);
		this.setSize(800, 600);
		this.setTitle("网络聊天《VChat》");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		jta = new JTextArea();
		jsp1 = new JScrollPane(jta);
		jtf = new JTextField(35);
		jbtn = new JButton("发送");
		jl = new JList(new String[]{"所有人","张三","李四"});
		jl.setFixedCellWidth(120);
		jsp2 = new JScrollPane(jl);
		jp = new JPanel();
		jp.add(jtf);
		jp.add(jbtn);
		this.add(jsp1);
		this.add(jp,BorderLayout.SOUTH);
		this.add(jsp2,BorderLayout.WEST);
		
		this.setVisible(true);
	}
	
}

