package com.example.yuansu;

/**
 * �����߼���. ���ڴ����������ص��߼�
 * 
 * @author Administrator
 * 
 */
public class Score {

	public static Score instance = new Score();

	private int score;// ��ǰ����
	private int best;// ��߷���

	public Score() {
		score = Prefs.instance.score;
		best = Prefs.instance.best;
		// score = 0;
		// best = 0;

	}

	/**
	 * ��Ӧ��getXXX()��setXXX()����
	 * 
	 * @return
	 */
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		if (score > best) {
			best = score;
		}
	}

	/**
	 * �ӷֲ��Ҹ�����Ϸ�ķ�ֵ
	 * 
	 * @param score
	 */
	public void addScore(int score) {
		this.score += score;
		// �����ǰ���� > ��߷���.������߷���
		this.best = (this.score > best) ? this.score : best;
	}

	public int getBest() {
		return best;
	}
}
