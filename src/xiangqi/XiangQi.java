package xiangqi;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class XiangQi extends JFrame implements ActionListener {

	public static final Color bgColor = new Color(245, 250, 160);// 棋盘的背景色
	public static final Color focusbg = new Color(242, 242, 242);// 棋盘选中后的背景色
	public static final Color focuschar = new Color(96, 95, 91);// 棋盘选中后的字符颜色
	public static final Color color1 = new Color(249, 183, 173);// 红方的颜色
	public static final Color color2 = Color.white;// 白方的颜色

	JLabel jlHost = new JLabel("主机名");// 创建提示输入主机名的标签
	JLabel jlPort = new JLabel("端口号");// 创建提示输入端口号的标签
	JLabel jlNickName = new JLabel("昵  称");// 创建提示输入昵称的标签

	JTextField jtfHost = new JTextField("127.0.0.1");// 创建输入主机名的文本框，默认值是"127.0.0.1"
	JTextField jtfPort = new JTextField("9999");// 创建输入端口号的文本框，默认值是9999
	JTextField jtfNickName = new JTextField("Player1");// 创建输入昵称的文本框，默认值是Player1

	JButton jbConnect = new JButton("连  接");// 创建连接按钮
	JButton jbDisconnect = new JButton("断  开");// 创建断开按钮
	JButton jbFail = new JButton("认  输");// 创建认输按钮
	JButton jbChallenge = new JButton("挑  战");// 创建挑战按钮
	JComboBox jcbNickList = new JComboBox();// 创建存放当前用户的下拉列表框
	JButton jbYChallenge = new JButton("接收挑战");// 创建接收挑战按钮
	JButton jbNChallenge = new JButton("拒绝挑战");// 创建拒绝挑战按钮

	int width = 60;// 设置棋盘两线之间的距离，单位为像素
	QiZi[][] qiZi = new QiZi[9][10];// 创建棋子数组
	QiPan jpz = new QiPan(qiZi, width, this);// 创建棋盘
	JPanel jpy = new JPanel();// 创建一个JPanel
	JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpz, jpy);// 创建一个JSplitPane

	boolean caiPan = false;// 能否走棋的标志位
	int color = 0;// 0代表红棋，1代表白棋
	Socket sc;// 声明Socket引用
	ClientAgentThread cat;// 声明引用

	public XiangQi() {
		this.initialComponent();// 初始化控件
		this.addListener();// 为相应控件注册事件监听器
		this.initialState();// 初始化状态
		this.initialQiZi();// 初始化棋子,这里先初始化棋子，再初始化窗体
		this.initialFrame();// 初始化窗体
	}

	public void initialComponent() {
		jpy.setLayout(null);// 设为空布局
		this.jlHost.setBounds(10, 10, 50, 20);
		jpy.add(this.jlHost);// 添加主机名标签
		this.jtfHost.setBounds(70, 10, 80, 20);
		jpy.add(this.jtfHost);// 添加用于输入主机名的文本框
		this.jlPort.setBounds(10, 40, 50, 20);
		jpy.add(this.jlPort);// 添加端口号标签
		this.jtfPort.setBounds(70, 40, 80, 20);
		jpy.add(this.jtfPort);// 添加用于输入端口号的文本框
		this.jlNickName.setBounds(10, 70, 50, 20);
		jpy.add(this.jlNickName);// 添加昵称标签
		this.jtfNickName.setBounds(70, 70, 80, 20);
		jpy.add(this.jtfNickName);// 添加用于输入昵称的文本框
		this.jbConnect.setBounds(10, 100, 80, 20);
		jpy.add(this.jbConnect);// 添加连接按钮
		this.jbDisconnect.setBounds(100, 100, 80, 20);
		jpy.add(this.jbDisconnect);// 添加断开按钮
		this.jcbNickList.setBounds(20, 130, 130, 20);
		jpy.add(this.jcbNickList);// 添加用于显示当前用户的下拉列表框
		this.jbChallenge.setBounds(10, 160, 86, 20);
		jpy.add(this.jbChallenge);// 添加挑战按钮
		this.jbFail.setBounds(105, 160, 86, 20);
		jpy.add(this.jbFail);// 添加认输按钮
		this.jbYChallenge.setBounds(10, 190, 86, 20);
		jpy.add(this.jbYChallenge);// 添加接受挑战按钮
		this.jbNChallenge.setBounds(105, 190, 86, 20);
		jpy.add(this.jbNChallenge);// 添加拒绝挑战按钮

	}

	public void addListener() {
		this.jbConnect.addActionListener(this);// 为连接按钮注册事件监听器
		this.jbDisconnect.addActionListener(this);// 为断开按钮注册事件监听器
		this.jbChallenge.addActionListener(this);// 为挑战按钮注册事件监听器
		this.jbFail.addActionListener(this);// 为认输按钮注册事件监听器
		this.jbYChallenge.addActionListener(this);// 为同意挑战按钮注册事件监听器
		this.jbNChallenge.addActionListener(this);// 为拒绝挑战注册事件监听器
	}

	public void initialState() {
		this.jbDisconnect.setEnabled(false);// 将断开按钮设为不可用
		this.jbChallenge.setEnabled(false);// 将挑战按钮设为不可用
		this.jbYChallenge.setEnabled(false);// 将同意挑战按钮设为不可用
		this.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
		this.jbFail.setEnabled(false);// 将认输按钮设为不可用
	}

	public void initialQiZi() {

		// 初始化各个棋子
		qiZi[0][0] = new QiZi(color1, "車", 0, 0);
		qiZi[1][0] = new QiZi(color1, "馬", 1, 0);
		qiZi[2][0] = new QiZi(color1, "相", 2, 0);
		qiZi[3][0] = new QiZi(color1, "仕", 3, 0);
		qiZi[4][0] = new QiZi(color1, "帥", 4, 0);
		qiZi[5][0] = new QiZi(color1, "仕", 5, 0);
		qiZi[6][0] = new QiZi(color1, "相", 6, 0);
		qiZi[7][0] = new QiZi(color1, "馬", 7, 0);
		qiZi[8][0] = new QiZi(color1, "車", 8, 0);
		qiZi[1][2] = new QiZi(color1, "砲", 1, 2);
		qiZi[7][2] = new QiZi(color1, "砲", 7, 2);
		qiZi[0][3] = new QiZi(color1, "卒", 0, 3);
		qiZi[2][3] = new QiZi(color1, "卒", 2, 3);
		qiZi[4][3] = new QiZi(color1, "卒", 4, 3);
		qiZi[6][3] = new QiZi(color1, "卒", 6, 3);
		qiZi[8][3] = new QiZi(color1, "卒", 8, 3);
		qiZi[0][9] = new QiZi(color2, "车", 0, 9);
		qiZi[1][9] = new QiZi(color2, "马", 1, 9);
		qiZi[2][9] = new QiZi(color2, "象", 2, 9);
		qiZi[3][9] = new QiZi(color2, "士", 3, 9);
		qiZi[4][9] = new QiZi(color2, "将", 4, 9);
		qiZi[5][9] = new QiZi(color2, "士", 5, 9);
		qiZi[6][9] = new QiZi(color2, "象", 6, 9);
		qiZi[7][9] = new QiZi(color2, "马", 7, 9);
		qiZi[8][9] = new QiZi(color2, "车", 8, 9);
		qiZi[1][7] = new QiZi(color2, "炮", 1, 7);
		qiZi[7][7] = new QiZi(color2, "炮", 7, 7);
		qiZi[0][6] = new QiZi(color2, "兵", 0, 6);
		qiZi[2][6] = new QiZi(color2, "兵", 2, 6);
		qiZi[4][6] = new QiZi(color2, "兵", 4, 6);
		qiZi[6][6] = new QiZi(color2, "兵", 6, 6);
		qiZi[8][6] = new QiZi(color2, "兵", 8, 6);

	}

	public void initialFrame() {
		this.setTitle("中国象棋——客户端");// 设置窗体标题
		Image image = new ImageIcon("").getImage();
		this.setIconImage(image);// 设置图标
		this.add(this.jsp);// 添加分割线JSplitPane
		jsp.setDividerLocation(680);// 设置分割线位置
		jsp.setDividerSize(4);// 设置分割线宽度
		this.setBounds(200, 20, 900, 700);// 设置窗体大小
		this.setVisible(true);// 设置可见性
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				if (cat == null)// 客户端代理线程为空，直接退出
				{
					System.exit(0);// 退出
					return;
				}
				try {
					if (cat.tiaozhanzhe != null)// 正在下棋中
					{
						try {
							// 发送认输信息
							cat.dout.writeUTF("<#RENSHU#>" + cat.tiaozhanzhe);
						} catch (Exception ee) {
							ee.printStackTrace();
						}
					}
					cat.dout.writeUTF("<#CLIENT_LEAVE#>");// 向服务器发送离开信息
					cat.flag = false;// 终止客户端代理线程
					cat = null;

				} catch (Exception ee) {
					ee.printStackTrace();
				}
				System.exit(0);// 退出
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == this.jbConnect) {
			// 当单击连接按钮时
			this.jbConnect_event();
		} else if (e.getSource() == this.jbDisconnect) {
			// 当单击断开按钮时
			this.jbDisconnect_event();
		} else if (e.getSource() == this.jbChallenge) {
			// 当单击挑战按钮时
			this.jbChallenge_event();
		} else if (e.getSource() == this.jbYChallenge) {
			// 当单击同意挑战按钮时
			this.jbYChallenge_event();
		} else if (e.getSource() == this.jbNChallenge) {
			// 当单击拒绝挑战按钮时
			this.jbNChallenge_event();
		} else if (e.getSource() == this.jbFail) {
			// 当单击认输按钮时
			this.jbFail_event();
		}
	}

	public void jbConnect_event() {

		// 单击连接按钮时的业务处理代码
		int port = 0;
		try {
			// 获得用户输入的端口号并转化为整型
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
		String name = this.jtfNickName.getText().trim();// 获得昵称
		if (name.length() == 0) {
			// 昵称为空，给出错误提示信息
			JOptionPane.showMessageDialog(this, "玩家姓名不能为空", "错误", JOptionPane.ERROR_MESSAGE);

			return;// 此处返回到点击这个按钮之前的状态，相当于这次点击无效
		}
		try {

			sc = new Socket(this.jtfHost.getText().trim(), port);// 创建Socket对象
			this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);// 将连接按钮设为不可用
			this.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
			this.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
			this.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
			this.jbFail.setEnabled(false);// 将认输按钮设为不可用

			cat = new ClientAgentThread(this);
			cat.start();// 创建并启动客户端线程

			// 连接成功，给出提示消息
			JOptionPane.showMessageDialog(this, "已连接到服务器", "提示", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "连接服务器失败", "错误", JOptionPane.ERROR_MESSAGE);

			return;// 此处返回到点击这个按钮之前的状态，相当于这次点击无效
		}
	}

	public void jbDisconnect_event() {
		// 单击断开按钮事件的业务处理代码
		try {
			this.cat.dout.writeUTF("<#CLIENT_LEAVE#>");// 向服务器发送离开的信息
			this.cat.flag = false;// 终止客户端代理线程
			this.cat = null;

			this.jtfHost.setEnabled(!false);// 将用于输入主机名的文本框设为可用
			this.jtfPort.setEnabled(!false);// 将用于输入端口号的文本框设为可用
			this.jtfNickName.setEnabled(!false);// 将用于输入昵称的文本框设为可用
			this.jbConnect.setEnabled(!false);// 将连接按钮设为可用
			this.jbDisconnect.setEnabled(!true);// 将断开按钮设为不可用
			this.jbChallenge.setEnabled(!true);// 将挑战按钮设为不可用
			this.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
			this.jbFail.setEnabled(false);// 将认输按钮设为不可用

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void jbChallenge_event() {
		// 获取用户选中的挑战的对象
		Object o = this.jcbNickList.getSelectedItem();
		if (o == null || ((String) o).equals("")) {
			// 当未选中挑战对象，给出错误提示信息
			JOptionPane.showMessageDialog(this, "请选择对方名字", "错误", JOptionPane.ERROR_MESSAGE);

		} else {
			String name2 = (String) this.jcbNickList.getSelectedItem();// 获得挑战对象
			try {
				this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
				this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
				this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
				this.jbConnect.setEnabled(false);// 将连接按钮设为不可用
				this.jbDisconnect.setEnabled(!true);// 将断开按钮设为不可用
				this.jbChallenge.setEnabled(!true);// 将挑战按钮设为不可用
				this.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
				this.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
				this.jbFail.setEnabled(false);// 将认输按钮设为不可用
				this.cat.tiaozhanzhe = name2;// 设置挑战对象
				this.caiPan = true;
				this.color = 0;
				this.cat.dout.writeUTF("<#TIAO_ZHAN#>" + name2);// 发送挑战信息
				// 给出挑战信息发出的提示信息
				JOptionPane.showMessageDialog(this, "已提出挑战，请等待...", "提示", JOptionPane.INFORMATION_MESSAGE);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void jbYChallenge_event() {
		try {
			// 发送同意挑战的信息
			this.cat.dout.writeUTF("<#TONG_YI#>" + this.cat.tiaozhanzhe);
			this.caiPan = false;// 将caiPan设为false
			this.color = 1;// 将color设为1
			this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);// 将连接按钮设为不可用
			this.jbDisconnect.setEnabled(!true);// 将断开按钮设为不可用
			this.jbChallenge.setEnabled(!true);// 将挑战按钮设为不可用
			this.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
			this.jbFail.setEnabled(!false);// 将认输按钮设为可用

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jbNChallenge_event() {
		try {
			// 发送拒绝挑战的信息
			this.cat.dout.writeUTF("<#BUTONG_YI#>" + this.cat.tiaozhanzhe);
			this.cat.tiaozhanzhe = null;// 将tiaoZhanZhe设为空
			this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);// 将连接按钮设为不可用
			this.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
			this.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
			this.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
			this.jbFail.setEnabled(false);// 将认输按钮设为不可用

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jbFail_event() {
		try {
			// 发送认输的信息
			this.cat.dout.writeUTF("<#RENSHU#>" + this.cat.tiaozhanzhe);
			this.cat.tiaozhanzhe = null;// 将tiaoZhanZhe设为空
			this.next();// 初始化下一局
			this.caiPan = false;
			this.color = 0;
			this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);// 将连接按钮设为不可用
			this.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
			this.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
			this.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
			this.jbFail.setEnabled(false);// 将认输按钮设为不可用

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void next() {
		// 将棋子数组置空
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				this.qiZi[i][j] = null;
			}
		}
		this.caiPan = false;
		this.initialQiZi();// 重新初始化棋子
		this.repaint();// 重绘
	}

	public static void main(String args[]) {
		new XiangQi();
	}
}
