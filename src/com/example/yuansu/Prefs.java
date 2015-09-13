package com.example.yuansu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * ������Ϸʱ�����ݴ����߼���
 * ��Ҫ���ã�:
 * 1)������صĻָ�
 * 2)����Ļָ�
 * @author Administrator
 */
public class Prefs {
	//ά��һ�����������
	public static Prefs instance = new Prefs();
	//����ʵ�ʱ���ͻ�ȡ����
	private static Preferences pref;
	//��ǰ����
	public int score;
	//��߷���
	public int best;
	//�Ƿ�������Ϸ
	public boolean isLaunchNewGame;
	//��ǰ����Ϸ����
	public String board;

	/**
	 * ��ȡ��ά���������Ӧ��
	 * @return
	 */
	public static Prefs getInstance() {
		return instance;
	}
	/**
	 * ����������ά����Ӧ��
	 * @param instance
	 */
	public static void setInstance(Prefs instance) {
		Prefs.instance = instance;
	}
	/**
	 * ��ȡpref
	 * @return
	 */
	public static Preferences getPref() {
		return pref;
	}
	/**
	 * ����pref
	 * @param pref
	 */
	public static void setPref(Preferences pref) {
		Prefs.pref = pref;
	}
	/**
	 * ��ȡ��ǰ����
	 * @return
	 */
	public int getScore() {
		return score;
	}
	/**
	 * ���õ�ǰ����
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * ��ȡ��߷���
	 * @return
	 */
	public int getBest() {
		return best;
	}
	/**
	 * ������߷���
	 * @param best
	 */
	public void setBest(int best) {
		this.best = best;
	}
	/**
	 * ��ȡ�Ƿ����µ���ϷisLaunchNewGame
	 * @return
	 */
	public boolean isLaunchNewGame() {
		return isLaunchNewGame;
	}
	/**
	 * �����Ƿ����µ��ֶ�isLaunchNewGame
	 * @param isLaunchNewGame
	 */
	public void setLaunchNewGame(boolean isLaunchNewGame) {
		this.isLaunchNewGame = isLaunchNewGame;
	}
	/**
	 * ��ȡboard������
	 * @return
	 */
	public String getBoard() {
		return board;
	}
	/**
	 * ����board������
	 * @param board
	 */
	public void setBoard(String board) {
		this.board = board;
	}
	/**
	 * ���캯��
	 */
	private Prefs() {
		//��ȡһ��Prefereneces������
		pref = Gdx.app.getPreferences("hjd-2048");
		// ����ǵ�һ�ν�����Ϸ
		if (!pref.contains("score")) {
			//����ǰ����scoreĬ�����ó�0
			pref.putInteger("score", 0);
			//����߷���bestĬ�����ó�0
			pref.putInteger("best", 0);
			//��isFirstLaunchGameĬ�����ó�true
			pref.putBoolean("isFirstLaunchGame", true);
			//��board�е�����Ĭ������Ϊ��
			pref.putString("board", "");
			//�����ݳ�ˢ��ȥ
			pref.flush();
		}
		//��ɱ����ĳ�ʼ��
		load();
	}

	/**
	 * ��Ա�����ĳ�ʼ��
	 */
	private void load() {
		//��ʼ����ǰ����score��ֵ
		score = pref.getInteger("score", 0);
		//��ʼ����߷���best��ֵ
		best = pref.getInteger("best", 0);
		//��ʼ��isFirstLaunchGame��ֵ
		isLaunchNewGame = pref.getBoolean("isFirstLaunchGame", true);
		//��ʼ��board�е�����
		board = pref.getString("board", "");
	}

	/**
	 * �������
	 */
	public void saveScore() {
		//���浱ǰ����
		pref.putInteger("score", Score.instance.getScore());
		//������߷���
		pref.putInteger("best", Score.instance.getBest());
		//�����ݳ�ˢ��ȥ
		pref.flush();
	}
	/**
	 * �������
	 * @param board
	 * @param isLauchNewGame
	 */
	public void saveBoard(String board, boolean isLauchNewGame) {
		//������ǿ���һ���µ���Ϸ
		if (!isLauchNewGame) {
			//����board������
			pref.putString("board", board);
			//��ӡ��һ��log
			Gdx.app.log("Save Board1", board);
		}
		//�޸�isFirstLaunchGame��ֵ
		pref.putBoolean("isFirstLaunchGame", isLauchNewGame);
		//�����ݳ�ˢ��ȥ
		pref.flush();
	}
}
