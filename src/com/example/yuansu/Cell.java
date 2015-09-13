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
 * ��������
 * 
 * @author Administrator
 * 
 */
public class Cell extends Actor {
	private int value;// ����������ӵķ���
	// coordinate in screen
	private int screenX;// ������������Ļ������
	private int screenY;
	// coordinate in board
	private int boardX;// ����������board�е�����
	private int boardY;

	
	/**
	 * �����ӱ�
	 */
	public void doubleValue() {
		value *= 2;//����ֵ�ӱ�
		//�������������һ������
		addAction(Actions.sequence(Actions.scaleTo(1.12f, 1.12f, 0.035f), Actions.scaleTo(1, 1, 0.035f)));
	}
	
	
	// ��durationʱ�����ƶ���(boardX,boardY)���λ����
	public void moveTo(int boardX, int boardY, int duration) {
		convertBoardToScreen(boardX, boardY);// ����board�е�λ��ת������Ļ�е�λ��
		// ��Ԫ�����Action
		this.addAction(Actions.moveTo(screenX, screenY, 0.1f / duration,
				Interpolation.linear));
	}

	/**
	 * ����board�е�λ��ת������Ļ�е�λ��
	 * 
	 * @param boardX
	 * @param boardY
	 */
	private void convertBoardToScreen(int boardX, int boardY) {
		this.boardX = boardX;
		this.boardY = boardY;
		/**
		 * ��Ļ�е�λ��=�������+ÿ���������ӵĿ��*���� + ��������֮��ľ���*����
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
		screenY += 7;// �����������Ļ�,���ܻ���һ��ƫ��

	}

	/**
	 * ���캯��: ���ݷ������ӵķ���ֵ����board�е�����������һ����������
	 * 
	 * @param value
	 *            ����ֵ
	 * @param boardX
	 *            ��board�е�����
	 * @param boardY
	 */
	public Cell(int value, int boardX, int boardY) {
		super();
		this.value = value;
		convertBoardToScreen(boardX, boardY);// ����board�е�����ת��������Ļ�е�����
		setPosition(screenX, screenY);// ���÷������ӵĴ�С
		setSize(Constants.CELL_WIDTH, Constants.CELL_WIDTH);// ���÷������ӵĴ�С
		setScale(0.8f);// ���÷������ӵ����ű���
		addAction(Actions.scaleTo(1, 1, 0.035f));// ����������������һ������
	}

	/**
	 * ÿ����Ա��Ԫ��������getXXX()��setXXX()����
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

		drawBg(batch);// �ѱ������ƻ���
		drawFenshu(batch);// �ѷ������Ƴ���
	}

	/**
	 * ���Ʒ������ӵı���
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
		 * ��������Oλ��
		 */
		float x = getX() + Constants.CELL_WIDTH / 2
				- Assets.font.getBounds(Integer.toString(value)).width / 2;
		float y = getY() + Constants.CELL_WIDTH * Constants.STAGE_STRETCH / 2
				+ Assets.font.getBounds("9985").height
				+ Constants.BITMAPFONT_LINEHEIGHT / 2
				* Assets.squares.get(value).getScale() * getScaleX();
		/**
		 * ���ݷ������ӵķ�ֵ����һ���������ɫ
		 */
		if (value > 4) {// ��ֵ>4
			Assets.font.setColor(Color.WHITE);// ���������ɫ���óɰ�ɫ
		} else {// �����ֵ<=4
			Assets.font.setColor(Color.GRAY);// ���������ɫ���Ƴɻ�ɫ
		}
		Assets.font.draw(batch, Integer.toString(value), x, y);// ���Ʒ�ֵ
	}
}
