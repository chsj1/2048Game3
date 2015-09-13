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
 * �ɹ������߼���
 * @author Administrator
 *
 */
public class WinGroup extends Group {
	//tryagain��ť
	public Image tryAgainImage;
	//exit��ť
	public Image exitImage;
	//����
	public Image bgImage;
	//����ʵ�ֹ��ܵĸ����ĳ�Ա����
	public GameScreen screen;
	public Stage stage;
	
	public WinGroup(final GameScreen screen,final Stage stage) {
		this.screen = screen;
		this.stage = stage;
		
		//�ؼ��ĳ�ʼ��
		tryAgainImage = new Image(Assets.tryagainRegion);
		tryAgainImage.setPosition(100, 200);
		
		exitImage = new Image(Assets.exitRegion);
		exitImage.setPosition(272, 200);
		
		bgImage = new Image(Assets.opacityRegion);
		bgImage.setSize(480, 800);
		
		tryAgainImage.addListener(new InputListener(){//��tryAgain��ť�������ʱ��
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				screen.restartGame();//���¿�ʼ��Ϸ
				screen.getWinGroup().hide();//���Ұѳɹ�group�����ص�..
				
				return true;
			}
		});
		
		exitImage.addListener(new InputListener(){//�� exitImage�콣������
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.exit();//�˳���Ϸ
				return true;
			}
		});
		
		setWidth(bgImage.getWidth());//���ÿ��
		setHeight(bgImage.getHeight());//���Ǹ߶�
		setOrigin(getWidth()/2, getHeight()/2);//����ê��
		
		//�ѿؼ�����ӵ�Group��
		this.addActor(bgImage);
		this.addActor(tryAgainImage);
		this.addActor(exitImage);
	}
	
	/**
	 * �������Group����ʾ
	 */
	public void show(){
		//�������д���ȷ�����̽������ʱ��λ��stage�����ϲ�
		this.remove();
		stage.addActor(this);
//		this.setVisible(true);//��ʾ����
		//Ian����һ������,��Group��ʾ����
		addAction(Actions.sequence(Actions.scaleTo(1.15f, 1.15f, 0.05f), Actions.scaleTo(1, 1, 0.05f)));
	}
	
	public void hide(){
//		this.setVisible(false);//����
		//���һ������,�óɹ��������ص�
		addAction(Actions.sequence(Actions.scaleTo(0f, 0f, 0.05f)));
	}
}
