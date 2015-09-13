package com.example.yuansu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * 用于存储每种分值对应的分数格子的背景的颜色
 * @author Administrator
 *
 */
public class Square {

	private int key;//分值
	private TextureRegion smallTexture;//对应背景
	private Color color;//背景的颜色
	private float scale;//缩放系数

	/**
	 * 根据分数格子的分值、和对应的RGB值来构造一个square
	 * @param key 分数格子的分值
	 * @param r 颜色的RGB值
	 * @param g
	 * @param b
	 */
	public Square(int key, int r, int g, int b) {
		this.key = key;
		this.color = new Color((r) / 255f, (g) / 255f, (b) / 255f, 1.0f);
		if (Integer.toString(key).length() <= 4){
			this.scale = 1;
		}else{
			this.scale = 4f / Integer.toString(key).length();
		}
		this.smallTexture = createBackground(color);
	}

	public Square(int key, float r, float g, float b) {
		this.key = key;
		this.color = new Color(r, g, b, 1.0f);
		if (Integer.toString(key).length() <= 4)
			this.scale = 1;
		else
			this.scale = 4f / Integer.toString(key).length();
		this.smallTexture = createBackground(color);
	}

	public Square(int key, Color color) {
		this.key = key;
		this.color = color;
		if (Integer.toString(key).length() <= 4)
			this.scale = 1;
		else
			this.scale = 4f / Integer.toString(key).length();

		this.smallTexture = createBackground(color);
	}

	/**
	 * 根据颜色，利用Pixmap来生成一个分数格子的背景
	 * @param color
	 * @return
	 */
	private TextureRegion createBackground(Color color) {

		if (key >= 128) {//如果分数格子的分值>=128
			//构造Pixmap
			Pixmap pixmap = new Pixmap(MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH), MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH), Format.RGBA8888);
			pixmap.setColor(Color.WHITE);
			pixmap.drawRectangle(0, 0,MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH), MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH));
			pixmap.setColor(color);
			pixmap.fillRectangle(1, 1,MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH) -2,MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH) -2);

			Texture texture = new Texture(pixmap);
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion region = new TextureRegion(texture,90,90);
			pixmap.dispose();
			return region;
		} else {//如果分数格子的分值< 128
			Pixmap pixmap = new Pixmap(MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH), MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH), Format.RGBA8888);
			
			pixmap.setColor(color);
			pixmap.fillRectangle(0, 0, MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH), MathUtils.nextPowerOfTwo(Constants.CELL_WIDTH) );

			Texture texture = new Texture(pixmap);
			TextureRegion region = new TextureRegion(texture,90,90);
//			region.setRegionWidth(90);
//			region.setRegionHeight(90);
			
			pixmap.dispose();
			return region;
		}
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public TextureRegion getTexture() {
		return smallTexture;
	}

	public void setTexture(TextureRegion texture) {
		this.smallTexture = texture;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}

