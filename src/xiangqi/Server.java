package xiangqi;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class Server extends JFrame implements ActionListener {
	JLabel jlPort = new JLabel("端口号");// 创建提示输入端口号标签
	JTextField jtfPort = new JTextField("9999");// 用于输入端口号的文本框
	JButton jbStart = new JButton("启动");// 创建启动按钮
	JButton jbStop = new JButton("关闭");// 创建关闭按钮
	JPanel jps = new JPanel();// 创建一个JPanel对象
	JList jlUserOnline = new JList();// 创建用于显示当前用户的JList
	JScrollPane jspx = new JScrollPane(jlUserOnline);// 将显示当前用户的JList放在JScrollPane中,JScrollPane可以理解为一个带滚动条的窗口
	JSplitPane jspz = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspx, jps);// 创建JSplitPane对象，JSplitPane为将窗口分为两个部分的分割线
	ServerSocket ss;// 声明ServerSocket引用
	ServerThread st;// 声明ServerThread引用
	Vector onlineList = new Vector();// 创建存放当前在线用户的Vector对象，此对象是私密的

	public Server() {

		this.initialComponent();// 初始化控件
		this.addListener();// 为相应的控件注册事件监听器
		this.initialFrame();// 初始化窗体
	}

	public void initialComponent() {
		jps.setLayout(null);// 设为空布局
		this.jlPort.setBounds(20, 20, 50, 20);
		jps.add(jlPort);// 添加用于提示输入端口号的标签
		this.jtfPort.setBounds(85, 20, 60, 20);
		jps.add(this.jtfPort);// 添加用于输入端口号的文本框
		this.jbStart.setBounds(10, 50, 60, 20);
		jps.add(this.jbStart);// 添加开始按钮
		this.jbStop.setBounds(85, 50, 60, 20);
		jps.add(this.jbStop);// 添加关闭按钮
		this.jbStop.setEnabled(false);// 将关闭按钮设为不可用

	}

	public void addListener() {
		this.jbStart.addActionListener(this);// 为开始按钮注册事件监听器
		this.jbStop.addActionListener(this);// 为关闭按钮注册事件监听器
	}

	public void initialFrame() {
		this.setTitle("象棋——服务器端");// 设置窗体标题
		Image image = new ImageIcon("").getImage();
		this.setIconImage(image);
		this.add(jspz);
		jspz.setDividerLocation(250);
		jspz.setDividerSize(4);// 设置分割线的位置和高度
		this.setBounds(450, 200, 420, 320);// 设置窗体位置和大小
		this.setVisible(true);// 设置可见性
		this.addWindowListener(
				// 为窗口关闭事件注册监听器
				new WindowAdapter() {

					public void windowClosing(WindowEvent e) {

						if (st == null)// 当服务器线程为空时直接退出
						{
							System.exit(0);
							return;

						}
						Vector v = onlineList;
						int size = v.size();
						for (int i = 0; i < size; i++) {
							// 当不为空时，向在线用户发送离线信息
							ServerAgentThread tempSat = (ServerAgentThread) v.get(i);// 获取第i+1个对象
							try {
								tempSat.dout.writeUTF("<#SERVER_DOWN#>");

							} catch (IOException e1) {
								e1.printStackTrace();
							}
							tempSat.flag = false;// 终止服务器代理线程
						}
						st.flag = false;// 终止服务器线程
						st = null;
						try {
							ss.close();// 关闭ServerSocket
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						v.clear();// 将在线用户列表清空
						refreshList();// 刷新列表
						System.exit(0);// 退出
					}

					private void setDefaultCloseOperation(int doNothingOnClose) {

					}
				});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.jbStart) {
			// 当单击启动按钮时
			this.jbStart_event();

		} else if (e.getSource() == this.jbStop) {
			// 点击关闭按钮时
			this.jbStop_event();

		}
	}

	public void jbStart_event() {
		// 单击启动按钮的业务处理代码
		int port = 0;
		try {
			// 获得用户输入的端口号，并转化为整型
			port = Integer.parseInt(this.jtfPort.getText().trim());
		} catch (Exception e) {
			// 端口号不是整数，给出提示信息
			JOptionPane.showMessageDialog(this, "端口号只能是整数", "错误", JOptionPane.ERROR_MESSAGE);

			return;// 此处返回到点击这个按钮之前的状态，相当于这次点击无效
		}
		if (port > 65535 || port < 0) {
			// 端口号不合法，给出提示信息
			JOptionPane.showMessageDialog(this, "端口号只能是0-65535的整数", "错误", JOptionPane.ERROR_MESSAGE);
			return;// 此处返回到点击这个按钮之前的状态，相当于这次点击无效
		}
		try {
			this.jbStart.setEnabled(false);// 将开始按钮设为不可用
			this.jtfPort.setEnabled(false);// 将用于输入端口的文本框设为不可用
			this.jbStop.setEnabled(true);// 将关闭按钮设为可用

			ss = new ServerSocket(port);// 创建ServerSocket对象，此时服务器启动，并且监听了port端口
			st = new ServerThread(this);// 创建服务器线程
			st.start();// 启动服务器线程

			// 给出服务器启动成功的提示信息
			JOptionPane.showMessageDialog(this, "服务器启动成功", "提示", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {

			// 给出服务器启动失败的提示信息
			JOptionPane.showMessageDialog(this, "服务器启动失败", "错误", JOptionPane.ERROR_MESSAGE);
			this.jbStart.setEnabled(true);// 将开始按钮设为可用
			this.jtfPort.setEnabled(true);// 将用于输入端口的文本框设为可用
			this.jbStop.setEnabled(false);// 将关闭按钮设为不可用

		}

	}

	public void jbStop_event() {

		// 单击关闭按钮的业务处理代码
		try {
			Vector v = onlineList;
			int size = v.size();
			for (int i = 0; i < size; i++) {
				// 向在线用户发送离线信息
				ServerAgentThread tempSat = (ServerAgentThread) v.get(i);// 获取第i+1个对象
				tempSat.dout.writeUTF("<#SERVER_DOWN#>");
				tempSat.flag = false;// 终止服务器代理线程
			}
			st.flag = false;// 终止服务器线程
			st = null;
			ss.close();// 关闭ServerSocket
			v.clear();// 将在线用户列表清空
			refreshList();// 刷新列表
			this.jbStart.setEnabled(true);// 将开始按钮设为可用
			this.jtfPort.setEnabled(true);// 将用于输入端口的文本框设为可用
			this.jbStop.setEnabled(false);// 将关闭按钮设为不可用

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void refreshList() {
		// 更新在线用户列表的业务处理代码
		Vector v = new Vector();
		int size = this.onlineList.size();
		for (int i = 0; i < size; i++) {
			// 遍历在线列表
			ServerAgentThread tempSat = (ServerAgentThread) this.onlineList.get(i);// 获取第i+1个对象
			String temps = tempSat.sc.getInetAddress().toString();
			temps = temps + "|" + tempSat.getName();// 获取所需信息
			v.add(temps);// 添加到Vector中
		}
		this.jlUserOnline.setListData(v);// 更新列表数据
	}

	public static void main(String args[]) {
		new Server();
	}
}
