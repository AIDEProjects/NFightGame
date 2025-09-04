package com.goldsprite.nfightgame.screens.examples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goldsprite.gdxcore.physics.RectCollider;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.utils.math.Rectangle;
import com.goldsprite.utils.math.Vector2;

public class CollisionDetectionExampleScreen extends ExampleGScreen {
	private Skin skin;
	private Stage stage;
	private ShapeRenderer shapeRenderer;

	// 物理引擎和矩形对象
	private RectCollider collA; // 黄色矩形
	private RectCollider collB; // 蓝色矩形

	// 模拟状态
	private boolean isSimulating = false;
	private boolean isSettingMode = true;
	private float fixedDeltaTime = 1/60f; // 固定帧率
	private float accumulator = 0;

	// UI控件
	private Table inspectorTable;
	private TextButton startButton, pauseButton, stepBackButton, stepForwardButton;
	private Label positionALabel, positionBLabel, velocityALabel;
	private TextField aXField, aYField, bXField, bYField, velXField, velYField;

	// 历史记录（用于单步回退）
	private java.util.LinkedList<SimulationState> history = new java.util.LinkedList<>();
	private static final int MAX_HISTORY = 1000;

	@Override
	public String getIntroduction() {
		return "物理碰撞演示: 矩形AABB碰撞检测与响应";
	}

	private Color backColor = new Color(0.2f, 0.2f, 0.2f, 1);
	@Override
	public Color getBackColor()
	{
		return backColor;
	}

	@Override
	public void create() {
		skin = GlobalAssets.getInstance().editorSkin;
		stage = new Stage(getViewport());
		shapeRenderer = new ShapeRenderer();

		// 初始化物理引擎和矩形
//		physicsEngine = new PhysicsEngine();
		collA = new RectCollider();
		collA.bound = new Rectangle(261, 132, 25, 25); // 黄色矩形A
		collB = new RectCollider();
		collB.bound = new Rectangle(300, 200, 35, 35); // 蓝色矩形B
		collA.velocity.set(1000, 2000); // 初始速度

		// 创建UI
		createUI();

		// 保存初始状态
		saveState();
	}

