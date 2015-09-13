package com.example.gamegroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.example.hellolibgdx.Assets;
import com.example.hellolibgdx.GameScreen;

/**
 * 成功界面逻辑体
 * @author Administrator
 *
 */
public class WinGroup extends Group {
	//tryagain按钮
	public Image tryAgainImage;
	//exit按钮
	public Image exitImage;
	//背景
	public Image bgImage;
	//用于实现功能的辅助的成员变量
	public GameScreen screen;
	public Stage stage;
	
	public WinGroup(final GameScreen screen,final Stage stage) {
		this.screen = screen;
		this.stage = stage;
		
		//控件的初始化
		tryAgainImage = new Image(Assets.tryagainRegion);
		tryAgainImage.setPosition(100, 200);
		
		exitImage = new Image(Assets.exitRegion);
		exitImage.setPosition(272, 200);
		
		bgImage = new Image(Assets.opacityRegion);
		bgImage.setSize(480, 800);
		
		tryAgainImage.addListener(new InputListener(){//当tryAgain按钮被点击的时候
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				screen.restartGame();//重新开始游戏
				screen.getWinGroup().hide();//并且把成功group给隐藏掉..
				
				return true;
			}
		});
		
		exitImage.addListener(new InputListener(){//给 exitImage天剑监听器
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.exit();//退出游戏
				return true;
			}
		});
		
		setWidth(bgImage.getWidth());//设置宽度
		setHeight(bgImage.getHeight());//这是高度
		setOrigin(getWidth()/2, getHeight()/2);//设置锚点
		
		//把控件都添加到Group中
		this.addActor(bgImage);
		this.addActor(tryAgainImage);
		this.addActor(exitImage);
	}
	
	/**
	 * 控制这个Group的显示
	 */
	public void show(){
		//以下两行代码确保工程界面调用时是位于stage的最上层
		this.remove();
		stage.addActor(this);
//		this.setVisible(true);//显示出来
		//Ian带你一个动画,把Group显示出来
		addAction(Actions.sequence(Actions.scaleTo(1.15f, 1.15f, 0.05f), Actions.scaleTo(1, 1, 0.05f)));
	}
	
	public void hide(){
//		this.setVisible(false);//隐藏
		//添加一个动画,让成功界面隐藏掉
		addAction(Actions.sequence(Actions.scaleTo(0f, 0f, 0.05f)));
	}
}
