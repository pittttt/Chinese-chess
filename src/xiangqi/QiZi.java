package xiangqi;

import java.awt.Color;

public class QiZi {
	private Color color;// 棋子颜色
	private String name;// 棋子的名字，即是什么棋
	private int x;// 所在的x方向位置
	private int y;// 所在的y方向位置
	private boolean focus = false;// 是否被选中

	public QiZi() {

	}

	public QiZi(Color color, String name, int x, int y) {
		// 构造器
		this.color = color;
		this.name = name;
		this.x = x;
		this.y = y;
		this.focus = false;
	}

	public Color getColor() {
		// 获得棋子颜色的方法
		return this.color;
	}

	public void setColor(Color color) {
		// 设置棋子颜色的方法
		this.color = color;
	}

	public String getName() {
		// 获得棋子名字的方法
		return this.name;
	}

	public void setName(String name) {
		// 设置棋子名字的方法
		this.name = name;
	}

	public int getX() {
		// 获得棋子x方向位置的方法
		return this.x;
	}

	public void setX(int x) {
		// 设置棋子x方向位置的方法
		this.x = x;
	}

	public int getY() {
		// 获得棋子y方向位置的方法
		return this.y;
	}

	public void setY(int y) {
		// 设置棋子y方向位置的方法
		this.y = y;
	}

	public boolean getFocus() {
		// 判断棋子是否被选中的方法
		return focus;
	}

	public void setFocus(boolean focus) {
		// 设置棋子选中状态的方法
		this.focus = focus;
	}

}
