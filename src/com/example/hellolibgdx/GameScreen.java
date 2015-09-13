package com.example.hellolibgdx;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.example.gamegroup.LoseGroup;
import com.example.gamegroup.WinGroup;
import com.example.yuansu.Cell;
import com.example.yuansu.Prefs;
import com.example.yuansu.Score;

public class GameScreen implements Screen {

	MyGame game;

	Stage stage;
	Image bgImage;
	Image newImage;
	Image exitImage;

	private int emptyCellCount;// ���ڼ�¼�ո��ӵ�����
	private Cell[][] board;// board�е�16����������

	// ���ڷ�ֵ��ͬʱ�ĺϲ�
	private Array<Cell> shouldRemoveSquareActors;// ��Ҫ�Ƴ���Ԫ��
	private Array<Cell> shouldDoubleSquareActors;// ��Ҫ˫����Ԫ��

	//����ʵ����Ϸ�еķ���
	LabelStyle fenshuLabelStyle;
	Label fenshuLabel;//������ʾ��ǰ����
	Label maxFenshuLabel;//������ʾ��߷���
	
	
	int maxValue;//���ڼ�¼��ǰ�ﵽ�ķ��������еķ��������ֵ
	WinGroup winGroup;
	
	//---����ʵ��ʧ�ܽ�����صı���
	int direction[][] = {//�������,�����ж��Ƿ��ܷ����ϲ��¼�
			{1,0},//����
			{-1,0},//����
			{0,1},//����
			{0,-1}//����
	};
	LoseGroup loseGroup;
	
	
	
	public GameScreen(MyGame game) {
		super();
		this.game = game;

		stage = new Stage(480, 800, false);
		bgImage = new Image(Assets.bgRegion);
		bgImage.setSize(480, 800);
		// bgImage.setSize(480, 640);

		newImage = new Image(Assets.newRegion);
		newImage.setPosition(30, 585);

		exitImage = new Image(Assets.exitRegion);
		exitImage.setPosition(110, 585);
		exitImage.setSize(Assets.newRegion.getRegionWidth(),
				Assets.newRegion.getRegionHeight());

		stage.addActor(bgImage);
		stage.addActor(exitImage);
		stage.addActor(newImage);

		initBoard();// ��ʼ��board
		
		//���ڷ�ֵ��ͬʱ�ĺϲ�
		shouldRemoveSquareActors = new Array<Cell>();
		shouldDoubleSquareActors = new Array<Cell>();
		
		addListenerOnNewBtn();// ��new��ť���ʱ���������¿�ʼ��Ϸ
		addListenerOnExitBtn();// ��exit��ť��Ӽ�����..

		addListenerOnStage();//��stage��Ӽ�����,���ڴ�����Ϸ�����߼�
		
		//---------���ڴ�������߼�
		initFenshuLabel();
		addFenshuLabelToStage();
		
		//---------�ɹ���ʧ�ܽ���
		initWinAndLoseGroup();
//		addWinAndLoseGroupToStage();
	}
	
	
	
	public LoseGroup getLoseGroup() {
		return loseGroup;
	}



	public void setLoseGroup(LoseGroup loseGroup) {
		this.loseGroup = loseGroup;
	}



	public WinGroup getWinGroup() {
		return winGroup;
	}



	public void setWinGroup(WinGroup winGroup) {
		this.winGroup = winGroup;
	}



	public void initWinAndLoseGroup(){
		winGroup = new WinGroup(this,stage);
		if(maxValue == 2048){
			winGroup.show();
		}else{
			winGroup.hide();
		}
		
		loseGroup = new LoseGroup(this, stage);
		if(isGameLose() == true){
			loseGroup.show();
		}else{
			loseGroup.hide();
		}
//		winGroup.setVisible(false);
	}
	
	public void addWinAndLoseGroupToStage(){
		stage.addActor(winGroup);
	}
	/**
	 * �ѷ�����ص�Label��ӵ���̨��
	 */
	public void addFenshuLabelToStage(){
		stage.addActor(fenshuLabel);
		stage.addActor(maxFenshuLabel);
	}
	
	/**
	 * ��ʼ��������ص�Label
	 */
	public void initFenshuLabel(){
		fenshuLabelStyle = new LabelStyle(Assets.font, Color.WHITE);//��ʼ��LabelStyle
		
//		fenshuLabel = new Label("0", fenshuLabelStyle);//��ʼ��fenshuLabel
		fenshuLabel = new Label(Score.instance.getScore() + "", fenshuLabelStyle);//��ʼ��fenshuLabel
		fenshuLabel.setPosition(291 - fenshuLabel.getTextBounds().width/2, 692);//����fenshuLabel,�������д���
		
		maxFenshuLabel = new Label(Score.instance.getBest() + "", fenshuLabelStyle);
		maxFenshuLabel.setPosition(403 - maxFenshuLabel.getTextBounds().width/2, 692);
	}

