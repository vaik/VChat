package vaik.chat.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import vaik.chat.constant.Constant;
import vaik.chat.model.Message;
import vaik.chat.model.User;
import vaik.chat.type.MessageType;
import vaik.chat.ui.ChatFrame;

public class Client {
	private Socket s;

	public Socket getS() {
		return s;
	}

	public BufferedReader getIn() {
		return in;
	}

	public PrintWriter getOut() {
		return out;
	}

	private User user;
	private BufferedReader in;
	private PrintWriter out;
	private ChatFrame chat;
	private boolean stop = false;

	public Client(ChatFrame chat) {
		this.chat = chat;
		this.user = chat.getUser();
		try {
			s = new Socket(Constant.HOST, Constant.PORT);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
			out.println(user.getUserName());
			Message message = new Message(in.readLine());
			if (message.getType() == MessageType.Connect) {
				user.setUserId(Integer.parseInt(message.getBody()));
			}
			System.out.println("userId:" + user.getUserId());
			new Thread(new ClientThread()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		stop = true;
	}

	public class ClientThread implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					if (stop)break;
					String word = in.readLine();
					Message message = new Message(word);
					if (message.getType() == MessageType.Disconnect) {
						close();
						break;
					}else if(message.getType() == MessageType.UserList){
						chat.updateUserList(message);
					}else{
						chat.receive(message);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (s != null)
					try {
						s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				System.exit(0);
			}
		}

	}
}
