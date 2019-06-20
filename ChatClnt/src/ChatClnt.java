import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatClnt {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Socket sock = null ;
		DataInputStream in = null;
		DataOutputStream out =null;
		
		BufferedReader key = null; 
		
		try {
			sock = new Socket("127.0.0.1", 10001);
			in = new DataInputStream(sock.getInputStream());
			key = new BufferedReader(new InputStreamReader(System.in));
			out = new DataOutputStream(sock.getOutputStream());
		
			System.out.println("닉네임을 입력해주세요 : ");
			String data = key.readLine();
			out.writeUTF(data);
			
			Thread th = new Thread(new Send(out));
			th.start();
			
		}catch(IOException e) {}
		
		
		try {
			while(true) {
				String str = in.readUTF();
				System.out.println(str); 
			}
			
		
		}catch(IOException e) {}
	}

}

class Send implements Runnable{
	DataOutputStream out;
	BufferedReader key = new BufferedReader(new InputStreamReader(System.in));
	
	public Send(DataOutputStream out) {
	this.out = out; 
	}
	
	public void run() {
		while(true) {
			try {
				String msg = key.readLine();
				out.writeUTF(msg);
			}catch(Exception e) {}
		}
		
	}
	
}
