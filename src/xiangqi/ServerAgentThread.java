package xiangqi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread {

	Server father;// 声明Server的引用
	Socket sc;// 声明Socket的引用
	DataInputStream din;// 声明数据输入流与输出流的引用
	DataOutputStream dout;
	boolean flag = true;// 控制线程的标志位

	public ServerAgentThread(Server father, Socket sc) {
		this.father = father;
		this.sc = sc;

		try {
			din = new DataInputStream(sc.getInputStream());// 创建数据输入流
			dout = new DataOutputStream(sc.getOutputStream());// 创建数据输出流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (flag) {
			try {
				String msg = din.readUTF().trim();// 接收客户端发来的信息
				if (msg.startsWith("<#NICK_NAME#>")) {
					// 收到新用户发来的信息
					this.nick_name(msg);
				} else if (msg.startsWith("<#CLIENT_LEAVE#>")) {
					// 收到用户离开的信息
					this.client_leave(msg);
				} else if (msg.startsWith("<#TIAO_ZHAN#>")) {
					// 收到用户发出挑战的信息
					this.tiao_zhan(msg);
				} else if (msg.startsWith("<#TONG_YI#>")) {
					// 收到用户接收挑战的信息
					this.tong_yi(msg);
				} else if (msg.startsWith("<#BUTONG_YI#>")) {
					// 收到用户拒绝挑战的信息
					this.butong_yi(msg);
				} else if (msg.startsWith("<#BUSY#>")) {
					// 收到被挑战者忙
					this.busy(msg);
				} else if (msg.startsWith("<#MOVE#>")) {
					// 收到走棋信息
					this.move(msg);
				} else if (msg.startsWith("<#RENSHU#>")) {
					// 收到某用户认输的信息
					this.renshu(msg);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void nick_name(String msg) {
		String name = msg.substring(13);// 获取用户的昵称

		this.setName(name);// 用该昵称给线程取名，setName是Thread下的一个方法，给线程取名
		Vector v = father.onlineList;// 获取在线用户列表
		boolean isChongMing = false;
		int size = v.size();// 获取用户列表大小
		for (int i = 0; i < size; i++) {
			// 遍历列表，查看是否有此用户名
			ServerAgentThread tempSat = (ServerAgentThread) v.get(i);// 获取第i+1个对象
			if (tempSat.getName().equals(name)) {
				isChongMing = true;// 有重名，将标志位设为true
				break;
			}
		}
		if (isChongMing == true)// 如果重名
		{
			try {
				dout.writeUTF("<#NAME_CHONGMING#>");// 将重名信息发送给客户端
				din.close();// 关闭数据输入流
				dout.close();// 关闭数据输出流
				sc.close();// 关闭Socket
				flag = false;// 终止该服务器代理线程
			} catch (Exception e) {

				e.printStackTrace();
			}

		} else // 如果不重名
		{
			v.add(this);// 将该线程添加到在线列表
			father.refreshList();// 刷新服务器在线信息列表
			String nickListMsg = "";
			size = v.size();// 获取现在列表大小
			for (int i = 0; i < size; i++) {
				ServerAgentThread tempSat = (ServerAgentThread) v.get(i);// 获取第i+1个对象
				nickListMsg = nickListMsg + "|" + tempSat.getName();
				// 将在线列表内容组成字符串
			}
			nickListMsg = "<#NICK_LIST#>" + nickListMsg;
			Vector tempv = father.onlineList;
			size = tempv.size();
			for (int i = 0; i < size; i++) {
				// 遍历在线列表
				ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);// 获取第i+1个对象
				try {
					satTemp.dout.writeUTF(nickListMsg);// 将最新的列表信息发送到各个客户端
				} catch (IOException e) {

					e.printStackTrace();
				}
				if (satTemp != this) {
					// 给其他客户端发送新用户上线的信息
					try {
						satTemp.dout.writeUTF("<#MSG#>" + this.getName() + "上线了...");
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		}
	}

	public void client_leave(String msg) {
		Vector tempv = father.onlineList;// 获取在线列表
		tempv.remove(this);// 移除该用户
		int size = tempv.size();
		String n1 = "<#NICK_LIST#>";
		for (int i = 0; i < size; i++) {
			// 遍历在线列表
			ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);// 获取第i+1个对象
			// 向各个客户端发送用户离线信息
			try {
				satTemp.dout.writeUTF("<#MSG#>" + this.getName() + "离线了...");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			// 组织信息的在线用户列表
			n1 = n1 + "|" + satTemp.getName();
		}
		for (int i = 0; i < size; i++) {
			// 将最新的列表信息发送到各个客户端
			ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);// 获取第i+1个对象
			try {
				satTemp.dout.writeUTF(n1);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		this.flag = false;// 终止该服务器代理线程
		father.refreshList();// 更新服务器在线用户列表
	}

	public void tiao_zhan(String msg) {
		try {
			String name1 = this.getName();// 获得发出挑战信息用户的名字
			String name2 = msg.substring(13);// 获得被挑战的用户名字
			Vector v = father.onlineList;// 获取在线用户列表
			int size = v.size();// 获得在线用户列表大小
			for (int i = 0; i < size; i++) {
				// 编列列表，搜索被挑战用户
				ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// 获取第i+1个对象
				if (satTemp.getName().equals(name2)) {
					// 向该用户发送挑战请求，顺带提出挑战用户的名字

					satTemp.dout.writeUTF("<#TIAO_ZHAN#>" + name1);
					break;
				}
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	public void tong_yi(String msg) {
		String name = msg.substring(11);// 获取提出挑战的用户的名字
		Vector v = father.onlineList;// 获取在线用户列表
		int size = v.size();// 获得在线用户列表大小
		for (int i = 0; i < size; i++) {
			// 编列列表，搜索提出挑战用户
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// 获取第i+1个对象
			if (satTemp.getName().equals(name)) {
				// 向该用户发送对方接收挑战的信息
				try {
					satTemp.dout.writeUTF("<#TONG_YI#>");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public void butong_yi(String msg) {
		String name = msg.substring(13);// 获取提出挑战的用户的名字
		Vector v = father.onlineList;// 获取在线用户列表
		int size = v.size();// 获得在线用户列表大小
		for (int i = 0; i < size; i++) {
			// 编列列表，搜索提出挑战用户
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// 获取第i+1个对象
			if (satTemp.getName().equals(name)) {
				// 向该用户发送对方拒绝挑战的信息
				try {
					satTemp.dout.writeUTF("<#BUTONG_YI#>");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public void busy(String msg) {
		String name = msg.substring(8);// 获取提出挑战的用户的名字
		Vector v = father.onlineList;// 获取在线用户列表
		int size = v.size();// 获得在线用户列表大小
		for (int i = 0; i < size; i++) {
			// 编列列表，搜索提出挑战用户
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// 获取第i+1个对象
			if (satTemp.getName().equals(name)) {
				// 向该用户发送对方正在忙的信息
				try {
					satTemp.dout.writeUTF("<#BUSY#>");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			}

		}
	}

	public void move(String msg) {
		String name = msg.substring(8, msg.length() - 4);// 获取接收方的名字
		Vector v = father.onlineList;// 获取在线用户列表
		int size = v.size();// 获得在线用户列表大小
		for (int i = 0; i < size; i++) {
			// 编列列表，搜索接收方
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// 获取第i+1个对象
			if (satTemp.getName().equals(name)) {
				// 将信息发给接收方
				try {
					satTemp.dout.writeUTF(msg);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			}

		}
	}

	public void renshu(String msg) {
		String name = msg.substring(10);// 获取接收方的名字
		Vector v = father.onlineList;// 获取在线用户列表
		int size = v.size();// 获得在线用户列表大小
		for (int i = 0; i < size; i++) {
			// 编列列表，搜索接收方
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// 获取第i+1个对象
			if (satTemp.getName().equals(name)) {
				// 将信息发给接收方
				try {
					satTemp.dout.writeUTF(msg);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			}

		}
	}

}
