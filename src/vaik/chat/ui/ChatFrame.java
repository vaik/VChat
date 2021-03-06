package vaik.chat.ui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import vaik.chat.constant.Constant;
import vaik.chat.model.Message;
import vaik.chat.model.User;
import vaik.chat.socket.Client;
import vaik.chat.type.MessageType;

public class ChatFrame extends JFrame {
	private JList jl;
	private JTextArea jta;
	private JPanel jp;
	private JButton jbtn;
	private JTextField jtf;
	private JScrollPane jsp1, jsp2;
	private List<User> userList;
	private User user;

	public User getUser() {
		return user;
	}

	private DefaultListModel<Object> listModel;
	private BufferedReader in;
	private PrintWriter out;

	public ChatFrame(String userName) {
		this.setLocation(100, 100);
		this.setSize(800, 600);
		this.setTitle("网络聊天_VChat,当前用户：" + userName);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				out.println(Constant.SOCKET_DISCONNECT);
				super.windowClosing(e);
			}

		});
		this.user = new User();
		user.setUserName(userName);
		this.userList = new ArrayList<User>();

		jta = new JTextArea();
		jsp1 = new JScrollPane(jta);
		jtf = new JTextField(35);
		jtf.addKeyListener(new KeyClick());
		jbtn = new JButton("发言");
		jbtn.addActionListener(new BtnClick());
		listModel = new DefaultListModel<>();

		jl = new JList(listModel);
		jl.setFixedCellWidth(120);
		jsp2 = new JScrollPane(jl);
		jp = new JPanel();
		jp.add(jtf);
		jp.add(jbtn);
		this.add(jsp1);
		this.add(jp, BorderLayout.SOUTH);
		this.add(jsp2, BorderLayout.WEST);
		Client client = new Client(this);
		this.in = client.getIn();
		this.out = client.getOut();
		this.setVisible(true);
	}

	public void updateUserList(Message message) {
		if (message.getType() == MessageType.UserList) {
			userList = message.getAllUser();
			if (userList.size() > 0) {
				listModel.removeAllElements();
				listModel.addElement("所有人");
				for (int i = 0; i < userList.size(); i++) {
					listModel.addElement(userList.get(i));
				}
			}
		}
	}

	public void send() {
		String word = jtf.getText();
		int[] indices = jl.getSelectedIndices();
		boolean isAll = false;
		boolean hasSelf = false;
		String userList = "";
		if (indices.length > 0) {
			userList = Constant.SOCKET_TO_FRONT;
			for (int i : indices) {
				if (i == 0) {
					isAll = true;
					break;
				}
				User user = (User) listModel.getElementAt(i);
				userList += user.getUserId()+",";
				if(user.getUserId() == this.user.getUserId()){
					hasSelf = true;
				}
			}
			if(!hasSelf){
				userList += this.user.getUserId()+",";
			}
			userList = userList.substring(0,userList.length()-1);
			userList += Constant.SOCKET_TO_ENT;
		}
		if (indices == null || indices.length == 0 || isAll) {
			if (word != null && !word.trim().equals("")) {
				out.println(word);
				jtf.setText("");
			}
		}else{
			if (word != null && !word.trim().equals("")) {
				out.println(userList+word);
				jtf.setText("");
			}
		}
	}

	public void receive(Message message) {
		jta.setText(jta.getText() + message.getBody() + "\n");
	}

	public class BtnClick implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jbtn) {
				send();
			}
		}
	}

	public class KeyClick extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				send();
			}
		}

	}

}
