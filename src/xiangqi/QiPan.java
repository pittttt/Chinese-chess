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

	private int width;// ��������֮��ľ���
	boolean focus = false;// ���ӵ�״̬
	int jiang1_i = 4;// ����i����
	int jiang1_j = 0;// ����j����
	int jiang2_i = 4;// ����i����
	int jiang2_j = 9;// ����j����

	int startI = -1;// ���ӵĿ�ʼλ��
	int startJ = -1;

	int endI = -1;// ���ӵ���ֹλ��
	int endJ = -1;

	public QiZi qizi[][];// ���ӵ�����
	XiangQi xq = null;// ����XiangQi������
	GuiZe guiZe;// ����GuiZe������

	public QiPan(QiZi qizi[][], int width, XiangQi xq) {
		this.xq = xq;
		this.qizi = qizi;
		this.width = width;
		guiZe = new GuiZe(qizi);
		this.addMouseListener(this);// Ϊ�����������¼�������
		this.setBounds(0, 0, 700, 700);// �������̴�С
		this.setLayout(null);// ��Ϊ�ղ���
	}

	public void paint(Graphics g1) {

		Graphics2D g = (Graphics2D) g1;// ���Graphics2D����
		// �򿪿����
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color c = g.getColor();// ��û�����ɫ
		g.setColor(XiangQi.bgColor);// ��������Ϊ����ɫ
		g.fill3DRect(60, 30, 580, 630, false);// ����һ����������
		g.setColor(Color.black);// ��������ɫ��Ϊ��ɫ
		for (int i = 80; i <= 620; i = i + 60) {
			// ���������еĺ���
			g.drawLine(110, i, 590, i);
		}
		g.drawLine(110, 80, 110, 620);// ���������
		g.drawLine(590, 80, 590, 620);// �����ұ���

		for (int i = 170; i <= 530; i = i + 60) {
			// �����м������
			g.drawLine(i, 80, i, 320);
			g.drawLine(i, 380, i, 620);
		}
		g.drawLine(290, 80, 410, 200);// ��������б��
		g.drawLine(290, 200, 410, 80);
		g.drawLine(290, 500, 410, 620);
		g.drawLine(290, 620, 410, 500);

		this.smallLine(g, 1, 2);// ���Ƴh����λ�ñ�־
		this.smallLine(g, 7, 2);// ���Ƴh����λ�ñ�־

		this.smallLine(g, 0, 3);// ����������λ�ñ�־
		this.smallLine(g, 2, 3);// ����������λ�ñ�־
		this.smallLine(g, 4, 3);// ����������λ�ñ�־
		this.smallLine(g, 6, 3);// ����������λ�ñ�־
		this.smallLine(g, 8, 3);// ����������λ�ñ�־

		this.smallLine(g, 0, 6);// ���Ʊ�����λ�ñ�־
		this.smallLine(g, 2, 6);// ���Ʊ�����λ�ñ�־
		this.smallLine(g, 4, 6);// ���Ʊ�����λ�ñ�־
		this.smallLine(g, 6, 6);// ���Ʊ�����λ�ñ�־
		this.smallLine(g, 8, 6);// ���Ʊ�����λ�ñ�־

		this.smallLine(g, 1, 7);// ����������λ�ñ�־
		this.smallLine(g, 7, 7);// ����������λ�ñ�־

		g.setColor(Color.black);
		Font font1 = new Font("����", Font.BOLD, 50);// ��������
		g.setFont(font1);
		g.drawString("��  ��", 170, 365);// ���Ƴ��ӡ��h������
		g.drawString("�h  ��", 400, 365);

		Font font = new Font("����", Font.BOLD, 30);// ��������
		g.setFont(font);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				// ��������
				if (qizi[i][j] != null) {
					if (this.qizi[i][j].getFocus() != false) {// �����ѡ��
						g.setColor(XiangQi.focusbg);// ѡ�к�ı���ɫ
						g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);// ���Ƹ�����
						g.setColor(XiangQi.focuschar);// �ַ�����ɫ
					} else {
						g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);// ���Ƹ�����
						g.setColor(qizi[i][j].getColor());// ���û�����ɫ
					}
					g.drawString(qizi[i][j].getName(), 110 + i * 60 - 15, 80 + j * 60 + 10);
					g.setColor(Color.black);// ��Ϊ��ɫ
				}
			}
		}
		g.setColor(c);// ��ԭ������ɫ
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.xq.caiPan == true) {// �ж��Ƿ��ֵ����������
			int i = -1, j = -1;
			int[] pos = getPos(e);
			i = pos[0];
			j = pos[1];
			if (i >= 0 && i <=8 && j >= 0 && j <= 9) {// ��������̷�Χ��
				if (focus == false) {// �����ʱû�����ӱ�ѡ��
					this.noFocus(i,j);
				} else {// �����ǰѡ�й�����
					if (qizi[i][j] != null) {// ����ô�������
						if (qizi[i][j].getColor() == qizi[startI][startJ].getColor()) {
							// ������Լ�������
							qizi[startI][startJ].setFocus(false);
							qizi[i][j].setFocus(true);// ����ѡ�ж���
							startI = i;
							startJ = j;// �����޸�
						} else {// ����ǶԷ�����
							endI = i;// ����õ�
							endJ = j;
							String name = qizi[startI][startJ].getName();// ��ø����ӵ�����
							// ���Ƿ�����ƶ�
							boolean canMove = guiZe.canMove(startI, startJ, endI, endJ, name);
							if (canMove)
							// ��������ƶ�
							{
								try {
									// ���ƶ���Ϣ�����Է�
									this.xq.cat.dout.writeUTF(
											"<#MOVE#>" + this.xq.cat.tiaozhanzhe + startI +startJ+ endI + endJ);
									this.xq.caiPan = false;
									if (qizi[endI][endJ].getName().equals("��")
											|| qizi[endI][endJ].getName().equals("��")) {
										// ����յ㴦�ǶԷ��Ľ�
										this.success();
									} else {
										// ����յ㴦���ǶԷ��Ľ�
										this.noJiang();
									}
								} catch (Exception ee) {
									ee.printStackTrace();
								}
							}
						}
					} else {
						// ���û������
						endI = i;
						endJ = j;// �����յ�
						String name = qizi[startI][startJ].getName();// ��ø����ӵ�����
						boolean canMove = guiZe.canMove(startI, startJ, endI, endJ, name);// �ж��Ƿ����
						if (canMove) {
							// ��������ƶ�
							this.noQiZi();
						}
					}
				}
			}
			this.xq.repaint();// �ػ�
		}
	}

	public int[] getPos(MouseEvent e) {
		int[] pos = new int[2];
		pos[0] = -1;
		pos[1] = -1;
		Point p = e.getPoint();// ����¼������������
		double x = p.getX();
		double y = p.getY();
		if (Math.abs((x - 110) / 1 % 60) <= 25) {// ��ö�Ӧ������x�±��λ��
			pos[0] = Math.round((float) (x - 110)) / 60;// ��Ϊ��ӽ�������
		} else if (Math.abs((x - 110) / 1 % 60) >= 35) {
			pos[0] = Math.round((float) (x - 110)) / 60 + 1;
		}
		if (Math.abs((y - 80) / 1 % 60) <= 25) {// ��ö�Ӧ������y�±��λ��
			pos[1] = Math.round((float) (y - 80)) / 60;// ��Ϊ��ӽ�������
		} else if (Math.abs((y - 80) / 1 % 60) >= 35) {
			pos[1] = Math.round((float) (y - 80)) / 60 + 1;
		}
		return pos;
	}

	private void noFocus(int i, int j) {
		if (this.qizi[i][j] != null) {// �����λ��������
			if (this.xq.color == 0) {// ����Ǻ췽
				if (this.qizi[i][j].getColor().equals(XiangQi.color1)) {// ��������Ǻ�ɫ

					this.qizi[i][j].setFocus(true);// ��������Ϊѡ��״̬
					focus = true;// ��focus��Ϊtrue
					startI = i;
					startJ = j;// ����������
				}
			} else {// ����ǰ׷�
				if (this.qizi[i][j].getColor().equals(XiangQi.color2)) {// ��������ǰ�ɫ

					this.qizi[i][j].setFocus(true);// ��������Ϊѡ��״̬
					focus = true;// ��focus��Ϊtrue
					startI = i;
					startJ = j;// ����������
				}
			}
		}
	}

	public void success() {
		qizi[endI][endJ] = qizi[startI][startJ];// �Ե�������
		qizi[startI][startJ] = null;// ��ԭ��λ����Ϊ��
		this.xq.repaint();// �ػ�
		// ������ʤ��Ϣ
		JOptionPane.showMessageDialog(this.xq, "��ϲ��������ʤ�ˣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		this.xq.cat.tiaozhanzhe = null;
		this.xq.color = 0;
		this.xq.caiPan = false;
		this.xq.next();// ��ԭ���̣�������һ��
		this.xq.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
		this.xq.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
		this.xq.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
		this.xq.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
		this.xq.jbDisconnect.setEnabled(true);// ���Ͽ���ť��Ϊ����
		this.xq.jbChallenge.setEnabled(true);// ����ս��ť��Ϊ����
		this.xq.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
		this.xq.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
		this.xq.jbFail.setEnabled(false);// �����䰴ť��Ϊ������
		startI = -1;// ��ԭ�����
		startJ = -1;
		endI = -1;
		endJ = -1;
		jiang1_i = 4;// ����i����
		jiang1_j = 0;// ����j����
		jiang2_i = 4;// ����i����
		jiang2_j = 9;// ����j����
		focus = false;

	}

	public void noJiang() {
		qizi[endI][endJ] = qizi[startI][startJ];// �Ե�������
		qizi[startI][startJ] = null;// ��ԭ��λ����Ϊ��
		qizi[endI][endJ].setFocus(false);// ����������Ϊ��ѡ��״̬
		this.xq.repaint();// �ػ�
		if (qizi[endI][endJ].getName().equals("��")) {// ����ƶ����ǎ�
			jiang1_i = endI;// ����������
			jiang1_j = endJ;
		} else if (qizi[endI][endJ].getName().equals("��")) {// ����ƶ����ǽ�
			jiang2_i = endI;// ���½�������
			jiang2_j = endJ;
		}
		if (jiang1_i == jiang2_i) {// ���������ͬһ��������
			int count = 0;
			for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
				// ������������
				if (qizi[jiang1_i][jiang_j] != null) {
					count++;
					break;
				}
			}
			if (count == 0) {// ��ʱ�ս�
				JOptionPane.showMessageDialog(this.xq, "�ս�����ʧ���ˣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				this.xq.cat.tiaozhanzhe = null;
				this.xq.color = 0;
				this.xq.caiPan = false;
				this.xq.next();// ��ԭ���̣�������һ��
				this.xq.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
				this.xq.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
				this.xq.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
				this.xq.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
				this.xq.jbDisconnect.setEnabled(true);// ���Ͽ���ť��Ϊ����
				this.xq.jbChallenge.setEnabled(true);// ����ս��ť��Ϊ����
				this.xq.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
				this.xq.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
				this.xq.jbFail.setEnabled(false);// �����䰴ť��Ϊ������

				jiang1_i = 4;// ����i����
				jiang1_j = 0;// ����j����
				jiang2_i = 4;// ����i����
				jiang2_j = 9;// ����j����

			}
		}
		startI = -1;// ��ԭ�����
		startJ = -1;
		endI = -1;
		endJ = -1;
		focus = false;

	}

	public void noQiZi() {
		try {// �����ƶ���Ϣ���͸��Է�
			this.xq.cat.dout.writeUTF("<#MOVE#>" + this.xq.cat.tiaozhanzhe + startI + startJ + endI + endJ);
			this.xq.caiPan = false;
			qizi[endI][endJ] = qizi[startI][startJ];// �Ե�������
			qizi[startI][startJ] = null;// ��ԭ��λ����Ϊ��
			qizi[endI][endJ].setFocus(false);// ����������Ϊ��ѡ��״̬
			this.xq.repaint();// �ػ�
			if (qizi[endI][endJ].getName().equals("��")) {// ����ƶ����ǎ�
				jiang1_i = endI;// ����������
				jiang1_j = endJ;
			} else if (qizi[endI][endJ].getName().equals("��")) {// ����ƶ����ǽ�
				jiang2_i = endI;// ���½�������
				jiang2_j = endJ;
			}
			if (jiang1_i == jiang2_i) {// ���������ͬһ��������
				int count = 0;
				for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
					// ������������
					if (qizi[jiang1_i][jiang_j] != null) {
						count++;
						break;
					}
				}
				if (count == 0) {// ��ʱ�ս�
					JOptionPane.showMessageDialog(this.xq, "�ս�����ʧ���ˣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					this.xq.cat.tiaozhanzhe = null;
					this.xq.color = 0;
					this.xq.caiPan = false;
					this.xq.next();// ��ԭ���̣�������һ��
					this.xq.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
					this.xq.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
					this.xq.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
					this.xq.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
					this.xq.jbDisconnect.setEnabled(true);// ���Ͽ���ť��Ϊ����
					this.xq.jbChallenge.setEnabled(true);// ����ս��ť��Ϊ����
					this.xq.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
					this.xq.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
					this.xq.jbFail.setEnabled(false);// �����䰴ť��Ϊ������

					jiang1_i = 4;// ����i����
					jiang1_j = 0;// ����j����
					jiang2_i = 4;// ����i����
					jiang2_j = 9;// ����j����

				}
			}
			startI = -1;// ��ԭ�����
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
				&& (qizi[endI][endJ].getName().equals("��") || qizi[endI][endJ].getName().equals("��"))) {
			qizi[endI][endJ] = qizi[startI][startJ];
			qizi[startI][startJ] = null;// ����
			this.xq.repaint();// �ػ�
			// ����ʧ����Ϣ
			JOptionPane.showMessageDialog(this.xq, "���ź�����ʧ���ˣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			this.xq.cat.tiaozhanzhe = null;
			this.xq.color = 0;
			this.xq.caiPan = false;
			this.xq.next();// ��ԭ���̣�������һ��
			this.xq.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
			this.xq.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
			this.xq.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
			this.xq.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
			this.xq.jbDisconnect.setEnabled(true);// ���Ͽ���ť��Ϊ����
			this.xq.jbChallenge.setEnabled(true);// ����ս��ť��Ϊ����
			this.xq.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
			this.xq.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
			this.xq.jbFail.setEnabled(false);// �����䰴ť��Ϊ������

			jiang1_i = 4;// ����i����
			jiang1_j = 0;// ����j����
			jiang2_i = 4;// ����i����
			jiang2_j = 9;// ����j����

		} else {// ������ǽ�
			qizi[endI][endJ] = qizi[startI][startJ];// �Ե�������
			qizi[startI][startJ] = null;// ��ԭ��λ����Ϊ��
			this.xq.repaint();// �ػ�
			if (qizi[endI][endJ].getName().equals("��")) {// ����ƶ����ǎ�
				jiang1_i = endI;// ����������
				jiang1_j = endJ;
			} else if (qizi[endI][endJ].getName().equals("��")) {// ����ƶ����ǽ�
				jiang2_i = endI;// ���½�������
				jiang2_j = endJ;
			}
			if (jiang1_i == jiang2_i) {// ���������һ��������
				int count = 0;
				for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
					// ������������
					if (qizi[jiang1_i][jiang_j] != null) {
						count++;
						break;
					}
				}
				if (count == 0) {// ��ʱ�ս�
					JOptionPane.showMessageDialog(this.xq, "�Է��ս�����ʤ���ˣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					this.xq.cat.tiaozhanzhe = null;
					this.xq.color = 0;
					this.xq.caiPan = false;
					this.xq.next();// ��ԭ���̣�������һ��
					this.xq.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
					this.xq.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
					this.xq.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
					this.xq.jbConnect.setEnabled(false);// �����Ӱ�ť��Ϊ������
					this.xq.jbDisconnect.setEnabled(true);// ���Ͽ���ť��Ϊ����
					this.xq.jbChallenge.setEnabled(true);// ����ս��ť��Ϊ����
					this.xq.jbYChallenge.setEnabled(false);// ��������ս��ť��Ϊ������
					this.xq.jbNChallenge.setEnabled(false);// ���ܾ���ս��ť��Ϊ������
					this.xq.jbFail.setEnabled(false);// �����䰴ť��Ϊ������

					jiang1_i = 4;// ����i����
					jiang1_j = 0;// ����j����
					jiang2_i = 4;// ����i����
					jiang2_j = 9;// ����j����

				}
			}
		}
		this.xq.repaint();// �ػ�
	}

	public void smallLine(Graphics2D g, int i, int j) {
		// ��������
		int x = 110 + 60 * i;
		int y = 80 + 60 * j;

		if (i > 0) {// �������Ϸ��ı�־
			g.drawLine(x - 3, y - 3, x - 20, y - 3);
			g.drawLine(x - 3, y - 3, x - 3, y - 20);
		}
		if (i < 8) {// �������Ϸ��ı�־
			g.drawLine(x + 3, y - 3, x + 20, y - 3);
			g.drawLine(x + 3, y - 3, x + 3, y - 20);
		}
		if (i > 0) {// �������·��ı�־
			g.drawLine(x - 3, y + 3, x - 20, y + 3);
			g.drawLine(x - 3, y + 3, x - 3, y + 20);
		}
		if (i < 8) {// �������·��ı�־
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
