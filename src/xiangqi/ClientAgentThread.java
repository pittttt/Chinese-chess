package xiangqi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class ClientAgentThread extends Thread {

	XiangQi father;// 声明XiangQi的引用
	boolean flag = true;// 控制线程的标志位
	DataInputStream din;// 声明数据输入输出流
	DataOutputStream dout;
	String tiaozhanzhe = null;// 用于记录正在挑战的对手

	public ClientAgentThread(XiangQi father) {
		this.father = father;
		try {
			din = new DataInputStream(father.sc.getInputStream());// 创建数据输入输出流
			dout = new DataOutputStream(father.sc.getOutputStream());

			String name = father.jtfNickName.getText().trim();// 获得昵称
			dout.writeUTF("<#NICK_NAME#>" + name);// 发送昵称到服务器

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		while (flag) {
			try {
				String msg = din.readUTF().trim();// 获得服务器发来的消息
				if (msg.startsWith("<#NAME_CHONGMING#>")) {
					// 收到重名信息
					this.name_chongming();
				} else if (msg.startsWith("<#NICK_LIST#>")) {
					// 收到昵称列表
					this.nick_list(msg);
				} else if (msg.startsWith("<#SERVER_DOWN#>")) {
					// 收到服务器离开的信息
					this.server_down();
				} else if (msg.startsWith("<#TIAO_ZHAN#>")) {
					// 收到挑战信息
					this.tiao_zhan(msg);
				} else if (msg.startsWith("<#TONG_YI#>")) {
					// 收到对方接收挑战的信息
					this.tong_yi();
				} else if (msg.startsWith("<#BUTONG_YI#>")) {
					// 收到对方拒绝挑战的信息
					this.butong_yi();
				} else if (msg.startsWith("<#BUSY#>")) {
					// 收到对方忙的信息
					this.busy();
				} else if (msg.startsWith("<#MOVE#>")) {
					// 收到走棋信息
					this.move(msg);
				} else if (msg.startsWith("<#RENSHU#>")) {
					// 收到认输的信息
					this.renshu();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void name_chongming() {
		try {
			// 给出重名提示信息
			JOptionPane.showMessageDialog(this.father, "该玩家名称已经被占用，请重新填写！", "错误", JOptionPane.ERROR_MESSAGE);
			din.close();// 关闭输入流
			dout.close();// 关闭输出流
			this.father.jtfHost.setEnabled(!false);// 将用于输入主机名的文本框设为可用
			this.father.jtfPort.setEnabled(!false);// 将用于输入端口号的文本框设为可用
			this.father.jtfNickName.setEnabled(!false);// 将用于输入昵称的文本框设为可用
			this.father.jbConnect.setEnabled(!false);// 将连接按钮设为可用
			this.father.jbDisconnect.setEnabled(!true);// 将断开按钮设为不可用
			this.father.jbChallenge.setEnabled(!true);// 将挑战按钮设为不可用
			this.father.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
			this.father.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
			this.father.jbFail.setEnabled(false);// 将认输按钮设为不可用
			father.sc.close();// 关闭Socket
			father.sc = null;
			father.cat = null;
			flag = false;// 终止该客户端代理线程

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nick_list(String msg) {
		String s = msg.substring(13);// 分解并得到用户信息
		String[] na = s.split("\\|");
		Vector v = new Vector();// 创建Vector对象
		for (int i = 0; i < na.length; i++) {
			if (na[i].trim().length() != 0 && (!na[i].trim().equals(father.jtfNickName.getText().trim()))) {
				v.add(na[i]);// 将昵称全部添加到Vector中
			}
		}
		father.jcbNickList.setModel(new DefaultComboBoxModel(v));// 设置下拉列表的值
	}

	public void server_down() {
		this.father.jtfHost.setEnabled(!false);// 将用于输入主机名的文本框设为可用
		this.father.jtfPort.setEnabled(!false);// 将用于输入端口号的文本框设为可用
		this.father.jtfNickName.setEnabled(!false);// 将用于输入昵称的文本框设为可用
		this.father.jbConnect.setEnabled(!false);// 将连接按钮设为可用
		this.father.jbDisconnect.setEnabled(!true);// 将断开按钮设为不可用
		this.father.jbChallenge.setEnabled(!true);// 将挑战按钮设为不可用
		this.father.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
		this.father.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
		this.father.jbFail.setEnabled(false);// 将认输按钮设为不可用
		this.flag = false;// 终止该客户端代理线程
		father.cat = null;

		// 给出服务器离开的提示信息
		JOptionPane.showMessageDialog(this.father, "服务器停止！！！", "提示", JOptionPane.INFORMATION_MESSAGE);
	}

	public void tiao_zhan(String msg) {
		try {
			String name = msg.substring(13);// 获得挑战者的昵称
			if (this.tiaozhanzhe == null) {
				// 如果玩家空闲
				tiaozhanzhe = msg.substring(13);// 赋为挑战者的昵称
				this.father.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
				this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
				this.father.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
				this.father.jbConnect.setEnabled(false);// 将连接按钮设为不可用
				this.father.jbDisconnect.setEnabled(!true);// 将断开按钮设为不可用
				this.father.jbChallenge.setEnabled(!true);// 将挑战按钮设为不可用
				this.father.jbYChallenge.setEnabled(!false);// 将接受挑战按钮设为可用
				this.father.jbNChallenge.setEnabled(!false);// 将拒绝挑战按钮设为可用
				this.father.jbFail.setEnabled(false);// 将认输按钮设为不可用

				// 给出挑战信息
				JOptionPane.showMessageDialog(this.father, tiaozhanzhe + "向你挑战!!!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// 如果该玩家忙碌中，则返回一个busy信息
				this.dout.writeUTF("<#BUSY#>" + name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tong_yi() {
		this.father.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
		this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.father.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
		this.father.jbConnect.setEnabled(false);// 将连接按钮设为不可用
		this.father.jbDisconnect.setEnabled(!true);// 将断开按钮设为不可用
		this.father.jbChallenge.setEnabled(!true);// 将挑战按钮设为不可用
		this.father.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
		this.father.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
		this.father.jbFail.setEnabled(!false);// 将认输按钮设为可用
		JOptionPane.showMessageDialog(this.father, "对方接收您的挑战！您先走棋(红棋)", "提示", JOptionPane.INFORMATION_MESSAGE);
	}

	public void butong_yi() {

		this.father.caiPan = false;
		this.father.color = 0;
		this.father.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
		this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.father.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
		this.father.jbConnect.setEnabled(false);// 将连接按钮设为不可用
		this.father.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
		this.father.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
		this.father.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
		this.father.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
		this.father.jbFail.setEnabled(false);// 将认输按钮设为不可用

		// 给出拒绝挑战提示信息
		JOptionPane.showMessageDialog(this.father, "对方拒绝您的挑战！", "提示", JOptionPane.INFORMATION_MESSAGE);
		this.tiaozhanzhe = null;
	}

	public void busy() {
		this.father.caiPan = false;
		this.father.color = 0;
		this.father.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
		this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.father.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
		this.father.jbConnect.setEnabled(false);// 将连接按钮设为不可用
		this.father.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
		this.father.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
		this.father.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
		this.father.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
		this.father.jbFail.setEnabled(false);// 将认输按钮设为不可用

		// 给出拒绝挑战提示信息
		JOptionPane.showMessageDialog(this.father, "对方忙碌中！", "提示", JOptionPane.INFORMATION_MESSAGE);
		this.tiaozhanzhe = null;
	}

	public void move(String msg) {
		int length = msg.length();
		int startI = Integer.parseInt(msg.substring(length - 4, length - 3));// 获得棋子的原始位置
		int startJ = Integer.parseInt(msg.substring(length - 3, length - 2));
		int endI = Integer.parseInt(msg.substring(length - 2, length - 1));// 获得走棋后的位置
		int endJ = Integer.parseInt(msg.substring(length - 1));
		this.father.jpz.move(startI, startJ, endI, endJ);// 调用方法走棋
		this.father.caiPan = true;
	}

	public void renshu() {
		// 给出获胜信息
		JOptionPane.showMessageDialog(this.father, "恭喜你,你获胜,对方认输", "提示", JOptionPane.INFORMATION_MESSAGE);
		this.tiaozhanzhe = null;
		this.father.color = 0;
		this.father.caiPan = false;
		this.father.next();// 进入下一盘
		this.father.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
		this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.father.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
		this.father.jbConnect.setEnabled(false);// 将连接按钮设为不可用
		this.father.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
		this.father.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
		this.father.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
		this.father.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
		this.father.jbFail.setEnabled(false);// 将认输按钮设为不可用

	}
}
