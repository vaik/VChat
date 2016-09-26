package vaik.chat.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import vaik.chat.constant.Constant;
import vaik.chat.model.Message;
import vaik.chat.model.User;
import vaik.chat.type.MessageType;

public class Server {
	private ServerSocket ss;
	private Map<Integer, ServerThread> servers;

	public void run() {
		try {
			ss = new ServerSocket(Constant.PORT);
			servers = new HashMap<Integer, ServerThread>();
			while (true) {
				Socket s = ss.accept();
				new Thread(new ServerThread(s)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ss != null)
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public class ServerThread implements Runnable {
		private Socket s;
		private BufferedReader in;

		public BufferedReader getIn() {
			return in;
		}

		public PrintWriter getOut() {
			return out;
		}

		private PrintWriter out;
		private User user;
		private boolean stop = false;

		public ServerThread(Socket s) {
			this.s = s;
			try {
				this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				this.out = new PrintWriter(s.getOutputStream(), true);
				String name = in.readLine();
				user = new User();
				user.setUserName(name);
				user.setIp(s.getLocalAddress().getHostAddress());
				user.setPort(s.getPort());
				user.setUserId(getMaxUserId() + 1);
				this.user = user;
				servers.put(user.getUserId(), this);
				out.println(Constant.SOCKET_CONNECT + user.getUserId());

				System.out.println(user.getUserName() + "[" + user.getIp() + ":" + user.getPort() + "]连接上了");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private int getMaxUserId() {
			int num = 0;
			if (servers.size() > 0) {
				for (int i : servers.keySet()) {
					if (i > num) {
						num = i;
					}
				}
			}
			return num;
		}

		private void close() {
			stop = true;
			out.println(Constant.SOCKET_DISCONNECT);
			System.out.println(user.getUserName() + "离开了");
			servers.remove(user.getUserId());
		}

		private void send(Message message) {
			int[] userIds = message.getUserIds();
			if (userIds == null || userIds.length == 0) {
				for (Integer st : servers.keySet()) {
					servers.get(st).getOut().println(user.getUserName() + ":" + message.getBody());
				}
			} else {
				for (int id : userIds) {
					ServerThread st = servers.get(id);
					if (st != null) {
						st.getOut().println(user.getUserName() + ":" + message.getBody());
					}
				}
			}
		}

		private void sendUserList() {
			if (servers != null && servers.size() > 0) {
				String userListStr = Constant.SOCKET_USER_LIST;
				for (Integer st : servers.keySet()) {
					userListStr += user.getUserName() + ":" + user.getUserId() + ",";
				}
				userListStr = userListStr.substring(0,userListStr.length()-1);
				
				for (Integer st : servers.keySet()) {
					servers.get(st).getOut().println(userListStr);
				}
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					if (stop)
						break;
					String str = in.readLine();
					Message message = new Message(str);
					if (message.getType() == MessageType.Disconnect) {
						close();
						break;
					}else if(message.getType() == MessageType.USERLIST){
						
						continue;
					}
					send(message);
					System.out.println(user.getUserName() + ":" + str);
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (s != null)
					try {
						s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}

	}
}