	/**
	 * ���ںϲ�����
	 */
	public void unionFenshu(){
		{
			// ����Ҫɾ���ķ�������ɾ����
			Iterator<Cell> iters = shouldRemoveSquareActors
					.iterator();
			while (iters.hasNext()) {//�����������ݽṹ
				Cell actor = iters.next();
				stage.getRoot().removeActor(actor);//�������Ԫ�ش�wutaistage���Ƴ�
				iters.remove();//���Ҵ����ݽṹ���Ƴ�
			}
		}
		{
			//���ڰѷ������ӵķ����ӱ�
			Iterator<Cell> iters = shouldDoubleSquareActors
					.iterator();
			while (iters.hasNext()) {//�������ݽṹ�е�����Ԫ��
				Cell actor = iters.next();
				actor.doubleValue();//����ǰ�����ӱ�
				//�жϵ�ǰ�����ӱ�֮���Ƿ�>ĿǰΪֹ������ֵ.�������,��������ֵ
				maxValue = actor.getValue() > maxValue ? actor
						.getValue() : maxValue;
				iters.remove();//���Ҵ����ݽṹ���Ƴ�
			}
		}
	}
	
	/**
	 * �����ƶ�
	 * 
	 * ��Ҫ��סboard�е������������ӵ�. boardX,boardY (0,0),(0,1) (1,0),(1,1) (2,0),(2,1)
	 * (3,0),(3,1)
	 */
	private void touchDragToUp() {
		for (int j = 0; j < 4; j++) {// ����ÿһ��
			Cell lastActor = null;// �������õķ�������(�����õĵ���һ��Ԫ��)
			int adjustPosition = 0;// ��ǰ���ڵ�����λ��,Ĭ�ϴ�0��ʼ
			for (int i = 0; i < 4; i++) {// ����ÿһ��...(��Ҫע�����ʱ���һЩ˳��.�����j.�����i)
				if (board[i][j] == null) {// �����ǰ�ĸ���Ϊnull
					continue;// ������һ��ѭ��
				} else {// �����ǰ���Ӳ�Ϊ��
					if (lastActor == null) {// ���֮ǰ��û�е�������������,����ǵ�һ�������ķ�������
						if (adjustPosition != i) {// �����ǰ�����ķ������Ӳ������ڵ�����λ��
							board[adjustPosition][j] = board[i][j];// ��ô�������ú�ķ�������ָ��ǰ��������
							board[i][j] = null;// ����ǰ����������Ϊ��
							board[adjustPosition][j].moveTo(adjustPosition, j,
									i - adjustPosition);// �����������ƶ���ָ��λ��
						}
						lastActor = board[adjustPosition][j];// �����������õķ�������lastActor
						adjustPosition++;// ���ڵ�����λ�õ�����+1
					} else {// ���֮ǰ�Ѿ���������������
						// ����������ķ������ӵķ���ֵ != ��ǰ�����ķ������ӵķ���ֵ.���ǲ���Ҫ�ϲ�
						if (lastActor.getValue() != board[i][j].getValue()) {
							if (adjustPosition != i) {// ��ǰ�����ķ������Ӳ������ڵ�����λ����
								// �ѵ�ǰ�����ķ��������ѵ����ڵ�����λ����
								board[adjustPosition][j] = board[i][j];
								board[i][j] = null;
								board[adjustPosition][j].moveTo(adjustPosition,
										j, i - adjustPosition);
							}
							lastActor = board[adjustPosition][j];// �����������õķ�������
							adjustPosition++;// �������ڵ�����λ��
						} else {// �����ǰԪ�غ���һ��Ԫ�صķ�ֵһ��
							// ����ǰԪ���ƶ�����һ��Ԫ������
							board[i][j].moveTo(lastActor.getBoardX(),
									lastActor.getBoardY(),
									i - lastActor.getBoardX());
							// ���ϵ�ǰԪ�صķ���
							 Score.instance.addScore(board[i][j].getValue());
							 shouldRemoveSquareActors.add(board[i][j]);//��board[i][j]��ӵ���Ҫ�Ƴ���������
							 shouldDoubleSquareActors.add(lastActor);//��lastActor��ӵ�������˫������������
							emptyCellCount++;// ��Ϊ�ϲ���,���Կո�����+1
							lastActor = null;// lastActor��Ϊ��
							board[i][j] = null;// board[i][j]��Ϊ��
						}
					}

				}
			}
		}
	}

