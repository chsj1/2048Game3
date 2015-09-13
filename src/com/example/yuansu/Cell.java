package com.example.yuansu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.example.hellolibgdx.Assets;

/**
 * 分数格子
 * 
 * @author Administrator
 * 
 */
public class Cell extends Actor {
	private int value;// 这个分数格子的分数
	// coordinate in screen
	private int screenX;// 分数格子在屏幕的坐标
	private int screenY;
	// coordinate in board
	private int boardX;// 分数格子在board中的坐标
	private int boardY;

	
	/**
	 * 分数加倍
	 */
	public void doubleValue() {
		value *= 2;//分数值加倍
		//给分数格子添加一个动作
		addAction(Actions.sequence(Actions.scaleTo(1.12f, 1.12f, 0.035f), Actions.scaleTo(1, 1, 0.035f)));
	}
	
	
	// 在duration时间内移动到(boardX,boardY)这个位置上
	public void moveTo(int boardX, int boardY, int duration) {
		convertBoardToScreen(boardX, boardY);// 将在board中的位置转换成屏幕中的位置
		// 给元素添加Action
		this.addAction(Actions.moveTo(screenX, screenY, 0.1f / duration,
				Interpolation.linear));
	}

	/**
	 * 将在board中的位置转化成屏幕中的位置
	 * 
	 * @param boardX
	 * @param boardY
	 */
	private void convertBoardToScreen(int boardX, int boardY) {
		this.boardX = boardX;
		this.boardY = boardY;
		/**
		 * 屏幕中的位置=起点坐标+每个分数格子的宽度*个数 + 分数格子之间的距离*个数
		 */
		screenX = Constants.BOARD_POS_BOT + Constants.BOARD_GRID_WIDTH
				* (this.boardY + 1) + this.boardY * Constants.CELL_WIDTH;
		screenY = (int) (Constants.BOARD_POS_BOT + Constants.BOARD_GRID_WIDTH
				* Constants.STAGE_STRETCH * (Constants.BOARD_COL - this.boardX) + (Constants.BOARD_COL - 1 - this.boardX)
				* Constants.CELL_WIDTH * Constants.STAGE_STRETCH);
		// screenY =(int) (Constants.BOARD_POS_BOT +
		// Constants.BOARD_GRID_WIDTH*(800f/480f) * (Constants.BOARD_COL -
		// this.boardX) + (Constants.BOARD_COL -1 - this.boardX) *
		// Constants.CELL_WIDTH*(800f/480f));
		screenY += 7;// 如果不加这个的话,可能会有一点偏差

	}

	/**
	 * 构造函数: 根据分数格子的分数值和在board中的坐标来生成一个分数格子
	 * 
	 * @param value
	 *            分数值
	 * @param boardX
	 *            在board中的坐标
	 * @param boardY
	 */
	public Cell(int value, int boardX, int boardY) {
		super();
		this.value = value;
		convertBoardToScreen(boardX, boardY);// 将在board中的坐标转化成在屏幕中的坐标
		setPosition(screenX, screenY);// 设置分数格子的大小
		setSize(Constants.CELL_WIDTH, Constants.CELL_WIDTH);// 设置分数格子的大小
		setScale(0.8f);// 设置分数格子的缩放倍数
		addAction(Actions.scaleTo(1, 1, 0.035f));// 给这个分数格子添加一个动画
	}

	/**
	 * 每个成员胡元白能量的getXXX()和setXXX()方法
	 * 
	 * @return
	 */
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	public int getBoardX() {
		return boardX;
	}

	public void setBoardX(int boardX) {
		this.boardX = boardX;
	}

	public int getBoardY() {
		return boardY;
	}

	public void setBoardY(int boardY) {
		this.boardY = boardY;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		// Assets.font.setScale(Assets.squares.get(value).getScale() *
		// getScaleX());

		drawBg(batch);// 把背景绘制回来
		drawFenshu(batch);// 把分数绘制出来
	}

	/**
	 * 绘制分数格子的背景
	 * 
	 * @param batch
	 */
	public void drawBg(SpriteBatch batch) {
		batch.draw(Assets.squares.get(value).getTexture(), getX(), getY(),
				Constants.CELL_WIDTH / 2, Constants.CELL_WIDTH / 2,
				Constants.CELL_WIDTH, Constants.CELL_WIDTH
						* Constants.STAGE_STRETCH, getScaleX(), getScaleX(), 0);
	}

	/**
	 * 
	 * @param batch
	 */
	public void drawFenshu(SpriteBatch batch) {
		/**
		 * 计算字体扥位置
		 */
		float x = getX() + Constants.CELL_WIDTH / 2
				- Assets.font.getBounds(Integer.toString(value)).width / 2;
		float y = getY() + Constants.CELL_WIDTH * Constants.STAGE_STRETCH / 2
				+ Assets.font.getBounds("9985").height
				+ Constants.BITMAPFONT_LINEHEIGHT / 2
				* Assets.squares.get(value).getScale() * getScaleX();
		/**
		 * 根据分数格子的分值控制一下字体的颜色
		 */
		if (value > 4) {// 分值>4
			Assets.font.setColor(Color.WHITE);// 将字体的颜色设置成白色
		} else {// 如果分值<=4
			Assets.font.setColor(Color.GRAY);// 将字体的颜色绘制成灰色
		}
		Assets.font.draw(batch, Integer.toString(value), x, y);// 绘制分值
	}
}
