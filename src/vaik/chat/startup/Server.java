package vaik.chat.startup;

public class Server {
	public static void main(String[] args) {
		new vaik.chat.socket.Server().run();
	}
}
