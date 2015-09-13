package com.example.yuansu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * ���ڴ洢ÿ�ַ�ֵ��Ӧ�ķ������ӵı�������ɫ
 * @author Administrator
 *
 */
public class Square {

	private int key;//��ֵ
	private TextureRegion smallTexture;//��Ӧ����
	private Color color;//��������ɫ
	private float scale;//����ϵ��

	/**
	 * ���ݷ������ӵķ�ֵ���Ͷ�Ӧ��RGBֵ������һ��square
	 * @param key �������ӵķ�ֵ
	 * @param r ��ɫ��RGBֵ
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
	 * ������ɫ������Pixmap������һ���������ӵı���
	 * @param color
	 * @return
	 */
	private TextureRegion createBackground(Color color) {

		if (key >= 128) {//����������ӵķ�ֵ>=128
			//����Pixmap
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
		} else {//����������ӵķ�ֵ< 128
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

