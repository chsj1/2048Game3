package com.example.yuansu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * 暂退游戏时的数据处理逻辑类
 * 主要作用：:
 * 1)分数相关的恢复
 * 2)局面的恢复
 * @author Administrator
 */
public class Prefs {
	//维护一个自身的引用
	public static Prefs instance = new Prefs();
	//用于实际保存和获取数据
	private static Preferences pref;
	//当前分数
	public int score;
	//最高分数
	public int best;
	//是否开启新游戏
	public boolean isLaunchNewGame;
	//当前的游戏局面
	public String board;

	/**
	 * 获取所维护的自身的应用
	 * @return
	 */
	public static Prefs getInstance() {
		return instance;
	}
	/**
	 * 设置自身所维护的应用
	 * @param instance
	 */
	public static void setInstance(Prefs instance) {
		Prefs.instance = instance;
	}
	/**
	 * 获取pref
	 * @return
	 */
	public static Preferences getPref() {
		return pref;
	}
	/**
	 * 设置pref
	 * @param pref
	 */
	public static void setPref(Preferences pref) {
		Prefs.pref = pref;
	}
	/**
	 * 获取当前分数
	 * @return
	 */
	public int getScore() {
		return score;
	}
	/**
	 * 设置当前分数
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * 获取最高分数
	 * @return
	 */
	public int getBest() {
		return best;
	}
	/**
	 * 设置最高分数
	 * @param best
	 */
	public void setBest(int best) {
		this.best = best;
	}
	/**
	 * 获取是否开启新的游戏isLaunchNewGame
	 * @return
	 */
	public boolean isLaunchNewGame() {
		return isLaunchNewGame;
	}
	/**
	 * 设置是否开启新的字段isLaunchNewGame
	 * @param isLaunchNewGame
	 */
	public void setLaunchNewGame(boolean isLaunchNewGame) {
		this.isLaunchNewGame = isLaunchNewGame;
	}
	/**
	 * 获取board的数据
	 * @return
	 */
	public String getBoard() {
		return board;
	}
	/**
	 * 设置board的数据
	 * @param board
	 */
	public void setBoard(String board) {
		this.board = board;
	}
	/**
	 * 构造函数
	 */
	private Prefs() {
		//获取一个Prefereneces的引用
		pref = Gdx.app.getPreferences("hjd-2048");
		// 如果是第一次进入游戏
		if (!pref.contains("score")) {
			//将当前分数score默认设置成0
			pref.putInteger("score", 0);
			//将最高分数best默认设置成0
			pref.putInteger("best", 0);
			//将isFirstLaunchGame默认设置成true
			pref.putBoolean("isFirstLaunchGame", true);
			//将board中的数据默认设置为空
			pref.putString("board", "");
			//将数据冲刷进去
			pref.flush();
		}
		//完成变量的初始化
		load();
	}

	/**
	 * 成员变量的初始化
	 */
	private void load() {
		//初始化当前分数score的值
		score = pref.getInteger("score", 0);
		//初始化最高分数best的值
		best = pref.getInteger("best", 0);
		//初始化isFirstLaunchGame的值
		isLaunchNewGame = pref.getBoolean("isFirstLaunchGame", true);
		//初始化board中的数据
		board = pref.getString("board", "");
	}

	/**
	 * 保存分数
	 */
	public void saveScore() {
		//保存当前分数
		pref.putInteger("score", Score.instance.getScore());
		//保存最高分数
		pref.putInteger("best", Score.instance.getBest());
		//将数据冲刷进去
		pref.flush();
	}
	/**
	 * 保存局面
	 * @param board
	 * @param isLauchNewGame
	 */
	public void saveBoard(String board, boolean isLauchNewGame) {
		//如果不是开启一个新的游戏
		if (!isLauchNewGame) {
			//保存board的数据
			pref.putString("board", board);
			//打印出一个log
			Gdx.app.log("Save Board1", board);
		}
		//修改isFirstLaunchGame的值
		pref.putBoolean("isFirstLaunchGame", isLauchNewGame);
		//将数据冲刷进去
		pref.flush();
	}
}