	/**
	 * �����ƶ�
	 */
	private void touchDragToDown() {
		for (int j = 0; j < 4; j++) {
			Cell lastActor = null;
			int adjustPosition = 3;
			for (int i = 3; i >= 0; i--) {
				if (board[i][j] == null)
					continue;
				else {
					if (lastActor == null) {
						if (adjustPosition != i) {
							board[adjustPosition][j] = board[i][j];
							board[i][j] = null;
							board[adjustPosition][j].moveTo(adjustPosition, j,
									adjustPosition - i);
						}
						lastActor = board[adjustPosition][j];
						adjustPosition--;
					} else {
						if (lastActor.getValue() != board[i][j].getValue()) {

							if (adjustPosition != i) {

								board[adjustPosition][j] = board[i][j];
								board[i][j] = null;
								board[adjustPosition][j].moveTo(adjustPosition,
										j, adjustPosition - i);
							}
							lastActor = board[adjustPosition][j];
							adjustPosition--;

						} else {

							board[i][j].moveTo(lastActor.getBoardX(),
									lastActor.getBoardY(),
									lastActor.getBoardX() - i);
							 shouldRemoveSquareActors.add(board[i][j]);
							 Score.instance.addScore(board[i][j].getValue());
							 shouldDoubleSquareActors.add(lastActor);
							emptyCellCount++;
							lastActor = null;
							board[i][j] = null;

						}
					}

				}
			}
		}
	}

	/**
	 * �����ƶ�
	 */
	private void touchDragToLeft() {
		for (int i = 0; i < 4; i++) {
			Cell lastActor = null;
			int adjustPosition = 0;
			for (int j = 0; j < 4; j++) {
				if (board[i][j] == null)
					continue;
				else {
					if (lastActor == null) {

						if (adjustPosition != j) {
							board[i][adjustPosition] = board[i][j];
							board[i][j] = null;
							board[i][adjustPosition].moveTo(i, adjustPosition,
									j - adjustPosition);
						}
						lastActor = board[i][adjustPosition];

						adjustPosition++;
					} else {
						if (lastActor.getValue() != board[i][j].getValue()) {
							if (adjustPosition != j) {
								board[i][adjustPosition] = board[i][j];
								board[i][j] = null;
								board[i][adjustPosition].moveTo(i,
										adjustPosition, j - adjustPosition);
							}
							lastActor = board[i][adjustPosition];

							adjustPosition++;

						} else {
							board[i][j].moveTo(lastActor.getBoardX(),
									lastActor.getBoardY(),
									j - lastActor.getBoardY());
							 shouldDoubleSquareActors.add(lastActor);
							 Score.instance.addScore(board[i][j].getValue());
							 shouldRemoveSquareActors.add(board[i][j]);
							emptyCellCount++;

							lastActor = null;
							board[i][j] = null;

						}
					}

				}
			}
		}

	}

	/**
	 * �����ƶ�
	 */
	private void touchDragToRight() {
		for (int i = 0; i < 4; i++) {
			Cell lastActor = null;
			int adjustPosition = 3;
			for (int j = 3; j >= 0; j--) {
				if (board[i][j] == null)
					continue;
				else {
					if (lastActor == null) {

						if (adjustPosition != j) {
							board[i][adjustPosition] = board[i][j];
							board[i][j] = null;
							board[i][adjustPosition].moveTo(i, adjustPosition,
									adjustPosition - j);
						}
						lastActor = board[i][adjustPosition];

						adjustPosition--;
					} else {
						if (lastActor.getValue() != board[i][j].getValue()) {
							if (adjustPosition != j) {
								board[i][adjustPosition] = board[i][j];
								board[i][j] = null;
								board[i][adjustPosition].moveTo(i,
										adjustPosition, adjustPosition - j);
							}
							lastActor = board[i][adjustPosition];
							adjustPosition--;

						} else {
							board[i][j].moveTo(lastActor.getBoardX(),
									lastActor.getBoardY(),
									lastActor.getBoardY() - j);

							 shouldRemoveSquareActors.add(board[i][j]);
							 Score.instance.addScore(board[i][j].getValue());
							 shouldDoubleSquareActors.add(lastActor);
							emptyCellCount++;
							lastActor = null;
							board[i][j] = null;
						}
					}

				}
			}
		}
	}

