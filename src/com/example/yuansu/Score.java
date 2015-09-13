package com.example.yuansu;

/**
 * 分数逻辑体. 用于处理与分数相关的逻辑
 * 
 * @author Administrator
 * 
 */
public class Score {

	public static Score instance = new Score();

	private int score;// 当前分数
	private int best;// 最高分数

	public Score() {
		score = Prefs.instance.score;
		best = Prefs.instance.best;
		// score = 0;
		// best = 0;

	}

	/**
	 * 相应的getXXX()与setXXX()方法
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
	 * 加分并且更新游戏的分值
	 * 
	 * @param score
	 */
	public void addScore(int score) {
		this.score += score;
		// 如果当前分数 > 最高分数.更新最高分数
		this.best = (this.score > best) ? this.score : best;
	}

	public int getBest() {
		return best;
	}
}
