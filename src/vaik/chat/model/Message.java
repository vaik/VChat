package vaik.chat.model;

import vaik.chat.constant.*;
import vaik.chat.type.MessageType;

public class Message {
	private String prefix;
	private String toUsers;
	private String suffix;
	private String body;
	private String messageStr;
	private MessageType type;
	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public Message(String message) {
		this.messageStr = message;
	}
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getToUsers() {
		return toUsers;
	}
	public void setToUsers(String toUsers) {
		this.toUsers = toUsers;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getMessageStr() {
		return messageStr;
	}
	public void setMessageStr(String messageStr) {
		this.messageStr = messageStr;
	}
	
	public void setFields(String message){
		if(message.startsWith(Constant.SOCKET_CONNECT)){
			this.type = MessageType.Connect;
		}else if (message.startsWith(Constant.SOCKET_DISCONNECT)){
			this.type = MessageType.Disconnect;
		}else{
			this.type = MessageType.Message;
			if(message.startsWith(Constant.SOCKET_TO_FRONT)){
				this.prefix = Constant.SOCKET_TO_FRONT;
				if(message.indexOf(Constant.SOCKET_TO_ENT) > 0 )this.suffix = Constant.SOCKET_TO_ENT;
				this.toUsers = message.substring(Constant.SOCKET_TO_ENT.length(), message.indexOf(Constant.SOCKET_TO_ENT)-1-Constant.SOCKET_TO_ENT.length());
				this.body = message.substring(message.indexOf(Constant.SOCKET_TO_ENT)+Constant.SOCKET_TO_ENT.length());
			}else{
				this.body = message;
			}
		}
	}
	
	public int[] getUserIds(){
		int[] ids = null;
		if(this.toUsers != null && this.toUsers.length() > 0){
			String[] idStrs = this.toUsers.split(",");
			if(idStrs.length > 0){
				ids = new int[idStrs.length];
				for (int i = 0; i < idStrs.length; i++) {
					ids[i] = Integer.parseInt(idStrs[i]);
				}
			}
		}
		return ids;
	}
}
