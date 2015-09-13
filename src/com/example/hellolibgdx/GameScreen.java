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

	private int emptyCellCount;// 用于记录空格子的数量
	private Cell[][] board;// board中的16个分数格子

	// 用于分值相同时的合并
	private Array<Cell> shouldRemoveSquareActors;// 需要移除的元素
	private Array<Cell> shouldDoubleSquareActors;// 需要双倍的元素

	//用于实现游戏中的分数
	LabelStyle fenshuLabelStyle;
	Label fenshuLabel;//用于显示当前分数
	Label maxFenshuLabel;//用于显示最高分数
	
	
	int maxValue;//用于记录当前达到的分数格子中的分数的最大值
	WinGroup winGroup;
	
	//---用于实现失败界面相关的变量
	int direction[][] = {//方向变量,用于判断是否还能发生合并事件
			{1,0},//向下
			{-1,0},//向上
			{0,1},//向右
			{0,-1}//向左
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

		initBoard();// 初始化board
		
		//用于分值相同时的合并
		shouldRemoveSquareActors = new Array<Cell>();
		shouldDoubleSquareActors = new Array<Cell>();
		
		addListenerOnNewBtn();// 给new按钮添加时间用于重新开始游戏
		addListenerOnExitBtn();// 给exit按钮添加监听器..

		addListenerOnStage();//给stage添加监听器,用于处理游戏的主逻辑
		
		//---------用于处理分数逻辑
		initFenshuLabel();
		addFenshuLabelToStage();
		
		//---------成功与失败界面
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
	 * 把分数相关的Label添加到舞台上
	 */
	public void addFenshuLabelToStage(){
		stage.addActor(fenshuLabel);
		stage.addActor(maxFenshuLabel);
	}
	
	/**
	 * 初始化分数相关的Label
	 */
	public void initFenshuLabel(){
		fenshuLabelStyle = new LabelStyle(Assets.font, Color.WHITE);//初始化LabelStyle
		
//		fenshuLabel = new Label("0", fenshuLabelStyle);//初始化fenshuLabel
		fenshuLabel = new Label(Score.instance.getScore() + "", fenshuLabelStyle);//初始化fenshuLabel
		fenshuLabel.setPosition(291 - fenshuLabel.getTextBounds().width/2, 692);//设置fenshuLabel,并做居中处理
		
		maxFenshuLabel = new Label(Score.instance.getBest() + "", fenshuLabelStyle);
		maxFenshuLabel.setPosition(403 - maxFenshuLabel.getTextBounds().width/2, 692);
	}

	/**
	 * 用于合并分数
	 */
	public void unionFenshu(){
		{
			// 把需要删除的分数格子删除掉
			Iterator<Cell> iters = shouldRemoveSquareActors
					.iterator();
			while (iters.hasNext()) {//遍历整个数据结构
				Cell actor = iters.next();
				stage.getRoot().removeActor(actor);//把里面的元素从wutaistage中移除
				iters.remove();//并且从数据结构中移除
			}
		}
		{
			//用于把分数格子的分数加倍
			Iterator<Cell> iters = shouldDoubleSquareActors
					.iterator();
			while (iters.hasNext()) {//便利数据结构中的所有元素
				Cell actor = iters.next();
				actor.doubleValue();//将当前分数加倍
				//判断当前分数加倍之后是否>目前为止的最大分值.如果大于,更新最大分值
				maxValue = actor.getValue() > maxValue ? actor
						.getValue() : maxValue;
				iters.remove();//并且从数据结构中移除
			}
		}
	}
	
	/**
	 * 向上移动
	 * 
	 * 需要记住board中的坐标是这样子的. boardX,boardY (0,0),(0,1) (1,0),(1,1) (2,0),(2,1)
	 * (3,0),(3,1)
	 */
	private void touchDragToUp() {
		for (int j = 0; j < 4; j++) {// 遍历每一列
			Cell lastActor = null;// 最后调整好的分数格子(调整好的的上一个元素)
			int adjustPosition = 0;// 当前正在调整的位置,默认从0开始
			for (int i = 0; i < 4; i++) {// 遍历每一行...(需要注意这个时候的一些顺序.外层是j.里层是i)
				if (board[i][j] == null) {// 如果当前的格子为null
					continue;// 继续下一次循环
				} else {// 如果当前格子不为空
					if (lastActor == null) {// 如果之前还没有调整过分数格子,这个是第一个调整的分数格子
						if (adjustPosition != i) {// 如果当前调整的分数格子不在正在调整的位置
							board[adjustPosition][j] = board[i][j];// 那么将调整好后的分数格子指向当前分数格子
							board[i][j] = null;// 将当前分数格子置为空
							board[adjustPosition][j].moveTo(adjustPosition, j,
									i - adjustPosition);// 将分数格子移动到指定位置
						}
						lastActor = board[adjustPosition][j];// 更新最后调整好的分数格子lastActor
						adjustPosition++;// 正在调整的位置的索引+1
					} else {// 如果之前已经调整过分数格子
						// 如果最后调整的分数格子的分数值 != 当前调整的分数格子的分数值.即是不需要合并
						if (lastActor.getValue() != board[i][j].getValue()) {
							if (adjustPosition != i) {// 当前调整的分数格子不在正在调整的位置上
								// 把当前调整的分数格子已到正在调整的位置上
								board[adjustPosition][j] = board[i][j];
								board[i][j] = null;
								board[adjustPosition][j].moveTo(adjustPosition,
										j, i - adjustPosition);
							}
							lastActor = board[adjustPosition][j];// 更新最后调整好的分数格子
							adjustPosition++;// 更新正在调整的位置
						} else {// 如果当前元素和上一个元素的分值一样
							// 将当前元素移动到上一个元素那里
							board[i][j].moveTo(lastActor.getBoardX(),
									lastActor.getBoardY(),
									i - lastActor.getBoardX());
							// 加上当前元素的分数
							 Score.instance.addScore(board[i][j].getValue());
							 shouldRemoveSquareActors.add(board[i][j]);//将board[i][j]添加到需要移除的链表中
							 shouldDoubleSquareActors.add(lastActor);//将lastActor添加到“分数双倍”的链表中
							emptyCellCount++;// 因为合并了,所以空格子数+1
							lastActor = null;// lastActor置为空
							board[i][j] = null;// board[i][j]置为空
						}
					}

				}
			}
		}
	}

	/**
	 * 向下移动
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
	 * 向左移动
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
	 * 向右移动
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
		stage.addListener(new InputListener() {//给stage添加监听器
			/**
			 * keycode返回的是你按下的键盘中的按键的唯一标识
			 */
			@Override
			public boolean keyDown(InputEvent event, int keycode) {

				if (keycode == Keys.UP) {//如果你按下的向上的按钮
					touchDragToUp();//那么所有的分数格子向上移动
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
				unionFenshu();//用于移动后合并分数格子
				addCell();//每次操作后添加新的分数格子
				
				updateFenshu();//更新当前分数..
				
				checkIsWinOrLose();//检查游戏是否结束
				
				return true;
			}
		});
	}

	/**
	 * 检查游戏是否结束
	 */
	public void checkIsWinOrLose(){
		if(maxValue == 2048){//判断游戏是否已经成功
			winGroup.show();//显示成功界面
		}else if(isGameLose() == true){
			loseGroup.show();
		}
	}
	
	
	/**
	 * 判断是否越界
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean checkBound(int i,int j){
		if(i < 0 || i >= 4 || j < 0 || j >= 4){
			return false;//表示索引越界
		}
		return true;//表示索引合法,并未越界
	}
	
	/**
	 * 判断是否还能发生合并事件
	 * @return
	 */
	public boolean canMoved(){
		int i;
		int j;
		int k;
		for(i = 0 ; i < 4 ; ++i){//遍历所有的分数格子
			for(j = 0 ; j < 4 ; ++j){
				for(k = 0 ; k < 4 ; ++k){//用于遍历每个分数格子上、下、左、右四个方向是否能够发生合并
					if(checkBound(i + direction[k][0], j + direction[k][1]) == true){
						//判断当前分数格子与它的上下左右的分数格子的分值是否一样
						if(board[i][j].getValue() == board[i + direction[k][0]][j + direction[k][1]].getValue()){
							//如果分值一样
							return true;//返回true,表示还能发生合并事件
						}
					}
				}
			}
		}
		
		//表示已经不能发生合并事件了
		return false;
	}
	
	/**
	 * 判断游戏是否已经结束
	 * @return
	 */
	public boolean isGameLose(){
		if(emptyCellCount > 0 || canMoved() == true){
			return false;
		}
		
		//如果空格子数==0 && 不能发生合并,则游戏结束
		return true;
	}
	
	/**
	 * 更新分数
	 */
	public void updateFenshu(){
		//更新当前分数
		fenshuLabel.setText(Score.instance.getScore() + "");
		fenshuLabel.setPosition(291 - fenshuLabel.getTextBounds().width/2, 692);
		
		//更新最高分数
		maxFenshuLabel.setText(Score.instance.getBest() + "");
		maxFenshuLabel.setPosition(403 - maxFenshuLabel.getTextBounds().width/2, 692);
	}
	
	public void addListenerOnExitBtn() {
		exitImage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.exit();// libGDX提供的退出游戏的方法

				return true;
			}
		});
	}

	/**
	 * 给new按钮添加时间用于重新开始游戏
	 */
	public void addListenerOnNewBtn() {
		newImage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				System.out.println("--------->click new");
				restartGame();// 重新开始游戏

				return true;
			}
		});
	}

	/**
	 * 初始化board
	 */
	private void initDataForBoard() {
		// 添加两个分数格子
		for (int i = 0; i < 2; i++) {
			addCell();
		}
		 Score.instance.setScore(0);// 将游戏分数设置成0
		 updateFenshu();
		 
		 //新建两个1024的分数格子用于测试
//		 addCell(1024, 3, 1);
//		 addCell(1024, 3, 0);
//		 addCell(2, 1, 1);
//		 addCell(4, 1, 0);

		// isMove = false;
		// state = GameState.PLAYING;// 将游戏的状态设置成PLAYING状态
		 maxValue = 0;// 将游戏的最高分数设置成0
	}

	/**
	 * 初始化board用于第一次进入游戏 || 退出游戏后再进入游戏
	 */
	private void initBoard() {
		maxValue = 0;//最大分数值为0
		
		emptyCellCount = 16;
		board = new Cell[4][4];
		if (Prefs.instance.isLaunchNewGame) {// 如果是开启新的游戏
			initDataForBoard();// 初始化board
		} else {// 如果不是开启新游戏
			loadDataFromPrefs();// 根据Pref恢复游戏数据
		}
	}

	public void loadDataFromPrefs() {
		// 获取保存好的游戏数据
		String[] tmp = Prefs.instance.board.split(" ");
		for (int i = 0; i < 4; i++) {// 遍历所有的分数
			for (int j = 0; j < 4; j++) {
				if (!tmp[i * 4 + j].equals("0")) {// 如果当前分数 != 0
					addCell(Integer.parseInt(tmp[i * 4 + j]), i, j);// 添加分数格子
					if (Integer.parseInt(tmp[i * 4 + j]) > maxValue) {// 如果当前分数格子的分数
																		// >
																		// 最大分数
						maxValue = Integer.parseInt(tmp[i * 4 + j]);// 更新游戏的已经达到的最大值
					}
					emptyCellCount--;// 空格子的数量 减1
				}
			}
		}
	}

	
	
	/**
	 * 清除board中的所有分数格子
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
	 * 重新开始游戏
	 */
	public void restartGame() {
		removeCells();// 清除board中的所有分数格子
		initDataForBoard();
	}

	
	/**
	 * 找到合适的位置,添加分数格子
	 */
	private void addCell() {
		if (emptyCellCount > 0) {// 如果当前的空格子的数量>0

			/*
			 * choose random pos at null cell of board, after,we find pos in
			 * board, and random value for it call addCell(value, posX, posY) to
			 * add to board.
			 */
			int pos = MathUtils.random(emptyCellCount - 1);// 随机产生添加分数的格子的索引
			boolean findEmpty = false;// 默认将 是否找到格子 标记为false
			int i = 0;// 添加分数的格子的索引
			int j = 0;
			int d = 0;// 目前空格子的个数
			for (i = 0; i < 4 && !findEmpty; i++) {
				for (j = 0; j < 4 && !findEmpty; j++) {
					if (board[i][j] == null) {// 如果当前格子为null
						if (d == pos) {// 如果当前的空格子的个数 == 添加分数的格子的索引
							findEmpty = true;// 将是否找到目标格子标记为 true
						}
						d++;// 目前空格子的个数加 1
					}
				}
			}
			i--;
			j--;

			int value;// 新添加的格子的分值
			// if (MathUtils.randomBoolean(0.9f)) {
			if (MathUtils.random() < 0.9f) {// 如果产生的随机数 < 0.9
				value = 2;// 新添加的格子的分值为 2
			} else {// 如果产生的随机数 >= 0.9
				value = 4;// 新添加的格子的分值为2
			}

			addCell(value, i, j);// 将分值为value的格子添加到(i,j)上
			// addCell(value, 0, 0);// 将分值为value的格子添加到(i,j)上
			emptyCellCount--;// 空格子数减1
		}
	}

	/**
	 * 将分值为value的格子添加到(boardX,boardY)上
	 * 
	 * @param value
	 *            分值
	 * @param boardX
	 *            行坐标
	 * @param boardY
	 *            列坐标
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
	 * 保存游戏数据: 包括分数相关的数据  和  局面的数据
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
		Prefs.instance.saveBoard(convertBoardToString, false);//保存局面数据
		Prefs.instance.saveScore();//保存分数数据
	}
	
	@Override
	public void hide() {
		System.out.println("-------->调用了hide方法..");
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
