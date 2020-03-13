package xiangqi;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

	Server father;// 声明Server的引用
	ServerSocket ss;// 声明ServerSocket引用
	boolean flag = true;

	public ServerThread(Server father) {
		// 构造器
		this.father = father;
		ss = father.ss;

	}

	public void run() {

		while (flag) {
			try {
				Socket sc = ss.accept();// 监听，等待客户端port端口号进行连接
				ServerAgentThread sat = new ServerAgentThread(father, sc);
				sat.start();// 创建并启动服务器代理线程
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
