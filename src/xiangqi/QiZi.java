package xiangqi;

import java.awt.Color;

public class QiZi {
	private Color color;// ������ɫ
	private String name;// ���ӵ����֣�����ʲô��
	private int x;// ���ڵ�x����λ��
	private int y;// ���ڵ�y����λ��
	private boolean focus = false;// �Ƿ�ѡ��

	public QiZi() {

	}

	public QiZi(Color color, String name, int x, int y) {
		// ������
		this.color = color;
		this.name = name;
		this.x = x;
		this.y = y;
		this.focus = false;
	}

	public Color getColor() {
		// ���������ɫ�ķ���
		return this.color;
	}

	public void setColor(Color color) {
		// ����������ɫ�ķ���
		this.color = color;
	}

	public String getName() {
		// ����������ֵķ���
		return this.name;
	}

	public void setName(String name) {
		// �����������ֵķ���
		this.name = name;
	}

	public int getX() {
		// �������x����λ�õķ���
		return this.x;
	}

	public void setX(int x) {
		// ��������x����λ�õķ���
		this.x = x;
	}

	public int getY() {
		// �������y����λ�õķ���
		return this.y;
	}

	public void setY(int y) {
		// ��������y����λ�õķ���
		this.y = y;
	}

	public boolean getFocus() {
		// �ж������Ƿ�ѡ�еķ���
		return focus;
	}

	public void setFocus(boolean focus) {
		// ��������ѡ��״̬�ķ���
		this.focus = focus;
	}

}
