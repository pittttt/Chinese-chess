package xiangqi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class ClientAgentThread extends Thread {

	XiangQi father;// ����XiangQi������
	boolean flag = true;// �����̵߳ı�־λ
	DataInputStream din;// �����������������
	DataOutputStream dout;
	String tiaozhanzhe = null;// ���ڼ�¼������ս�Ķ���

	public ClientAgentThread(XiangQi father) {
		this.father = father;
		try {
			din = new DataInputStream(father.sc.getInputStream());// �����������������
			dout = new DataOutputStream(father.sc.getOutputStream());

			String name = father.jtfNickName.getText().trim();// ����ǳ�
			dout.writeUTF("<#NICK_NAME#>" + name);// �����ǳƵ�������

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		while (flag) {
			try {
				String msg = din.readUTF().trim();// ��÷�������������Ϣ
				if (msg.startsWith("<#NAME_CHONGMING#>")) {
					// �յ�������Ϣ
					this.name_chongming();
				} else if (msg.startsWith("<#NICK_LIST#>")) {
					// �յ��ǳ��б�
					this.nick_list(msg);
				} else if (msg.startsWith("<#SERVER_DOWN#>")) {
					// �յ��������뿪����Ϣ
					this.server_down();
				} else if (msg.startsWith("<#TIAO_ZHAN#>")) {
					// �յ���ս��Ϣ
					this.tiao_zhan(msg);
				} else if (msg.startsWith("<#TONG_YI#>")) {
					// �յ��Է�������ս����Ϣ
					this.tong_yi();
				} else if (msg.startsWith("<#BUTONG_YI#>")) {
					// �յ��Է��ܾ���ս����Ϣ
					this.butong_yi();
				} else if (msg.startsWith("<#BUSY#>")) {
					// �յ��Է�æ����Ϣ
					this.busy();
				} else if (msg.startsWith("<#MOVE#>")) {
					// �յ�������Ϣ
					this.move(msg);
				} else if (msg.startsWith("<#RENSHU#>")) {
					// �յ��������Ϣ
					this.renshu();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void name_chongming() {
		try {
			// ����������ʾ��Ϣ
			JOptionPane.showMessageDialog(this.father, "����������Ѿ���ռ�ã���������д��", "����", JOptionPane.ERROR_MESSAGE);
			din.close();// �ر�������
			dout.close();// �ر������
			this.father.jtfHost.setEnabled(!false);// �������������������ı�����Ϊ����
			this.father.jtfPort.setEnabled(!false);// ����������˿ںŵ��ı�����Ϊ����
			this.father.jtfNickName.setEnabled(!false);// �����������ǳƵ��ı�����Ϊ����
			this.father.jbConnect.setEnabled(!false);// �����Ӱ�ť��Ϊ����
			this.father.jbDisconnect.setEnabled(!true);// ���Ͽ���ť��Ϊ������
			this.father.jbChallenge.setEnabled(!true);// ����ս��ť��Ϊ������
			this.father.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
			this.father.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
			this.father.jbFail.setEnabled(false);// �����䰴ť��Ϊ������
			father.sc.close();// �ر�Socket
			father.sc = null;
			father.cat = null;
			flag = false;// ��ֹ�ÿͻ��˴����߳�

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nick_list(String msg) {
		String s = msg.substring(13);// �ֽⲢ�õ��û���Ϣ
		String[] na = s.split("\\|");
		Vector v = new Vector();// ����Vector����
		for (int i = 0; i < na.length; i++) {
			if (na[i].trim().length() != 0 && (!na[i].trim().equals(father.jtfNickName.getText().trim()))) {
				v.add(na[i]);// ���ǳ�ȫ����ӵ�Vector��
			}
		}
		father.jcbNickList.setModel(new DefaultComboBoxModel(v));// ���������б��ֵ
	}

	public void server_down() {
		this.father.jtfHost.setEnabled(!false);// �������������������ı�����Ϊ����
		this.father.jtfPort.setEnabled(!false);// ����������˿ںŵ��ı�����Ϊ����
		this.father.jtfNickName.setEnabled(!false);// �����������ǳƵ��ı�����Ϊ����
		this.father.jbConnect.setEnabled(!false);// �����Ӱ�ť��Ϊ����
		this.father.jbDisconnect.setEnabled(!true);// ���Ͽ���ť��Ϊ������
		this.father.jbChallenge.setEnabled(!true);// ����ս��ť��Ϊ������
		this.father.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
		this.father.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
		this.father.jbFail.setEnabled(false);// �����䰴ť��Ϊ������
		this.flag = false;// ��ֹ�ÿͻ��˴����߳�
		father.cat = null;

		// �����������뿪����ʾ��Ϣ
		JOptionPane.showMessageDialog(this.father, "������ֹͣ������", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}

	public void tiao_zhan(String msg) {
		try {
			String name = msg.substring(13);// �����ս�ߵ��ǳ�
			if (this.tiaozhanzhe == null) {
				// �����ҿ���
				tiaozhanzhe = msg.substring(13);// ��Ϊ��ս�ߵ��ǳ�
				this.father.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
				this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
				this.father.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
				this.father.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
				this.father.jbDisconnect.setEnabled(!true);// ���Ͽ���ť��Ϊ������
				this.father.jbChallenge.setEnabled(!true);// ����ս��ť��Ϊ������
				this.father.jbYChallenge.setEnabled(!false);// ��������ս��ť��Ϊ����
				this.father.jbNChallenge.setEnabled(!false);// ���ܾ���ս��ť��Ϊ����
				this.father.jbFail.setEnabled(false);// �����䰴ť��Ϊ������

				// ������ս��Ϣ
				JOptionPane.showMessageDialog(this.father, tiaozhanzhe + "������ս!!!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// ��������æµ�У��򷵻�һ��busy��Ϣ
				this.dout.writeUTF("<#BUSY#>" + name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tong_yi() {
		this.father.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
		this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
		this.father.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
		this.father.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
		this.father.jbDisconnect.setEnabled(!true);// ���Ͽ���ť��Ϊ������
		this.father.jbChallenge.setEnabled(!true);// ����ս��ť��Ϊ������
		this.father.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
		this.father.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
		this.father.jbFail.setEnabled(!false);// �����䰴ť��Ϊ����
		JOptionPane.showMessageDialog(this.father, "�Է�����������ս����������(����)", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}

	public void butong_yi() {

		this.father.caiPan = false;
		this.father.color = 0;
		this.father.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
		this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
		this.father.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
		this.father.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
		this.father.jbDisconnect.setEnabled(true);// ���Ͽ���ť��Ϊ����
		this.father.jbChallenge.setEnabled(true);// ����ս��ť��Ϊ����
		this.father.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
		this.father.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
		this.father.jbFail.setEnabled(false);// �����䰴ť��Ϊ������

		// �����ܾ���ս��ʾ��Ϣ
		JOptionPane.showMessageDialog(this.father, "�Է��ܾ�������ս��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		this.tiaozhanzhe = null;
	}

	public void busy() {
		this.father.caiPan = false;
		this.father.color = 0;
		this.father.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
		this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
		this.father.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
		this.father.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
		this.father.jbDisconnect.setEnabled(true);// ���Ͽ���ť��Ϊ����
		this.father.jbChallenge.setEnabled(true);// ����ս��ť��Ϊ����
		this.father.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
		this.father.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
		this.father.jbFail.setEnabled(false);// �����䰴ť��Ϊ������

		// �����ܾ���ս��ʾ��Ϣ
		JOptionPane.showMessageDialog(this.father, "�Է�æµ�У�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		this.tiaozhanzhe = null;
	}

	public void move(String msg) {
		int length = msg.length();
		int startI = Integer.parseInt(msg.substring(length - 4, length - 3));// ������ӵ�ԭʼλ��
		int startJ = Integer.parseInt(msg.substring(length - 3, length - 2));
		int endI = Integer.parseInt(msg.substring(length - 2, length - 1));// ���������λ��
		int endJ = Integer.parseInt(msg.substring(length - 1));
		this.father.jpz.move(startI, startJ, endI, endJ);// ���÷�������
		this.father.caiPan = true;
	}

	public void renshu() {
		// ������ʤ��Ϣ
		JOptionPane.showMessageDialog(this.father, "��ϲ��,���ʤ,�Է�����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		this.tiaozhanzhe = null;
		this.father.color = 0;
		this.father.caiPan = false;
		this.father.next();// ������һ��
		this.father.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
		this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
		this.father.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
		this.father.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
		this.father.jbDisconnect.setEnabled(true);// ���Ͽ���ť��Ϊ����
		this.father.jbChallenge.setEnabled(true);// ����ս��ť��Ϊ����
		this.father.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
		this.father.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
		this.father.jbFail.setEnabled(false);// �����䰴ť��Ϊ������

	}
}
