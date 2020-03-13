package xiangqi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class QiPan extends JPanel implements MouseListener {

	private int width;// 棋盘两线之间的距离
	boolean focus = false;// 棋子的状态
	int jiang1_i = 4;// 帥的i坐标
	int jiang1_j = 0;// 帥的j坐标
	int jiang2_i = 4;// 将的i坐标
	int jiang2_j = 9;// 将的j坐标

	int startI = -1;// 棋子的开始位置
	int startJ = -1;

	int endI = -1;// 棋子的终止位置
	int endJ = -1;

	public QiZi qizi[][];// 棋子的数组
	XiangQi xq = null;// 声明XiangQi的引用
	GuiZe guiZe;// 声明GuiZe的引用

	public QiPan(QiZi qizi[][], int width, XiangQi xq) {
		this.xq = xq;
		this.qizi = qizi;
		this.width = width;
		guiZe = new GuiZe(qizi);
		this.addMouseListener(this);// 为棋盘添加鼠标事件监听器
		this.setBounds(0, 0, 700, 700);// 设置棋盘大小
		this.setLayout(null);// 设为空布局
	}

	public void paint(Graphics g1) {

		Graphics2D g = (Graphics2D) g1;// 获得Graphics2D对象
		// 打开抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color c = g.getColor();// 获得画笔颜色
		g.setColor(XiangQi.bgColor);// 将画笔设为背景色
		g.fill3DRect(60, 30, 580, 630, false);// 绘制一个矩形棋盘
		g.setColor(Color.black);// 将画笔颜色设为黑色
		for (int i = 80; i <= 620; i = i + 60) {
			// 绘制棋盘中的横线
			g.drawLine(110, i, 590, i);
		}
		g.drawLine(110, 80, 110, 620);// 绘制左边线
		g.drawLine(590, 80, 590, 620);// 绘制右边线

		for (int i = 170; i <= 530; i = i + 60) {
			// 绘制中间的竖线
			g.drawLine(i, 80, i, 320);
			g.drawLine(i, 380, i, 620);
		}
		g.drawLine(290, 80, 410, 200);// 绘制两边斜线
		g.drawLine(290, 200, 410, 80);
		g.drawLine(290, 500, 410, 620);
		g.drawLine(290, 620, 410, 500);

		this.smallLine(g, 1, 2);// 绘制砲所在位置标志
		this.smallLine(g, 7, 2);// 绘制砲所在位置标志

		this.smallLine(g, 0, 3);// 绘制卒所在位置标志
		this.smallLine(g, 2, 3);// 绘制卒所在位置标志
		this.smallLine(g, 4, 3);// 绘制卒所在位置标志
		this.smallLine(g, 6, 3);// 绘制卒所在位置标志
		this.smallLine(g, 8, 3);// 绘制卒所在位置标志

		this.smallLine(g, 0, 6);// 绘制兵所在位置标志
		this.smallLine(g, 2, 6);// 绘制兵所在位置标志
		this.smallLine(g, 4, 6);// 绘制兵所在位置标志
		this.smallLine(g, 6, 6);// 绘制兵所在位置标志
		this.smallLine(g, 8, 6);// 绘制兵所在位置标志

		this.smallLine(g, 1, 7);// 绘制炮所在位置标志
		this.smallLine(g, 7, 7);// 绘制炮所在位置标志

		g.setColor(Color.black);
		Font font1 = new Font("宋体", Font.BOLD, 50);// 设置字体
		g.setFont(font1);
		g.drawString("楚  河", 170, 365);// 绘制楚河、漢界字体
		g.drawString("漢  界", 400, 365);

		Font font = new Font("宋体", Font.BOLD, 30);// 设置字体
		g.setFont(font);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				// 绘制棋子
				if (qizi[i][j] != null) {
					if (this.qizi[i][j].getFocus() != false) {// 如果被选中
						g.setColor(XiangQi.focusbg);// 选中后的背景色
						g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);// 绘制该棋子
						g.setColor(XiangQi.focuschar);// 字符的颜色
					} else {
						g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);// 绘制该棋子
						g.setColor(qizi[i][j].getColor());// 设置画笔颜色
					}
					g.drawString(qizi[i][j].getName(), 110 + i * 60 - 15, 80 + j * 60 + 10);
					g.setColor(Color.black);// 设为黑色
				}
			}
		}
		g.setColor(c);// 还原画笔颜色
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.xq.caiPan == true) {// 判断是否轮到该玩家走棋
			int i = -1, j = -1;
			int[] pos = getPos(e);
			i = pos[0];
			j = pos[1];
			if (i >= 0 && i <=8 && j >= 0 && j <= 9) {// 如果在棋盘范围内
				if (focus == false) {// 如果此时没有棋子被选中
					this.noFocus(i,j);
				} else {// 如果以前选中过棋子
					if (qizi[i][j] != null) {// 如果该处有棋子
						if (qizi[i][j].getColor() == qizi[startI][startJ].getColor()) {
							// 如果是自己的棋子
							qizi[startI][startJ].setFocus(false);
							qizi[i][j].setFocus(true);// 更改选中对象
							startI = i;
							startJ = j;// 保存修改
						} else {// 如果是对方棋子
							endI = i;// 保存该点
							endJ = j;
							String name = qizi[startI][startJ].getName();// 获得该棋子的名字
							// 看是否可以移动
							boolean canMove = guiZe.canMove(startI, startJ, endI, endJ, name);
							if (canMove)
							// 如果可以移动
							{
								try {
									// 将移动信息发给对方
									this.xq.cat.dout.writeUTF(
											"<#MOVE#>" + this.xq.cat.tiaozhanzhe + startI +startJ+ endI + endJ);
									this.xq.caiPan = false;
									if (qizi[endI][endJ].getName().equals("帥")
											|| qizi[endI][endJ].getName().equals("将")) {
										// 如果终点处是对方的将
										this.success();
									} else {
										// 如果终点处不是对方的将
										this.noJiang();
									}
								} catch (Exception ee) {
									ee.printStackTrace();
								}
							}
						}
					} else {
						// 如果没有棋子
						endI = i;
						endJ = j;// 保存终点
						String name = qizi[startI][startJ].getName();// 获得该棋子的名字
						boolean canMove = guiZe.canMove(startI, startJ, endI, endJ, name);// 判断是否可走
						if (canMove) {
							// 如果可以移动
							this.noQiZi();
						}
					}
				}
			}
			this.xq.repaint();// 重绘
		}
	}

	public int[] getPos(MouseEvent e) {
		int[] pos = new int[2];
		pos[0] = -1;
		pos[1] = -1;
		Point p = e.getPoint();// 获得事件发生的坐标点
		double x = p.getX();
		double y = p.getY();
		if (Math.abs((x - 110) / 1 % 60) <= 25) {// 获得对应于数组x下标的位置
			pos[0] = Math.round((float) (x - 110)) / 60;// 舍为最接近的整数
		} else if (Math.abs((x - 110) / 1 % 60) >= 35) {
			pos[0] = Math.round((float) (x - 110)) / 60 + 1;
		}
		if (Math.abs((y - 80) / 1 % 60) <= 25) {// 获得对应于数组y下标的位置
			pos[1] = Math.round((float) (y - 80)) / 60;// 舍为最接近的整数
		} else if (Math.abs((y - 80) / 1 % 60) >= 35) {
			pos[1] = Math.round((float) (y - 80)) / 60 + 1;
		}
		return pos;
	}

	private void noFocus(int i, int j) {
		if (this.qizi[i][j] != null) {// 如果该位置有棋子
			if (this.xq.color == 0) {// 如果是红方
				if (this.qizi[i][j].getColor().equals(XiangQi.color1)) {// 如果棋子是红色

					this.qizi[i][j].setFocus(true);// 将棋子设为选中状态
					focus = true;// 将focus设为true
					startI = i;
					startJ = j;// 保存该坐标点
				}
			} else {// 如果是白方
				if (this.qizi[i][j].getColor().equals(XiangQi.color2)) {// 如果棋子是白色

					this.qizi[i][j].setFocus(true);// 将棋子设为选中状态
					focus = true;// 将focus设为true
					startI = i;
					startJ = j;// 保存该坐标点
				}
			}
		}
	}

	public void success() {
		qizi[endI][endJ] = qizi[startI][startJ];// 吃掉该棋子
		qizi[startI][startJ] = null;// 将原来位置设为空
		this.xq.repaint();// 重绘
		// 给出获胜信息
		JOptionPane.showMessageDialog(this.xq, "恭喜您，您获胜了！", "提示", JOptionPane.INFORMATION_MESSAGE);
		this.xq.cat.tiaozhanzhe = null;
		this.xq.color = 0;
		this.xq.caiPan = false;
		this.xq.next();// 还原棋盘，进入下一盘
		this.xq.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
		this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.xq.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
		this.xq.jbConnect.setEnabled(false);// 将连接按钮设为不可用
		this.xq.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
		this.xq.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
		this.xq.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
		this.xq.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
		this.xq.jbFail.setEnabled(false);// 将认输按钮设为不可用
		startI = -1;// 还原保存点
		startJ = -1;
		endI = -1;
		endJ = -1;
		jiang1_i = 4;// 帥的i坐标
		jiang1_j = 0;// 帥的j坐标
		jiang2_i = 4;// 将的i坐标
		jiang2_j = 9;// 将的j坐标
		focus = false;

	}

	public void noJiang() {
		qizi[endI][endJ] = qizi[startI][startJ];// 吃掉该棋子
		qizi[startI][startJ] = null;// 将原来位置设为空
		qizi[endI][endJ].setFocus(false);// 将该棋子设为非选中状态
		this.xq.repaint();// 重绘
		if (qizi[endI][endJ].getName().equals("帥")) {// 如果移动的是帥
			jiang1_i = endI;// 更新帥的坐标
			jiang1_j = endJ;
		} else if (qizi[endI][endJ].getName().equals("将")) {// 如果移动的是将
			jiang2_i = endI;// 更新将的坐标
			jiang2_j = endJ;
		}
		if (jiang1_i == jiang2_i) {// 如果将帥在同一条竖线上
			int count = 0;
			for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
				// 遍历这条竖线
				if (qizi[jiang1_i][jiang_j] != null) {
					count++;
					break;
				}
			}
			if (count == 0) {// 此时照将
				JOptionPane.showMessageDialog(this.xq, "照将！你失败了！", "提示", JOptionPane.INFORMATION_MESSAGE);
				this.xq.cat.tiaozhanzhe = null;
				this.xq.color = 0;
				this.xq.caiPan = false;
				this.xq.next();// 还原棋盘，进入下一盘
				this.xq.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
				this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
				this.xq.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
				this.xq.jbConnect.setEnabled(false);// 将连接按钮设为不可用
				this.xq.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
				this.xq.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
				this.xq.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
				this.xq.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
				this.xq.jbFail.setEnabled(false);// 将认输按钮设为不可用

				jiang1_i = 4;// 帥的i坐标
				jiang1_j = 0;// 帥的j坐标
				jiang2_i = 4;// 将的i坐标
				jiang2_j = 9;// 将的j坐标

			}
		}
		startI = -1;// 还原保存点
		startJ = -1;
		endI = -1;
		endJ = -1;
		focus = false;

	}

	public void noQiZi() {
		try {// 将该移动信息发送给对方
			this.xq.cat.dout.writeUTF("<#MOVE#>" + this.xq.cat.tiaozhanzhe + startI + startJ + endI + endJ);
			this.xq.caiPan = false;
			qizi[endI][endJ] = qizi[startI][startJ];// 吃掉该棋子
			qizi[startI][startJ] = null;// 将原来位置设为空
			qizi[endI][endJ].setFocus(false);// 将该棋子设为非选中状态
			this.xq.repaint();// 重绘
			if (qizi[endI][endJ].getName().equals("帥")) {// 如果移动的是帥
				jiang1_i = endI;// 更新帥的坐标
				jiang1_j = endJ;
			} else if (qizi[endI][endJ].getName().equals("将")) {// 如果移动的是将
				jiang2_i = endI;// 更新将的坐标
				jiang2_j = endJ;
			}
			if (jiang1_i == jiang2_i) {// 如果将帥在同一条竖线上
				int count = 0;
				for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
					// 遍历这条竖线
					if (qizi[jiang1_i][jiang_j] != null) {
						count++;
						break;
					}
				}
				if (count == 0) {// 此时照将
					JOptionPane.showMessageDialog(this.xq, "照将！你失败了！", "提示", JOptionPane.INFORMATION_MESSAGE);
					this.xq.cat.tiaozhanzhe = null;
					this.xq.color = 0;
					this.xq.caiPan = false;
					this.xq.next();// 还原棋盘，进入下一盘
					this.xq.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
					this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
					this.xq.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
					this.xq.jbConnect.setEnabled(false);// 将连接按钮设为不可用
					this.xq.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
					this.xq.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
					this.xq.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
					this.xq.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
					this.xq.jbFail.setEnabled(false);// 将认输按钮设为不可用

					jiang1_i = 4;// 帥的i坐标
					jiang1_j = 0;// 帥的j坐标
					jiang2_i = 4;// 将的i坐标
					jiang2_j = 9;// 将的j坐标

				}
			}
			startI = -1;// 还原保存点
			startJ = -1;
			endI = -1;
			endJ = -1;
			focus = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void move(int startI, int startJ, int endI, int endJ) {
		if (qizi[endI][endJ] != null
				&& (qizi[endI][endJ].getName().equals("帥") || qizi[endI][endJ].getName().equals("将"))) {
			qizi[endI][endJ] = qizi[startI][startJ];
			qizi[startI][startJ] = null;// 走棋
			this.xq.repaint();// 重绘
			// 给出失败信息
			JOptionPane.showMessageDialog(this.xq, "很遗憾，您失败了！", "提示", JOptionPane.INFORMATION_MESSAGE);
			this.xq.cat.tiaozhanzhe = null;
			this.xq.color = 0;
			this.xq.caiPan = false;
			this.xq.next();// 还原棋盘，进入下一盘
			this.xq.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.xq.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			this.xq.jbConnect.setEnabled(false);// 将连接按钮设为不可用
			this.xq.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
			this.xq.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
			this.xq.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
			this.xq.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
			this.xq.jbFail.setEnabled(false);// 将认输按钮设为不可用

			jiang1_i = 4;// 帥的i坐标
			jiang1_j = 0;// 帥的j坐标
			jiang2_i = 4;// 将的i坐标
			jiang2_j = 9;// 将的j坐标

		} else {// 如果不是将
			qizi[endI][endJ] = qizi[startI][startJ];// 吃掉该棋子
			qizi[startI][startJ] = null;// 将原来位置设为空
			this.xq.repaint();// 重绘
			if (qizi[endI][endJ].getName().equals("帥")) {// 如果移动的是帥
				jiang1_i = endI;// 更新帥的坐标
				jiang1_j = endJ;
			} else if (qizi[endI][endJ].getName().equals("将")) {// 如果移动的是将
				jiang2_i = endI;// 更新将的坐标
				jiang2_j = endJ;
			}
			if (jiang1_i == jiang2_i) {// 如果两将在一条竖线上
				int count = 0;
				for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
					// 遍历这条竖线
					if (qizi[jiang1_i][jiang_j] != null) {
						count++;
						break;
					}
				}
				if (count == 0) {// 此时照将
					JOptionPane.showMessageDialog(this.xq, "对方照将！你胜利了！", "提示", JOptionPane.INFORMATION_MESSAGE);
					this.xq.cat.tiaozhanzhe = null;
					this.xq.color = 0;
					this.xq.caiPan = false;
					this.xq.next();// 还原棋盘，进入下一盘
					this.xq.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
					this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
					this.xq.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
					this.xq.jbConnect.setEnabled(false);// 将连接按钮设为不可用
					this.xq.jbDisconnect.setEnabled(true);// 将断开按钮设为可用
					this.xq.jbChallenge.setEnabled(true);// 将挑战按钮设为可用
					this.xq.jbYChallenge.setEnabled(false);// 将接受挑战按钮设为不可用
					this.xq.jbNChallenge.setEnabled(false);// 将拒绝挑战按钮设为不可用
					this.xq.jbFail.setEnabled(false);// 将认输按钮设为不可用

					jiang1_i = 4;// 帥的i坐标
					jiang1_j = 0;// 帥的j坐标
					jiang2_i = 4;// 将的i坐标
					jiang2_j = 9;// 将的j坐标

				}
			}
		}
		this.xq.repaint();// 重绘
	}

	public void smallLine(Graphics2D g, int i, int j) {
		// 计算坐标
		int x = 110 + 60 * i;
		int y = 80 + 60 * j;

		if (i > 0) {// 绘制左上方的标志
			g.drawLine(x - 3, y - 3, x - 20, y - 3);
			g.drawLine(x - 3, y - 3, x - 3, y - 20);
		}
		if (i < 8) {// 绘制右上方的标志
			g.drawLine(x + 3, y - 3, x + 20, y - 3);
			g.drawLine(x + 3, y - 3, x + 3, y - 20);
		}
		if (i > 0) {// 绘制左下方的标志
			g.drawLine(x - 3, y + 3, x - 20, y + 3);
			g.drawLine(x - 3, y + 3, x - 3, y + 20);
		}
		if (i < 8) {// 绘制右下方的标志
			g.drawLine(x + 3, y + 3, x + 20, y + 3);
			g.drawLine(x + 3, y + 3, x + 3, y + 20);
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
