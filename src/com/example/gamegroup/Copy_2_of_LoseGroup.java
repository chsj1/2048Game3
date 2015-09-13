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
 * 失败界面逻辑体
 * @author Administrator
 *
 */
public class Copy_2_of_LoseGroup extends Group {
	//tryagain按钮
	public Image tryAgainImage;
	//exit按钮
	public Image exitImage;
	//背景
	public Image bgImage;
	//输掉游戏的logo
	public Image gameoverImage;
	//用于实现功能的辅助的成员变量
	public GameScreen screen;
	public Stage stage;
	
	/**
	 * 获取tryAgainImage对象
	 * @return
	 */
	public Image getTryAgainImage() {
		return tryAgainImage;
	}
	/**
	 * 设置tryAgainImage对象
	 * @param tryAgainImage
	 */
	public void setTryAgainImage(Image tryAgainImage) {
		this.tryAgainImage = tryAgainImage;
	}
	/**
	 * 获取exitImage对象
	 * @return
	 */
	public Image getExitImage() {
		return exitImage;
	}
	/**
	 * 设置exitImage对象
	 * @param tryAgainImage
	 */
	public void setExitImage(Image exitImage) {
		this.exitImage = exitImage;
	}
	/**
	 * 获取bgImage对象
	 * @return
	 */
	public Image getBgImage() {
		return bgImage;
	}
	/**
	 * 设置bgImage对象
	 * @param tryAgainImage
	 */
	public void setBgImage(Image bgImage) {
		this.bgImage = bgImage;
	}
	/**
	 * 获取screen对象
	 * @return
	 */
	public GameScreen getScreen() {
		return screen;
	}
	/**
	 * 设置screen对象
	 * @param tryAgainImage
	 */
	public void setScreen(GameScreen screen) {
		this.screen = screen;
	}
	/**
	 * 获取stage对象
	 * @return
	 */
	public Stage getStage() {
		return stage;
	}
	/**
	 * 设置stage对象
	 * @param tryAgainImage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * LoseGroup的构造函数,主要负责:
	 * 成员变量的初始化,注册监听器,将演员添加到舞台
	 * @param screen
	 * @param stage
	 */
	public Copy_2_of_LoseGroup(final GameScreen screen,final Stage stage) {
		//设置辅助对象
		this.screen = screen;
		this.stage = stage;
		//初始化再来一次tryAgainImage对象
		tryAgainImage = new Image(Assets.tryagainRegion);
		//设置再来一次tryAgainImage按钮的位置
		tryAgainImage.setPosition(100, 200);
		//初始化退出按钮exitImage对象
		exitImage = new Image(Assets.exitRegion);
		//设置退出按钮的位置
		exitImage.setPosition(272, 200);
		//初始化背景图片
		bgImage = new Image(Assets.opacityRegion);
		//设置背景图片的大小
		bgImage.setSize(480, 800);
		/**
		 * 给再来一遍按钮tryAgain添加点击事件
		 */
		tryAgainImage.addListener(new InputListener(){
			/**
			 * 当手指按下的时候
			 */
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				//重新开始游戏
				screen.restartGame();
				//并且把成功group给隐藏掉
				screen.getLoseGroup().hide();
				return true;
			}
		});
		/**
		 * 给退出按钮添加点击事件
		 */
		exitImage.addListener(new InputListener(){
			/**
			 * 当手指按下的时候
			 */
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				//退出游戏
				Gdx.app.exit();
				return true;
			}
		});
		//设置宽度
		setWidth(bgImage.getWidth());
		//这是高度
		setHeight(bgImage.getHeight());
		//设置锚点
		setOrigin(getWidth()/2, getHeight()/2);
		//把背景添加到舞台中
		this.addActor(bgImage);
		//把再来一遍按钮添加到舞台中
		this.addActor(tryAgainImage);
		//把退出按钮添加到舞台中
		this.addActor(exitImage);
		//把游戏结束按钮添加到舞台中
		this.addActor(gameoverImage);
	}
	
	/**
	 * 控制这个Group的显示
	 */
	public void show(){
		/**
		 * 以下两行代码确保工程界面调用时是位于stage的最上层。
		 * 其主要步骤是:
		 * 1)先将该控件从舞台中移除
		 * 2)再将该控件添加到舞台中
		 */
		this.remove();
		stage.addActor(this);
		//加一个动画,把Group显示出来
		addAction(Actions.sequence(Actions.scaleTo(1.15f, 1.15f, 0.05f), Actions.scaleTo(1, 1, 0.05f)));
	}
	/**
	 * 将控件隐藏
	 */
	public void hide(){
		//添加一个动画,让成功界面隐藏掉
		addAction(Actions.sequence(Actions.scaleTo(0f, 0f, 0.05f)));
	}
}
