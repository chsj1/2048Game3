package com.example.hellolibgdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class MyGame extends Game {
	GameScreen gameScreen;
	
	@Override
	public void create() {
		Assets.load();
		
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

		super.pause();
	}
	
	
	
	
	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	
}
