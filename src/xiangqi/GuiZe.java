package xiangqi;

public class GuiZe {
	QiZi[][] qizi;// �������ӵ�����
	boolean canMove = false;
	int i;
	int j;

	public GuiZe(QiZi[][] qizi) {
		this.qizi = qizi;
	}

	public boolean canMove(int startI, int startJ, int endI, int endJ, String name) {
		int maxI;// ���帨������
		int minI;
		int maxJ;
		int minJ;
		canMove = true;
		if (startI >= endI) {
			// ȷ����ʼ����Ĵ�С��ϵ
			maxI = startI;
			minI = endI;
		} else {
			// ȷ��maxI��minI
			maxI = endI;
			minI = startI;
		}
		if (startJ >= endJ) {
			// ȷ����ʼ����Ĵ�С��ϵ
			maxJ = startJ;
			minJ = endJ;

		} else {
			// ȷ��maxI��minI
			maxJ = endJ;
			minJ = startJ;
		}

		if (name.equals("܇") || name.equals("��")) {
			this.ju(maxI, minI, maxJ, minJ);
		} else if (name.equals("�R") || name.equals("��")) {
			this.ma(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("��")) // �Ϸ���
		{
			this.xiang1(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("��")) // �·���
		{
			this.xiang2(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("ʿ") || name.equals("��")) {
			this.shi(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("��") || name.equals("��")) {
			this.jiang(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("��") || name.equals("�h")) {
			this.pao(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("��")) // �Ϸ���
		{
			this.zu(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		} else if (name.equals("��")) // �·���
		{
			this.bing(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
		}

		return canMove;
	}

	public void ju(int maxI, int minI, int maxJ, int minJ) {

		// ��܇�Ĵ�����
		if (maxI == minI) {
			// �����һ��������
			for (j = minJ + 1; j < maxJ; j++) {

				if (qizi[maxI][j] != null) {
					// ����м�������
					canMove = false;// ����������
					break;
				}
			}
		} else if (maxJ == minJ) {

			// �����һ��������
			for (i = minI + 1; i < maxI; i++) {

				if (qizi[i][maxJ] != null) {

					// ����м�������
					canMove = false;// ����������
					break;
				}
			}
		} else if (maxI != minI && maxJ != minJ) {

			// �������ͬһ������
			canMove = false;// ����������
		}

	}

	public void ma(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// ���R�Ĵ���
		int a = maxI - minI;
		int b = maxJ - minJ;// ����������֮��Ĳ�
		if (a == 1 && b == 2) {
			// ��������ŵ�����
			if (startJ > endJ) {
				// ����Ǵ���������
				if (qizi[startI][startJ - 1] != null) {// ���ȴ�������
					canMove = false;// ��������
				}
			} else {
				// ����Ǵ���������
				if (qizi[startI][startJ + 1] != null) {// ���ȴ�������
					canMove = false;// ��������
				}
			}

		} else if (a == 2 && b == 1) {
			// ����Ǻ��ŵ���
			if (startI > endI) {// ����Ǵ���������
				if (qizi[startI - 1][startJ] != null) {// ������ȴ�������
					canMove = false;// ��������
				}
			} else {
				// ����Ǵ���������
				if (qizi[startI + 1][startJ] != null) {// ������ȴ�������
					canMove = false;// ��������
				}
			}

		} else if (!(a == 2 && b == 1) || (a == 1 && b == 2)) {// �����������
			canMove = false;// ��������
		}

	}

	public void xiang1(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// ����Ĵ����Ϸ���
		int a = maxI - minI;
		int b = maxJ - minJ;// ���x��y����Ĳ�ֵ
		if (a == 2 && b == 2) {
			// ���������
			if (endJ > 4) {
				// ���������
				canMove = false;// ��������

			}
			if (qizi[(maxI + minI) / 2][(maxJ + minJ) / 2] != null) {
				// ��������м�������
				canMove = false;// ��������
			}

		} else {
			canMove = false;// ����������֣���������
		}

	}

	public void xiang2(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// ����Ĵ����·���
		int a = maxI - minI;
		int b = maxJ - minJ;// ���x��y����Ĳ�ֵ
		if (a == 2 && b == 2) {
			// ���������
			if (endJ < 5) {
				// ���������
				canMove = false;// ��������

			}
			if (qizi[(maxI + minI) / 2][(maxJ + minJ) / 2] != null) {
				// ��������м�������
				canMove = false;// ��������
			}

		} else {
			canMove = false;// ����������֣���������
		}
	}

	public void shi(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// ��ʿ���˵Ĵ���
		int a = maxI - minI;
		int b = maxJ - minJ;// ���x��y����Ĳ�ֵ

		if (a == 1 && b == 1) {// �����Сб��
			if (startJ > 4) {// ������·���ʿ
				if (endJ < 7) {
					canMove = false;// �·���ʿԽ�磬��������
				}
			} else {// ������Ϸ�����
				if (endJ > 2) {
					canMove = false;// �Ϸ�����Խ�磬��������
				}
			}
			if (endI > 5 || endI < 3) {// �������Խ�磬��������
				canMove = false;
			}
		} else {
			// �������б��
			canMove = false;// ��������
		}
	}

	public void jiang(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// �Խ������Ĵ���
		int a = maxI - minI;
		int b = maxJ - minJ;// ���x��y����Ĳ�ֵ
		if ((a == 1 && b == 0) || (a == 0 && b == 1)) {
			// ����ߵ���һС��
			if (startJ > 4) {// ������·��Ľ�
				if (endJ < 7) {// ���Խ��
					canMove = false;// ��������
				}
			} else {
				// ������Ϸ��Ď�
				if (endJ > 2) {// ���Խ��
					canMove = false;// ��������
				}
			}
			if (endI > 5 || endI < 3) {// �������Խ�磬��������
				canMove = false;
			}
		} else {
			// ����ߵĲ���һ�񣬲�������
			canMove = false;
		}
	}

	public void pao(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
		// ���ڡ��h�Ĵ���
		if (maxI == minI) {// ����ߵ�������
			if (qizi[endI][endJ] != null) {// ����յ�������
				int count = 0;
				for (j = minJ + 1; j < maxJ; j++) {
					if (qizi[minI][j] != null) {// �ж�������յ�֮���м�������
						count++;
					}
				}
				if (count != 1) {// �������һ������,����������
					canMove = false;
				}
			} else if (qizi[endI][endJ] == null) {// ����յ�û������
				for (j = minJ + 1; j < maxJ; j++) {
					if (qizi[minI][j] != null) {// �����ֹ��֮��������
						canMove = false;// ��������
						break;
					}
				}
			}
		} else if (maxJ == minJ) {// ����ߵ��Ǻ���
			if (qizi[endI][endJ] != null) {// ����յ�������
				int count = 0;
				for (i = minI + 1; i < maxI; i++) {
					if (qizi[i][minJ] != null) {// �ж�������յ�֮���м�������
						count++;
					}
				}
				if (count != 1) {// �������һ�����ӣ�����������
					canMove = false;
				}
			} else if (qizi[endI][endJ] == null) {// ����յ�û������
				for (i = minI + 1; i < maxI; i++) {
					if (qizi[i][minJ] != null) {// �����ֹ��֮��������
						canMove = false;// ��������
						break;
					}
				}
			}
		} else if (maxJ != minJ && maxI != minI) {
			// ����ߵĲ��Ǻ��ߺ�����
			canMove = false;// ����������
		}
	}

	public void zu(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

		// ����Ĵ����Ϸ���
		if (startJ < 5) {// �����û�й���
			if (startI != endI) {// ������
				canMove = false;// ����������
			}
			if (endJ - startJ != 1) {// �ߵĲ���һ�񣬲���ֻ����ǰ��
				canMove = false;// ����������
			}
		} else {
			// ����Ѿ�����
			if (startI == endI) {// ������
				if (endJ - startJ != 1) {// �ߵĲ���һ�񣬲���ֻ����ǰ��
					canMove = false;// ����������
				}
			} else if (startJ == endJ) {
				// ����ߵ��Ǻ���
				if (maxI - minI != 1) {// �ߵĲ���һ�񣬲���ֻ����ǰ��
					canMove = false;// ����������
				}
			} else if (startI != endI && startJ != endJ) {
				// ����ߵĲ��Ǻ���Ҳ��������,��������
				canMove = false;
			}
		}
	}

	public void bing(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

		// �Ա��Ĵ����·���
		if (startJ > 4) {// �����û�й���
			if (startI != endI) {// ������
				canMove = false;// ����������
			}
			if (endJ - startJ != -1) {// �ߵĲ���һ�񣬲���ֻ����ǰ��
				canMove = false;// ����������
			}
		} else {
			// ����Ѿ�����
			if (startI == endI) {// ������
				if (endJ - startJ != -1) {// �ߵĲ���һ�񣬲���ֻ����ǰ��
					canMove = false;// ����������
				}
			} else if (startJ == endJ) {
				// ����ߵ��Ǻ���
				if (maxI - minI != 1) {// �ߵĲ���һ�񣬲���ֻ����ǰ��
					canMove = false;// ����������
				}
			} else if (startI != endI && startJ != endJ) {
				// ����ߵĲ��Ǻ���Ҳ��������,��������
				canMove = false;
			}
		}
	}
}
