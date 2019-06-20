import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;



public class ChatServ {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Socket sock = null;
		User user = new User();
		ServerSocket serv = null;
		
		int count = 0;
		Thread thread[] = new Thread[10];
		
		try {
			serv = new ServerSocket(10001); 
			
			while(true) {
				sock = serv.accept();
				thread[count] = new Thread(new Receiver(user, sock));
				thread[count].start();
				count++;
			}
			
		}catch(Exception e) {};
		
	}
}

class User{
	HashMap<String,DataOutputStream> clnt = new HashMap<String,DataOutputStream>();
	
	public synchronized void AddClnt(String name, Socket sock) {
		try {
			sendMsg(name+"입장하셨습니다", "Server");
			clnt.put(name, new DataOutputStream(sock.getOutputStream()));
			System.out.println("채팅 참여인원"+ clnt.size());
		}catch(Exception e) {
			
		}
	}
	public synchronized void RemoveClnt(String name) {
		try {
			clnt.remove(name);
			sendMsg(name+"퇴장하셨습니다", "Server");
			System.out.println("채팅 참여인원"+ clnt.size());
		}catch(Exception e) {}
	}
	

	
	public synchronized void sendMsg(String msg, String name) throws Exception{
		Iterator<String> inet = clnt.keySet().iterator();
		while(inet.hasNext()) {
			String clntname = (String)inet.next();
			clnt.get(clntname).writeUTF(name+" :" + msg);
		}
		
	}
}

class Receiver implements Runnable {
	
	Socket sock; 
	DataInputStream in; 
	String name;
	User user; 
	
	public Receiver(User user, Socket sock) throws Exception{
		this.user  =user;
		this.sock = sock;
		in = new DataInputStream(sock.getInputStream());
		
		this.name = in.readUTF();
		this.user.AddClnt(name, sock);
	}
	
	public void run() {
		try {
			String msg = in.readUTF();
			user.sendMsg(msg, name);
		}catch(Exception e) {
			user.RemoveClnt(this.name);
		}
	}
}


