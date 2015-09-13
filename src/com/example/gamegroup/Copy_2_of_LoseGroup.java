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
 * ʧ�ܽ����߼���
 * @author Administrator
 *
 */
public class Copy_2_of_LoseGroup extends Group {
	//tryagain��ť
	public Image tryAgainImage;
	//exit��ť
	public Image exitImage;
	//����
	public Image bgImage;
	//�����Ϸ��logo
	public Image gameoverImage;
	//����ʵ�ֹ��ܵĸ����ĳ�Ա����
	public GameScreen screen;
	public Stage stage;
	
	/**
	 * ��ȡtryAgainImage����
	 * @return
	 */
	public Image getTryAgainImage() {
		return tryAgainImage;
	}
	/**
	 * ����tryAgainImage����
	 * @param tryAgainImage
	 */
	public void setTryAgainImage(Image tryAgainImage) {
		this.tryAgainImage = tryAgainImage;
	}
	/**
	 * ��ȡexitImage����
	 * @return
	 */
	public Image getExitImage() {
		return exitImage;
	}
	/**
	 * ����exitImage����
	 * @param tryAgainImage
	 */
	public void setExitImage(Image exitImage) {
		this.exitImage = exitImage;
	}
	/**
	 * ��ȡbgImage����
	 * @return
	 */
	public Image getBgImage() {
		return bgImage;
	}
	/**
	 * ����bgImage����
	 * @param tryAgainImage
	 */
	public void setBgImage(Image bgImage) {
		this.bgImage = bgImage;
	}
	/**
	 * ��ȡscreen����
	 * @return
	 */
	public GameScreen getScreen() {
		return screen;
	}
	/**
	 * ����screen����
	 * @param tryAgainImage
	 */
	public void setScreen(GameScreen screen) {
		this.screen = screen;
	}
	/**
	 * ��ȡstage����
	 * @return
	 */
	public Stage getStage() {
		return stage;
	}
	/**
	 * ����stage����
	 * @param tryAgainImage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * LoseGroup�Ĺ��캯��,��Ҫ����:
	 * ��Ա�����ĳ�ʼ��,ע�������,����Ա��ӵ���̨
	 * @param screen
	 * @param stage
	 */
	public Copy_2_of_LoseGroup(final GameScreen screen,final Stage stage) {
		//���ø�������
		this.screen = screen;
		this.stage = stage;
		//��ʼ������һ��tryAgainImage����
		tryAgainImage = new Image(Assets.tryagainRegion);
		//��������һ��tryAgainImage��ť��λ��
		tryAgainImage.setPosition(100, 200);
		//��ʼ���˳���ťexitImage����
		exitImage = new Image(Assets.exitRegion);
		//�����˳���ť��λ��
		exitImage.setPosition(272, 200);
		//��ʼ������ͼƬ
		bgImage = new Image(Assets.opacityRegion);
		//���ñ���ͼƬ�Ĵ�С
		bgImage.setSize(480, 800);
		/**
		 * ������һ�鰴ťtryAgain��ӵ���¼�
		 */
		tryAgainImage.addListener(new InputListener(){
			/**
			 * ����ָ���µ�ʱ��
			 */
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				//���¿�ʼ��Ϸ
				screen.restartGame();
				//���Ұѳɹ�group�����ص�
				screen.getLoseGroup().hide();
				return true;
			}
		});
		/**
		 * ���˳���ť��ӵ���¼�
		 */
		exitImage.addListener(new InputListener(){
			/**
			 * ����ָ���µ�ʱ��
			 */
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				//�˳���Ϸ
				Gdx.app.exit();
				return true;
			}
		});
		//���ÿ��
		setWidth(bgImage.getWidth());
		//���Ǹ߶�
		setHeight(bgImage.getHeight());
		//����ê��
		setOrigin(getWidth()/2, getHeight()/2);
		//�ѱ�����ӵ���̨��
		this.addActor(bgImage);
		//������һ�鰴ť��ӵ���̨��
		this.addActor(tryAgainImage);
		//���˳���ť��ӵ���̨��
		this.addActor(exitImage);
		//����Ϸ������ť��ӵ���̨��
		this.addActor(gameoverImage);
	}
	
	/**
	 * �������Group����ʾ
	 */
	public void show(){
		/**
		 * �������д���ȷ�����̽������ʱ��λ��stage�����ϲ㡣
		 * ����Ҫ������:
		 * 1)�Ƚ��ÿؼ�����̨���Ƴ�
		 * 2)�ٽ��ÿؼ���ӵ���̨��
		 */
		this.remove();
		stage.addActor(this);
		//��һ������,��Group��ʾ����
		addAction(Actions.sequence(Actions.scaleTo(1.15f, 1.15f, 0.05f), Actions.scaleTo(1, 1, 0.05f)));
	}
	/**
	 * ���ؼ�����
	 */
	public void hide(){
		//���һ������,�óɹ��������ص�
		addAction(Actions.sequence(Actions.scaleTo(0f, 0f, 0.05f)));
	}
}
