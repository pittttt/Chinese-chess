package xiangqi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread {

	Server father;// ����Server������
	Socket sc;// ����Socket������
	DataInputStream din;// ���������������������������
	DataOutputStream dout;
	boolean flag = true;// �����̵߳ı�־λ

	public ServerAgentThread(Server father, Socket sc) {
		this.father = father;
		this.sc = sc;

		try {
			din = new DataInputStream(sc.getInputStream());// ��������������
			dout = new DataOutputStream(sc.getOutputStream());// �������������
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (flag) {
			try {
				String msg = din.readUTF().trim();// ���տͻ��˷�������Ϣ
				if (msg.startsWith("<#NICK_NAME#>")) {
					// �յ����û���������Ϣ
					this.nick_name(msg);
				} else if (msg.startsWith("<#CLIENT_LEAVE#>")) {
					// �յ��û��뿪����Ϣ
					this.client_leave(msg);
				} else if (msg.startsWith("<#TIAO_ZHAN#>")) {
					// �յ��û�������ս����Ϣ
					this.tiao_zhan(msg);
				} else if (msg.startsWith("<#TONG_YI#>")) {
					// �յ��û�������ս����Ϣ
					this.tong_yi(msg);
				} else if (msg.startsWith("<#BUTONG_YI#>")) {
					// �յ��û��ܾ���ս����Ϣ
					this.butong_yi(msg);
				} else if (msg.startsWith("<#BUSY#>")) {
					// �յ�����ս��æ
					this.busy(msg);
				} else if (msg.startsWith("<#MOVE#>")) {
					// �յ�������Ϣ
					this.move(msg);
				} else if (msg.startsWith("<#RENSHU#>")) {
					// �յ�ĳ�û��������Ϣ
					this.renshu(msg);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void nick_name(String msg) {
		String name = msg.substring(13);// ��ȡ�û����ǳ�

		this.setName(name);// �ø��ǳƸ��߳�ȡ����setName��Thread�µ�һ�����������߳�ȡ��
		Vector v = father.onlineList;// ��ȡ�����û��б�
		boolean isChongMing = false;
		int size = v.size();// ��ȡ�û��б��С
		for (int i = 0; i < size; i++) {
			// �����б��鿴�Ƿ��д��û���
			ServerAgentThread tempSat = (ServerAgentThread) v.get(i);// ��ȡ��i+1������
			if (tempSat.getName().equals(name)) {
				isChongMing = true;// ������������־λ��Ϊtrue
				break;
			}
		}
		if (isChongMing == true)// �������
		{
			try {
				dout.writeUTF("<#NAME_CHONGMING#>");// ��������Ϣ���͸��ͻ���
				din.close();// �ر�����������
				dout.close();// �ر����������
				sc.close();// �ر�Socket
				flag = false;// ��ֹ�÷����������߳�
			} catch (Exception e) {

				e.printStackTrace();
			}

		} else // ���������
		{
			v.add(this);// �����߳���ӵ������б�
			father.refreshList();// ˢ�·�����������Ϣ�б�
			String nickListMsg = "";
			size = v.size();// ��ȡ�����б��С
			for (int i = 0; i < size; i++) {
				ServerAgentThread tempSat = (ServerAgentThread) v.get(i);// ��ȡ��i+1������
				nickListMsg = nickListMsg + "|" + tempSat.getName();
				// �������б���������ַ���
			}
			nickListMsg = "<#NICK_LIST#>" + nickListMsg;
			Vector tempv = father.onlineList;
			size = tempv.size();
			for (int i = 0; i < size; i++) {
				// ���������б�
				ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);// ��ȡ��i+1������
				try {
					satTemp.dout.writeUTF(nickListMsg);// �����µ��б���Ϣ���͵������ͻ���
				} catch (IOException e) {

					e.printStackTrace();
				}
				if (satTemp != this) {
					// �������ͻ��˷������û����ߵ���Ϣ
					try {
						satTemp.dout.writeUTF("<#MSG#>" + this.getName() + "������...");
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		}
	}

	public void client_leave(String msg) {
		Vector tempv = father.onlineList;// ��ȡ�����б�
		tempv.remove(this);// �Ƴ����û�
		int size = tempv.size();
		String n1 = "<#NICK_LIST#>";
		for (int i = 0; i < size; i++) {
			// ���������б�
			ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);// ��ȡ��i+1������
			// ������ͻ��˷����û�������Ϣ
			try {
				satTemp.dout.writeUTF("<#MSG#>" + this.getName() + "������...");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			// ��֯��Ϣ�������û��б�
			n1 = n1 + "|" + satTemp.getName();
		}
		for (int i = 0; i < size; i++) {
			// �����µ��б���Ϣ���͵������ͻ���
			ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);// ��ȡ��i+1������
			try {
				satTemp.dout.writeUTF(n1);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		this.flag = false;// ��ֹ�÷����������߳�
		father.refreshList();// ���·����������û��б�
	}

	public void tiao_zhan(String msg) {
		try {
			String name1 = this.getName();// ��÷�����ս��Ϣ�û�������
			String name2 = msg.substring(13);// ��ñ���ս���û�����
			Vector v = father.onlineList;// ��ȡ�����û��б�
			int size = v.size();// ��������û��б��С
			for (int i = 0; i < size; i++) {
				// �����б���������ս�û�
				ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// ��ȡ��i+1������
				if (satTemp.getName().equals(name2)) {
					// ����û�������ս����˳�������ս�û�������

					satTemp.dout.writeUTF("<#TIAO_ZHAN#>" + name1);
					break;
				}
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	public void tong_yi(String msg) {
		String name = msg.substring(11);// ��ȡ�����ս���û�������
		Vector v = father.onlineList;// ��ȡ�����û��б�
		int size = v.size();// ��������û��б��С
		for (int i = 0; i < size; i++) {
			// �����б����������ս�û�
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// ��ȡ��i+1������
			if (satTemp.getName().equals(name)) {
				// ����û����ͶԷ�������ս����Ϣ
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
		String name = msg.substring(13);// ��ȡ�����ս���û�������
		Vector v = father.onlineList;// ��ȡ�����û��б�
		int size = v.size();// ��������û��б��С
		for (int i = 0; i < size; i++) {
			// �����б����������ս�û�
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// ��ȡ��i+1������
			if (satTemp.getName().equals(name)) {
				// ����û����ͶԷ��ܾ���ս����Ϣ
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
		String name = msg.substring(8);// ��ȡ�����ս���û�������
		Vector v = father.onlineList;// ��ȡ�����û��б�
		int size = v.size();// ��������û��б��С
		for (int i = 0; i < size; i++) {
			// �����б����������ս�û�
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// ��ȡ��i+1������
			if (satTemp.getName().equals(name)) {
				// ����û����ͶԷ�����æ����Ϣ
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
		String name = msg.substring(8, msg.length() - 4);// ��ȡ���շ�������
		Vector v = father.onlineList;// ��ȡ�����û��б�
		int size = v.size();// ��������û��б��С
		for (int i = 0; i < size; i++) {
			// �����б��������շ�
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// ��ȡ��i+1������
			if (satTemp.getName().equals(name)) {
				// ����Ϣ�������շ�
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
		String name = msg.substring(10);// ��ȡ���շ�������
		Vector v = father.onlineList;// ��ȡ�����û��б�
		int size = v.size();// ��������û��б��С
		for (int i = 0; i < size; i++) {
			// �����б��������շ�
			ServerAgentThread satTemp = (ServerAgentThread) v.get(i);// ��ȡ��i+1������
			if (satTemp.getName().equals(name)) {
				// ����Ϣ�������շ�
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
