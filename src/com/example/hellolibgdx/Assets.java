package com.example.hellolibgdx;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.example.yuansu.Square;

public class Assets {
	static TextureAtlas atlas;
	static TextureRegion bgRegion;
	public static TextureRegion exitRegion;
	static TextureRegion newRegion;
	public static TextureRegion gameoverRegion;
	public static TextureRegion opacityRegion;
	public static TextureRegion restartRegion;
	public static TextureRegion tryagainRegion;
	
	
	public static BitmapFont font;//游戏中用到的字体
	public static Map<Integer,Square> squares;//用于存储各种分值对
	
	
	public static void load(){
		atlas = new TextureAtlas(Gdx.files.internal("data/2048.txt"));
		bgRegion = atlas.findRegion("background");
		exitRegion = atlas.findRegion("exit");
		gameoverRegion = atlas.findRegion("game-over");
		opacityRegion = atlas.findRegion("opacity");
		restartRegion = atlas.findRegion("restart");
		tryagainRegion = atlas.findRegion("try-again");
		newRegion = atlas.findRegion("new");
		
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		initColor();//初始化各种分数对应的颜色
	}
	
	
	/**
	 * 初始化各种分数对应的颜色
	 */
	public static void initColor() {
		squares = new HashMap<Integer, Square>();
		squares.put(2, new Square(2, 238, 228, 218));//分值2对应RGD为(238,228,218)这种颜色
		squares.put(4, new Square(4, 237, 205, 123));
		squares.put(8, new Square(8, 255, 131, 30));
		squares.put(16, new Square(16, 255, 129, 8));
		squares.put(32, new Square(32, 237, 88, 52));
		squares.put(64, new Square(64, 217, 54, 17));
		squares.put(128, new Square(128, 255, 220, 102));
		squares.put(256, new Square(256, 255, 215, 80));
		squares.put(512, new Square(512, 255, 210, 60));
		squares.put(1024, new Square(1024, 255, 211, 1));
		squares.put(2048, new Square(2048, 255, 195, 0));
	}
}
