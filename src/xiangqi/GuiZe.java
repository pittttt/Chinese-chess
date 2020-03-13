package xiangqi;

public class GuiZe {
	QiZi[][] qizi;// 声明棋子的数组
	boolean canMove = false;
	int i;
	int j;

	public GuiZe(QiZi[][] qizi) {
		this.qizi = qizi;
	}

	public boolean canMove(int startI, int startJ, int endI, int endJ, String name) {
		int maxI;// 定义辅助变量
		int minI;
		int maxJ;
		int minJ;
		canMove = true;
		if (startI >= endI) {
			// 确定起始坐标的大小关系
			maxI = startI;
			minI = endI;
		} else {
			// 确定maxI和minI
			maxI = endI;
			minI = startI;
		}
		if (startJ >= endJ) {
			// 确定起始坐标的大小关系
			maxJ = startJ;
			minJ = endJ;

		} else {
			// 确定maxI和minI
			maxJ = endJ;
			minJ = startJ;
		}

		if (name.equals("車") || name.equals("车")) {
			this.ju(maxI, minI, maxJ, minJ);
		} else if (name.equals("馬") || name.equals("马")) {
			this.ma(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("相")) // 上方相
		{
			this.xiang1(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("象")) // 下方象
		{
			this.xiang2(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("士") || name.equals("仕")) {
			this.shi(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("帥") || name.equals("将")) {
			this.jiang(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("炮") || name.equals("砲")) {
			this.pao(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("卒")) // 上方卒
		{
			this.zu(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("兵")) // 下方兵
		{
			this.bing(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		}

		return canMove;
	}

	public void ju(int maxI, int minI, int maxJ, int minJ) {

		// 对車的处理方法
		if (maxI == minI) {
			// 如果在一条竖线上
			for (j = minJ + 1; j < maxJ; j++) {

				if (qizi[maxI][j] != null) {
					// 如果中间有棋子
					canMove = false;// 不可以走棋
					break;
				}
			}
		} else if (maxJ == minJ) {

			// 如果在一条横线上
			for (i = minI + 1; i < maxI; i++) {

				if (qizi[i][maxJ] != null) {

					// 如果中间有棋子
					canMove = false;// 不可以走棋
					break;
				}
			}
		} else if (maxI != minI && maxJ != minJ) {

			// 如果不在同一条线上
			canMove = false;// 不可以走棋
		}

	}

	public void ma(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// 对馬的处理
		int a = maxI - minI;
		int b = maxJ - minJ;// 获得两坐标点之间的差
		if (a == 1 && b == 2) {
			// 如果是竖着的日字
			if (startJ > endJ) {
				// 如果是从下向上走
				if (qizi[startI][startJ - 1] != null) {// 马腿处有棋子
					canMove = false;// 不能走棋
				}
			} else {
				// 如果是从上往下走
				if (qizi[startI][startJ + 1] != null) {// 马腿处有棋子
					canMove = false;// 不能走棋
				}
			}

		} else if (a == 2 && b == 1) {
			// 如果是横着的日
			if (startI > endI) {// 如果是从右往左走
				if (qizi[startI - 1][startJ] != null) {// 如果马腿处有棋子
					canMove = false;// 不能走棋
				}
			} else {
				// 如果是从左往右走
				if (qizi[startI + 1][startJ] != null) {// 如果马腿处有棋子
					canMove = false;// 不能走棋
				}
			}

		} else if (!(a == 2 && b == 1) || (a == 1 && b == 2)) {// 如果不是日字
			canMove = false;// 不能走棋
		}

	}

	public void xiang1(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// 对相的处理（上方）
		int a = maxI - minI;
		int b = maxJ - minJ;// 获得x，y坐标的差值
		if (a == 2 && b == 2) {
			// 如果是田字
			if (endJ > 4) {
				// 如果过河了
				canMove = false;// 不能走棋

			}
			if (qizi[(maxI + minI) / 2][(maxJ + minJ) / 2] != null) {
				// 如果田字中间有棋子
				canMove = false;// 不能走棋
			}

		} else {
			canMove = false;// 如果不是田字，不能走棋
		}

	}

	public void xiang2(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// 对象的处理（下方）
		int a = maxI - minI;
		int b = maxJ - minJ;// 获得x，y坐标的差值
		if (a == 2 && b == 2) {
			// 如果是田字
			if (endJ < 5) {
				// 如果过河了
				canMove = false;// 不能走棋

			}
			if (qizi[(maxI + minI) / 2][(maxJ + minJ) / 2] != null) {
				// 如果田字中间有棋子
				canMove = false;// 不能走棋
			}

		} else {
			canMove = false;// 如果不是田字，不能走棋
		}
	}

	public void shi(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// 对士、仕的处理
		int a = maxI - minI;
		int b = maxJ - minJ;// 获得x，y坐标的差值

		if (a == 1 && b == 1) {// 如果是小斜线
			if (startJ > 4) {// 如果是下方的士
				if (endJ < 7) {
					canMove = false;// 下方的士越界，不可以走
				}
			} else {// 如果是上方的仕
				if (endJ > 2) {
					canMove = false;// 上方的仕越界，不可以走
				}
			}
			if (endI > 5 || endI < 3) {// 如果左右越界，不可以走
				canMove = false;
			}
		} else {
			// 如果不是斜线
			canMove = false;// 不可以走
		}
	}

	public void jiang(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// 对将、帥的处理
		int a = maxI - minI;
		int b = maxJ - minJ;// 获得x，y坐标的差值
		if ((a == 1 && b == 0) || (a == 0 && b == 1)) {
			// 如果走的是一小格
			if (startJ > 4) {// 如果是下方的将
				if (endJ < 7) {// 如果越界
					canMove = false;// 不可以走
				}
			} else {
				// 如果是上方的帥
				if (endJ > 2) {// 如果越界
					canMove = false;// 不可以走
				}
			}
			if (endI > 5 || endI < 3) {// 如果左右越界，不可以走
				canMove = false;
			}
		} else {
			// 如果走的不是一格，不可以走
			canMove = false;
		}
	}

	public void pao(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// 对炮、砲的处理
		if (maxI == minI) {// 如果走的是竖线
			if (qizi[endI][endJ] != null) {// 如果终点有棋子
				int count = 0;
				for (j = minJ + 1; j < maxJ; j++) {
					if (qizi[minI][j] != null) {// 判断起点与终点之间有几个棋子
						count++;
					}
				}
				if (count != 1) {// 如果不是一个棋子,不可以走棋
					canMove = false;
				}
			} else if (qizi[endI][endJ] == null) {// 如果终点没有棋子
				for (j = minJ + 1; j < maxJ; j++) {
					if (qizi[minI][j] != null) {// 如果起止点之间有棋子
						canMove = false;// 不能走棋
						break;
					}
				}
			}
		} else if (maxJ == minJ) {// 如果走的是横线
			if (qizi[endI][endJ] != null) {// 如果终点有棋子
				int count = 0;
				for (i = minI + 1; i < maxI; i++) {
					if (qizi[i][minJ] != null) {// 判断起点与终点之间有几个棋子
						count++;
					}
				}
				if (count != 1) {// 如果不是一个棋子，不可以走棋
					canMove = false;
				}
			} else if (qizi[endI][endJ] == null) {// 如果终点没有棋子
				for (i = minI + 1; i < maxI; i++) {
					if (qizi[i][minJ] != null) {// 如果起止点之间有棋子
						canMove = false;// 不能走棋
						break;
					}
				}
			}
		} else if (maxJ != minJ && maxI != minI) {
			// 如果走的不是横线和竖线
			canMove = false;// 不可以走棋
		}
	}

	public void zu(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

		// 对卒的处理（上方）
		if (startJ < 5) {// 如果还没有过河
			if (startI != endI) {// 竖着走
				canMove = false;// 不可以走棋
			}
			if (endJ - startJ != 1) {// 走的不是一格，并且只能向前走
				canMove = false;// 不可以走棋
			}
		} else {
			// 如果已经过河
			if (startI == endI) {// 走竖线
				if (endJ - startJ != 1) {// 走的不是一格，并且只能向前走
					canMove = false;// 不可以走棋
				}
			} else if (startJ == endJ) {
				// 如果走的是横线
				if (maxI - minI != 1) {// 走的不是一格，并且只能向前走
					canMove = false;// 不可以走棋
				}
			} else if (startI != endI && startJ != endJ) {
				// 如果走的不是横线也不是竖线,不能走棋
				canMove = false;
			}
		}
	}

	public void bing(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

		// 对兵的处理（下方）
		if (startJ > 4) {// 如果还没有过河
			if (startI != endI) {// 竖着走
				canMove = false;// 不可以走棋
			}
			if (endJ - startJ != -1) {// 走的不是一格，并且只能向前走
				canMove = false;// 不可以走棋
			}
		} else {
			// 如果已经过河
			if (startI == endI) {// 走竖线
				if (endJ - startJ != -1) {// 走的不是一格，并且只能向前走
					canMove = false;// 不可以走棋
				}
			} else if (startJ == endJ) {
				// 如果走的是横线
				if (maxI - minI != 1) {// 走的不是一格，并且只能向前走
					canMove = false;// 不可以走棋
				}
			} else if (startI != endI && startJ != endJ) {
				// 如果走的不是横线也不是竖线,不能走棋
				canMove = false;
			}
		}
	}
}