	public void addListenerOnStage() {
		stage.addListener(new InputListener() {//��stage��Ӽ�����
			/**
			 * keycode���ص����㰴�µļ����еİ�����Ψһ��ʶ
			 */
			@Override
			public boolean keyDown(InputEvent event, int keycode) {

				if (keycode == Keys.UP) {//����㰴�µ����ϵİ�ť
					touchDragToUp();//��ô���еķ������������ƶ�
				} else if (keycode == Keys.DOWN) {
					touchDragToDown();
				} else if (keycode == Keys.LEFT) {
					touchDragToLeft();
				} else if (keycode == Keys.RIGHT) {
					touchDragToRight();
				}

				return true;
			}
			
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				unionFenshu();//�����ƶ���ϲ���������
				addCell();//ÿ�β���������µķ�������
				
				updateFenshu();//���µ�ǰ����..
				
				checkIsWinOrLose();//�����Ϸ�Ƿ����
				
				return true;
			}
		});
	}

	/**
	 * �����Ϸ�Ƿ����
	 */
	public void checkIsWinOrLose(){
		if(maxValue == 2048){//�ж���Ϸ�Ƿ��Ѿ��ɹ�
			winGroup.show();//��ʾ�ɹ�����
		}else if(isGameLose() == true){
			loseGroup.show();
		}
	}
	
	
	/**
	 * �ж��Ƿ�Խ��
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean checkBound(int i,int j){
		if(i < 0 || i >= 4 || j < 0 || j >= 4){
			return false;//��ʾ����Խ��
		}
		return true;//��ʾ�����Ϸ�,��δԽ��
	}
	
	/**
	 * �ж��Ƿ��ܷ����ϲ��¼�
	 * @return
	 */
	public boolean canMoved(){
		int i;
		int j;
		int k;
		for(i = 0 ; i < 4 ; ++i){//�������еķ�������
			for(j = 0 ; j < 4 ; ++j){
				for(k = 0 ; k < 4 ; ++k){//���ڱ���ÿ�����������ϡ��¡������ĸ������Ƿ��ܹ������ϲ�
					if(checkBound(i + direction[k][0], j + direction[k][1]) == true){
						//�жϵ�ǰ���������������������ҵķ������ӵķ�ֵ�Ƿ�һ��
						if(board[i][j].getValue() == board[i + direction[k][0]][j + direction[k][1]].getValue()){
							//�����ֵһ��
							return true;//����true,��ʾ���ܷ����ϲ��¼�
						}
					}
				}
			}
		}
		
		//��ʾ�Ѿ����ܷ����ϲ��¼���
		return false;
	}
	
	/**
	 * �ж���Ϸ�Ƿ��Ѿ�����
	 * @return
	 */
	public boolean isGameLose(){
		if(emptyCellCount > 0 || canMoved() == true){
			return false;
		}
		
		//����ո�����==0 && ���ܷ����ϲ�,����Ϸ����
		return true;
	}
	
	/**
	 * ���·���
	 */
	public void updateFenshu(){
		//���µ�ǰ����
		fenshuLabel.setText(Score.instance.getScore() + "");
		fenshuLabel.setPosition(291 - fenshuLabel.getTextBounds().width/2, 692);
		
		//������߷���
		maxFenshuLabel.setText(Score.instance.getBest() + "");
		maxFenshuLabel.setPosition(403 - maxFenshuLabel.getTextBounds().width/2, 692);
	}
	
	public void addListenerOnExitBtn() {
		exitImage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.exit();// libGDX�ṩ���˳���Ϸ�ķ���

				return true;
			}
		});
	}

	/**
	 * ��new��ť���ʱ���������¿�ʼ��Ϸ
	 */
	public void addListenerOnNewBtn() {
		newImage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				System.out.println("--------->click new");
				restartGame();// ���¿�ʼ��Ϸ

				return true;
			}
		});
	}

	/**
	 * ��ʼ��board
	 */
	private void initDataForBoard() {
		// ���������������
		for (int i = 0; i < 2; i++) {
			addCell();
		}
		 Score.instance.setScore(0);// ����Ϸ�������ó�0
		 updateFenshu();
		 
		 //�½�����1024�ķ����������ڲ���
//		 addCell(1024, 3, 1);
//		 addCell(1024, 3, 0);
//		 addCell(2, 1, 1);
//		 addCell(4, 1, 0);

		// isMove = false;
		// state = GameState.PLAYING;// ����Ϸ��״̬���ó�PLAYING״̬
		 maxValue = 0;// ����Ϸ����߷������ó�0
	}

	/**
	 * ��ʼ��board���ڵ�һ�ν�����Ϸ || �˳���Ϸ���ٽ�����Ϸ
	 */
	private void initBoard() {
		maxValue = 0;//������ֵΪ0
		
		emptyCellCount = 16;
		board = new Cell[4][4];
		if (Prefs.instance.isLaunchNewGame) {// ����ǿ����µ���Ϸ
			initDataForBoard();// ��ʼ��board
		} else {// ������ǿ�������Ϸ
			loadDataFromPrefs();// ����Pref�ָ���Ϸ����
		}
	}

	public void loadDataFromPrefs() {
		// ��ȡ����õ���Ϸ����
		String[] tmp = Prefs.instance.board.split(" ");
		for (int i = 0; i < 4; i++) {// �������еķ���
			for (int j = 0; j < 4; j++) {
				if (!tmp[i * 4 + j].equals("0")) {// �����ǰ���� != 0
					addCell(Integer.parseInt(tmp[i * 4 + j]), i, j);// ��ӷ�������
					if (Integer.parseInt(tmp[i * 4 + j]) > maxValue) {// �����ǰ�������ӵķ���
																		// >
																		// ������
						maxValue = Integer.parseInt(tmp[i * 4 + j]);// ������Ϸ���Ѿ��ﵽ�����ֵ
					}
					emptyCellCount--;// �ո��ӵ����� ��1
				}
			}
		}
	}

	
	
	/**
	 * ���board�е����з�������
	 */
	public void removeCells() {
		int i;
		int j;
		for (i = 0; i < 4; ++i) {
			for (j = 0; j < 4; ++j) {
				if (board[i][j] != null) {
					board[i][j].remove();
					board[i][j] = null;
				}
			}
		}
		
		emptyCellCount =16;
	}

	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void restartGame() {
		removeCells();// ���board�е����з�������
		initDataForBoard();
	}

	
	/**
	 * �ҵ����ʵ�λ��,��ӷ�������
	 */
	private void addCell() {
		if (emptyCellCount > 0) {// �����ǰ�Ŀո��ӵ�����>0

			/*
			 * choose random pos at null cell of board, after,we find pos in
			 * board, and random value for it call addCell(value, posX, posY) to
			 * add to board.
			 */
			int pos = MathUtils.random(emptyCellCount - 1);// ���������ӷ����ĸ��ӵ�����
			boolean findEmpty = false;// Ĭ�Ͻ� �Ƿ��ҵ����� ���Ϊfalse
			int i = 0;// ��ӷ����ĸ��ӵ�����
			int j = 0;
			int d = 0;// Ŀǰ�ո��ӵĸ���
			for (i = 0; i < 4 && !findEmpty; i++) {
				for (j = 0; j < 4 && !findEmpty; j++) {
					if (board[i][j] == null) {// �����ǰ����Ϊnull
						if (d == pos) {// �����ǰ�Ŀո��ӵĸ��� == ��ӷ����ĸ��ӵ�����
							findEmpty = true;// ���Ƿ��ҵ�Ŀ����ӱ��Ϊ true
						}
						d++;// Ŀǰ�ո��ӵĸ����� 1
					}
				}
			}
			i--;
			j--;

			int value;// ����ӵĸ��ӵķ�ֵ
			// if (MathUtils.randomBoolean(0.9f)) {
			if (MathUtils.random() < 0.9f) {// �������������� < 0.9
				value = 2;// ����ӵĸ��ӵķ�ֵΪ 2
			} else {// �������������� >= 0.9
				value = 4;// ����ӵĸ��ӵķ�ֵΪ2
			}

			addCell(value, i, j);// ����ֵΪvalue�ĸ�����ӵ�(i,j)��
			// addCell(value, 0, 0);// ����ֵΪvalue�ĸ�����ӵ�(i,j)��
			emptyCellCount--;// �ո�������1
		}
	}

	/**
	 * ����ֵΪvalue�ĸ�����ӵ�(boardX,boardY)��
	 * 
	 * @param value
	 *            ��ֵ
	 * @param boardX
	 *            ������
	 * @param boardY
	 *            ������
	 */
	private void addCell(int value, int boardX, int boardY) {
		Cell actor = new Cell(value, boardX, boardY);
		board[boardX][boardY] = actor;
		stage.addActor(actor);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * ������Ϸ����: ����������ص�����  ��  ���������
	 */
	public void saveGameData(){
		String convertBoardToString = "";
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++) {
				if (board[i][j] != null){
					convertBoardToString += (Integer.toString(board[i][j]
							.getValue())) + " ";
				}else{
					convertBoardToString += ("0 ");
				}
			}
		}
		Prefs.instance.saveBoard(convertBoardToString, false);//�����������
		Prefs.instance.saveScore();//�����������
	}
	
	@Override
	public void hide() {
		System.out.println("-------->������hide����..");
		saveGameData();
	}
	
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
		System.out.println("------>pause");
		saveGameData();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