	private void createUI() {
		// 主表格
		Table mainTable = new Table();
		mainTable.setFillParent(true);
		stage.addActor(mainTable);

		// 左侧为演示区域，右侧为检查器
		Table demoArea = new Table();
		inspectorTable = new Table();
		inspectorTable.defaults().pad(5);

		mainTable.add(demoArea).expand().fill();
		mainTable.add(inspectorTable).width(300).pad(10);

		// 检查器标题
		inspectorTable.add(new Label("碰撞演示检查器", skin, "title")).colspan(2);
		inspectorTable.row();

		// 矩形A位置
		inspectorTable.add(new Label("矩形A位置:", skin)).left();
		Table aPosTable = new Table();
		aXField = new TextField(String.valueOf(collA.bound.getCenterX()), skin);
		aYField = new TextField(String.valueOf(collA.bound.getCenterY()), skin);
		aPosTable.add(new Label("X:", skin)).width(20);
		aPosTable.add(aXField).width(60);
		aPosTable.add(new Label("Y:", skin)).width(20);
		aPosTable.add(aYField).width(60);
		inspectorTable.add(aPosTable);
		inspectorTable.row();

		// 矩形B位置
		inspectorTable.add(new Label("矩形B位置:", skin)).left();
		Table bPosTable = new Table();
		bXField = new TextField(String.valueOf(collB.bound.getCenterX()), skin);
		bYField = new TextField(String.valueOf(collB.bound.getCenterY()), skin);
		bPosTable.add(new Label("X:", skin)).width(20);
		bPosTable.add(bXField).width(60);
		bPosTable.add(new Label("Y:", skin)).width(20);
		bPosTable.add(bYField).width(60);
		inspectorTable.add(bPosTable);
		inspectorTable.row();

		// 矩形A速度
		inspectorTable.add(new Label("矩形A速度:", skin)).left();
		Table velTable = new Table();
		velXField = new TextField(String.valueOf(collA.velocity.x), skin);
		velYField = new TextField(String.valueOf(collA.velocity.y), skin);
		velTable.add(new Label("X:", skin)).width(20);
		velTable.add(velXField).width(60);
		velTable.add(new Label("Y:", skin)).width(20);
		velTable.add(velYField).width(60);
		inspectorTable.add(velTable);
		inspectorTable.row();

		// 应用按钮
		TextButton applyButton = new TextButton("应用设置", skin);
		applyButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				applySettings();
			}
		});
		inspectorTable.add(applyButton).colspan(2).padTop(10);
		inspectorTable.row();

		// 状态标签
		positionALabel = new Label("", skin);
		positionBLabel = new Label("", skin);
		velocityALabel = new Label("", skin);
		inspectorTable.add(new Label("当前状态", skin, "subtitle")).colspan(2).padTop(20);
		inspectorTable.row();
		inspectorTable.add(positionALabel).colspan(2).left();
		inspectorTable.row();
		inspectorTable.add(positionBLabel).colspan(2).left();
		inspectorTable.row();
		inspectorTable.add(velocityALabel).colspan(2).left();
		inspectorTable.row();

		// 控制按钮
		Table controlTable = new Table();
		controlTable.defaults().pad(2).width(80);

		startButton = new TextButton("开始", skin);
		startButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				startSimulation();
			}
		});

		pauseButton = new TextButton("暂停", skin);
		pauseButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pauseSimulation();
			}
		});

		stepBackButton = new TextButton("← 上一帧", skin);
		stepBackButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				stepBack();
				return true;
			}
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				stepBack();
			}
		});

		stepForwardButton = new TextButton("下一帧 →", skin);
		stepForwardButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				stepForward();
				return true;
			}
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				stepForward();
			}
		});

		controlTable.add(startButton);
		controlTable.add(pauseButton);
		controlTable.row();
		controlTable.add(stepBackButton);
		controlTable.add(stepForwardButton);

		inspectorTable.add(controlTable).colspan(2).padTop(20);

		// 说明文本
		Label instructions = new Label(
			"设置模式: 拖动矩形调整位置\n" +
				"播放模式: 自动模拟物理\n" +
				"暂停模式: 使用←→键单步调试", skin);
		instructions.setAlignment(Align.left);
		inspectorTable.add(instructions).colspan(2).padTop(20).left();

		// 设置输入处理器
		getImp().addProcessor(stage);
	}

	private void applySettings() {
		try {
			// 更新矩形A位置
			collA.bound.center.x = Float.parseFloat(aXField.getText());
			collA.bound.center.y = Float.parseFloat(aYField.getText());

			// 更新矩形B位置
			collB.bound.center.x = Float.parseFloat(bXField.getText());
			collB.bound.center.y = Float.parseFloat(bYField.getText());

			// 更新矩形A速度
			collA.velocity.x = Float.parseFloat(velXField.getText());
			collA.velocity.y = Float.parseFloat(velYField.getText());

			// 重置历史
			history.clear();
			saveState();

		} catch (NumberFormatException e) {
			// 处理输入格式错误
			System.err.println("输入格式错误: " + e.getMessage());
		}
	}

	private void startSimulation() {
		isSimulating = true;
		isSettingMode = false;
	}

	private void pauseSimulation() {
		isSimulating = false;
	}

	private void stepBack() {
		if (history.size() > 1) {
			history.removeLast(); // 移除当前状态
			SimulationState prevState = history.getLast();
			loadState(prevState);
		}
	}

	private void stepForward() {
		if (!isSimulating) {
			float deltaTime = fixedDeltaTime;
			updatePhysics(deltaTime);
			saveState();
		}
	}

	private void saveState() {
		if (history.size() >= MAX_HISTORY) {
			history.removeFirst();
		}
		history.add(new SimulationState(collA, collB));
	}

	private void loadState(SimulationState state) {
		collA.bound.center.set(state.aPos);
		collA.bound.prevCenter.set(state.aPrevPos);
		collA.velocity.set(state.aVel);

		collB.bound.center.set(state.bPos);
		collB.bound.prevCenter.set(state.bPrevPos);
		collB.velocity.set(state.bVel);
	}

	@Override
	public void render(float delta) {
//		// 清屏
//		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render(delta);

		// 处理输入
		handleInput();

		// 更新物理
		if (isSimulating) {
			// 固定时间步长更新
			accumulator += delta;
			while (accumulator >= fixedDeltaTime) {
				updatePhysics(fixedDeltaTime);
				accumulator -= fixedDeltaTime;
				saveState();
			}
		}

		// 绘制矩形
		drawRectangles();

		// 更新UI
		updateUI();
		stage.act(delta);
		stage.draw();
	}

	Vector2 tmpVec = new Vector2();
	private void handleInput() {
		// 在设置模式下处理拖拽
		if (/*isSettingMode && */Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Vector2 mousePos = tmpVec.set(Gdx.input.getX(), Gdx.input.getY());
			screenToWorldCoord(mousePos);
			float worldX = mousePos.x;
			float worldY = mousePos.y;
			// 检查是否点击了矩形A
			if (Math.abs(worldX - (collA.bound.getLeftDownX() + collA.bound.getWidth()/2)) < collA.bound.getWidth()/2 &&
				Math.abs(worldY - (collA.bound.getLeftDownY() + collA.bound.getHeight()/2)) < collA.bound.getHeight()/2) {
				collA.bound.center.x = worldX;
				collA.bound.center.y = worldY;
				updateUIValues();
			}

			// 检查是否点击了矩形B
			if (Math.abs(worldX - (collB.bound.getLeftDownX() + collB.bound.getWidth()/2)) < collB.bound.getWidth()/2 &&
				Math.abs(worldY - (collB.bound.getLeftDownY() + collB.bound.getHeight()/2)) < collB.bound.getHeight()/2) {
				collB.bound.center.x = worldX;
				collB.bound.center.y = worldY;
				updateUIValues();
			}
		}
	}

	private void updatePhysics(float delta) {
		// 更新矩形A速度
		collA.velocity.x = Float.parseFloat(velXField.getText());
		collA.velocity.y = Float.parseFloat(velYField.getText());

		// 保存上一帧位置
		collA.bound.prevCenter.set(collA.bound.center);
		collB.bound.prevCenter.set(collB.bound.center);

		// 更新位置
		collA.bound.center.x += collA.velocity.x * delta;
		collA.bound.center.y += collA.velocity.y * delta;

		// 检测碰撞
		if (collA.bound.checkAABBCollision(collB.bound)) {
			collA.resolveCollision(collB, delta);
		}

//		// 边界检测（可选，防止矩形跑出屏幕）
//		checkBoundaries(rectA);
//		checkBoundaries(rectB);
	}

	private void drawRectangles() {
		shapeRenderer.setProjectionMatrix(getCamera().combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		// 绘制矩形A（黄色，碰撞时变红）
		if (collA.bound.checkAABBCollision(collB.bound)) {
			shapeRenderer.setColor(Color.RED);
		} else {
			shapeRenderer.setColor(Color.YELLOW);
		}
		shapeRenderer.rect(collA.bound.getLeftDownX(), collA.bound.getLeftDownY(), collA.bound.getWidth(), collA.bound.getHeight());

		// 绘制矩形B（蓝色，碰撞时变红）
		if (collA.bound.checkAABBCollision(collB.bound)) {
			shapeRenderer.setColor(Color.RED);
		} else {
			shapeRenderer.setColor(Color.BLUE);
		}
		shapeRenderer.rect(collB.bound.getLeftDownX(), collB.bound.getLeftDownY(), collB.bound.getWidth(), collB.bound.getHeight());

		shapeRenderer.end();
	}

	private void updateUI() {
		positionALabel.setText(String.format("A: (%.1f, %.1f)", collA.bound.getCenterX(), collA.bound.getCenterY()));
		positionBLabel.setText(String.format("B: (%.1f, %.1f)", collB.bound.getCenterX(), collB.bound.getCenterY()));
		velocityALabel.setText(String.format("A速度: (%.1f, %.1f)", collA.velocity.x, collA.velocity.y));
	}

	private void updateUIValues() {
		aXField.setText(String.valueOf(collA.bound.getCenterX()));
		aYField.setText(String.valueOf(collA.bound.getCenterY()));
		bXField.setText(String.valueOf(collB.bound.getCenterX()));
		bYField.setText(String.valueOf(collB.bound.getCenterY()));
//		velXField.setText(String.valueOf(rectA.velocity.x));
//		velYField.setText(String.valueOf(rectA.velocity.y));
	}

	@Override
	public void dispose() {
		stage.dispose();
		shapeRenderer.dispose();
	}



	// 内部类：物理引擎
/*
	public class PhysicsEngine {

		public void resolveCollision(Rectangle rectA, Rectangle rectB) {
			// 1. 计算当前帧的穿透深度
			float penetrationX = calculatePenetrationX(rectA, rectB);
			float penetrationY = calculatePenetrationY(rectA, rectB);

			// 如果 somehow 没有穿透，直接返回
			if (penetrationX <= 0 || penetrationY <= 0) {
				return;
			}

			// 2. 根据速度方向决定优先尝试哪个轴
			boolean tryYFirst = Math.abs(rectA.velocity.y) > Math.abs(rectA.velocity.x);

			// 3. 定义两个解决方案的变量
			Vector2 solutionX = null;
			Vector2 solutionY = null;
			boolean xAxisValid = false;
			boolean yAxisValid = false;

			// 4. 尝试解决Y轴碰撞（假设先撞Y轴）
			if (rectA.velocity.y != 0) {
				solutionY = new Vector2(rectA.bound.pos.x, 0);
				if (rectA.velocity.y > 0) { // 向下运动，撞到顶部
					solutionY.y = rectB.bound.getY() - rectA.bound.getHeight();
				} else { // 向上运动，撞到底部
					solutionY.y = rectB.bound.getY() + rectB.bound.getHeight();
				}
				// 计算发生Y碰撞时刻的X位置
				float tImpactY = penetrationY / Math.abs(rectA.velocity.y);
				float xAtImpactY = rectA.bound.prevPos.x + rectA.velocity.x * tImpactY;
				// 创建假想矩形进行验证
				Rectangle projectedRectY = new Rectangle(xAtImpactY, solutionY.y, rectA.bound.getWidth(), rectA.bound.getHeight());
				yAxisValid = checkAABBCollision(projectedRectY, rectB);
			}

			// 5. 尝试解决X轴碰撞（假设先撞X轴）
			if (rectA.velocity.x != 0) {
				solutionX = new Vector2(0, rectA.bound.pos.y);
				if (rectA.velocity.x > 0) { // 向右运动，撞到左侧
					solutionX.x = rectB.bound.getX() - rectA.bound.getWidth();
				} else { // 向左运动，撞到右侧
					solutionX.x = rectB.bound.getX() + rectB.bound.getWidth();
				}
				// 计算发生X碰撞时刻的Y位置
				float tImpactX = penetrationX / Math.abs(rectA.velocity.x);
				float yAtImpactX = rectA.bound.prevPos.y + rectA.velocity.y * tImpactX;
				// 创建假想矩形进行验证
				Rectangle projectedRectX = new Rectangle(solutionX.x, yAtImpactX, rectA.bound.getWidth(), rectA.bound.getHeight());
				xAxisValid = checkAABBCollision(projectedRectX, rectB);
			}

			// 6. 决策与响应
			if (yAxisValid && !xAxisValid) {
				// Y轴是第一接触点
				rectA.bound.pos.y = solutionY.y;
				rectA.velocity.y = 0; // 阻挡Y轴速度
			} else if (xAxisValid && !yAxisValid) {
				// X轴是第一接触点
				rectA.bound.pos.x = solutionX.x;
				rectA.velocity.x = 0; // 阻挡X轴速度
			} else if (xAxisValid && yAxisValid) {
				// 角落碰撞，选择穿透深度小的轴处理
				if (penetrationX < penetrationY) {
					rectA.bound.pos.x = solutionX.x;
					rectA.velocity.x = 0;
				} else {
					rectA.bound.pos.y = solutionY.y;
					rectA.velocity.y = 0;
				}
			} else {
				// 降级方案：通常由于速度为0或其他异常情况导致，选择穿透大的轴
				if (penetrationX > penetrationY) {
					rectA.bound.pos.x = solutionX != null ? solutionX.x : rectA.bound.pos.x;
					rectA.velocity.x = 0;
				} else {
					rectA.bound.pos.y = solutionY != null ? solutionY.y : rectA.bound.pos.y;
					rectA.velocity.y = 0;
				}
			}
		}

		// 更高效的X轴穿透深度计算
		private float calculatePenetrationX(Rectangle a, Rectangle b) {
			// 计算两个矩形在X轴上的重叠部分
			float overlapLeft = b.getX() - (a.getX() + a.getWidth());
			float overlapRight = (b.getX() + b.getWidth()) - a.getX();

			// 如果都是负数，说明没有重叠
			if (overlapLeft < 0 && overlapRight < 0) {
				return 0;
			}

			// 选择绝对值较小的那个作为穿透深度（因为两个值一正一负）
			return Math.min(Math.abs(overlapLeft), Math.abs(overlapRight));
		}

		// 更高效的Y轴穿透深度计算
		private float calculatePenetrationY(Rectangle a, Rectangle b) {
			// 计算两个矩形在Y轴上的重叠部分
			float overlapTop = b.getY() - (a.getY() + a.getHeight());
			float overlapBottom = (b.getY() + b.getHeight()) - a.getY();

			// 如果都是负数，说明没有重叠
			if (overlapTop < 0 && overlapBottom < 0) {
				return 0;
			}

			// 选择绝对值较小的那个作为穿透深度
			return Math.min(Math.abs(overlapTop), Math.abs(overlapBottom));
		}

		// 辅助方法：AABB碰撞检测
		private boolean checkAABBCollision(Rectangle a, Rectangle b) {
			return a.getX() < b.getX() + b.getWidth() &&
				a.getX() + a.getWidth() > b.getX() &&
				a.getY() < b.getY() + b.getHeight() &&
				a.getY() + a.getHeight() > b.getY();
		}
	}

	// 内部类：矩形
	// 简单的矩形类，需要包含位置、速度、上一帧位置和尺寸
	class Rectangle {
		public Vector2 pos;
		public Vector2 prevPos; // 这是实现CCD的关键，需要记录上一帧的位置
		public Vector2 vel;
		private float width;
		private float height;

		public Rectangle(float x, float y, float width, float height) {
			this.pos = new Vector2(x, y);
			this.prevPos = new Vector2(x, y);
			this.vel = new Vector2();
			this.width = width;
			this.height = height;
		}

		// Getter methods...
		public float getX() { return pos.x; }
		public float getY() { return pos.y; }
		public float getWidth() { return width; }
		public float getHeight() { return height; }
	}

	*/
	// 内部类：模拟状态（用于历史记录）
	class SimulationState {
		Vector2 aPos, aPrevPos, aVel;
		Vector2 bPos, bPrevPos, bVel;

		SimulationState(RectCollider a, RectCollider b) {
			this.aPos = new Vector2(a.bound.center);
			this.aPrevPos = new Vector2(a.bound.prevCenter);
			this.aVel = new Vector2(a.velocity);

			this.bPos = new Vector2(b.bound.center);
			this.bPrevPos = new Vector2(b.bound.prevCenter);
			this.bVel = new Vector2(b.velocity);
		}
	}
}
