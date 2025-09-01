package com.goldsprite.gdxcore.logs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class LogViewerService implements Disposable {
	private static LogViewerService instance;
	private Color whiteColor = Color.WHITE;
	private Color greenColor = new Color(0.2f, 0.9f, 0.3f, 1);
	private Color blueColor = new Color(0, 0.8f, 1, 1);
	private Color redColor = new Color(1f, 0.3f, 0.2f, 1);

	private LoggerService loggerService;
	public LogStyleManager logStyleManager;
	private Table table;  // 通过构造函数传入 Table
	private ScrollPane scrollPane;  // 引入 ScrollPane
	private boolean autoScroll = true;  // 控制是否自动滑动到底部
	private int fntSize = 50;
	private float fntScale = 0.3f;
	private int tablePad = 10;
	private int logPad = 1;

	private Skin skin;
	private BitmapFont fnt;

	private String layout;//用于指定布局模式: Default, Window
	private Actor rootLayout;
	private float windowX, windowY;

	private Window window;


	public LogViewerService(Skin skin) {
		this(skin, 0, 0, "Default");
	}
	public LogViewerService(Skin skin, float windowX, float windowY, String layout) {
		instance = this;
		this.skin = skin;
		this.layout = layout;
		this.windowX = windowX;this.windowY = windowY;
		this.loggerService = new LoggerService();
		initLayout();
		addLogMessageToView("logViewer初始化", Color.WHITE);//防止前几行被遮挡
	}

	private void initLayout() {
		//初始化样式提供者
		this.logStyleManager = new LogStyleManager(skin, fntSize, fntScale);

		//创建table
		table = new Table(skin);
		table.top().left();
		table.pad(tablePad);

		//设置table背景
		//Drawable tableBackground = ColorTextureUtils.createColorDrawable(new Color(0.3f, 0.3f, 0.3f, 1));
		//table.setBackground(tableBackground);
		table.setBackground("list");
		table.top().left();
		table.row();

		// 创建滚动面板
		scrollPane = new ScrollPane(table);
		scrollPane.setScrollingDisabled(false, false); // 允许横向和纵向滚动
		//scrollPane.setFillParent(true); // 填充整个屏幕
		scrollPane.setCancelTouchFocus(false);//防止滑动事件阻止其他控件事件
		scrollPane.setSmoothScrolling(false); // 禁用平滑滚动动画
		rootLayout = scrollPane;

		if("Window".equals(layout)){
			//将视图添加到窗口
			window = new Window("Console Window", skin);
			window.setSize(400, 300);
			window.setMovable(true);
			window.setResizable(true);
			//window.row();//防止拖动窗口时误触滑动
			window.add(scrollPane).expand().fill();
			rootLayout = window;

			//logWindow展开收起
			LogViewerService logViewer = this;
			Window logWindow = logViewer.getWindowLayout();
			logWindow.setPosition(windowX, windowY, Align.topLeft);
			logWindow.addListener(
				new InputListener(){
					boolean isExpend = true;
					boolean dragged;
					float oldSizeY;
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						//isUIEvent[0] = true;
						event.stop();
						dragged = false;
						return true;
					}
					public void touchDragged(InputEvent event, float x, float y, int pointer) {
						dragged = true;
					}
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						//isUIEvent[0] = false;
						//expendLogic
						if(!dragged){
							if(isExpend)oldSizeY = logWindow.getHeight();
							Actor hitActor = logWindow.hit(x, y, false);
							String actorName = hitActor.getClass().getSimpleName();
							//按下标题栏
							if("Window".equals(actorName)){
								isExpend = !isExpend;
								float expendY = isExpend ?oldSizeY :logWindow.getTitleLabel().getHeight();
								float topY=logWindow.getY(Align.top);
								logWindow.setSize(logWindow.getWidth(), expendY);
								logWindow.setPosition(logWindow.getX(Align.left), topY, Align.topLeft);
							}
						}
					}
				}
			);
		}
	}

	public static void log(String message, Object... args) {
		if(getInstance() == null) return;
		message = String.format(message, args);
		getInstance().loggerService.log(message, args);
		getInstance().addLogMessageToView("-NONE: "+message, getInstance().whiteColor);  // 显示 NONE 级别日志为白色
	}

	public static void logInfo(String message, Object... args) {
		if(getInstance() == null) return;
		message = String.format(message, args);
		getInstance().loggerService.logInfo(message, args);
		getInstance().addLogMessageToView("-INFO: "+message, getInstance().greenColor);  // 显示 INFO 级别日志为绿色
	}

	public static void logDebug(String message, Object... args) {
		if(getInstance() == null) return;
		message = String.format(message, args);
		getInstance().loggerService.logDebug(message, args);
		getInstance().addLogMessageToView("DEBUG: "+message, getInstance().blueColor);  // 显示 DEBUG 级别日志为蓝色
	}

	public static void logError(String message, Object... args) {
		if(getInstance() == null) return;
		message = String.format(message, args);
		getInstance().loggerService.logError(message, args);
		getInstance().addLogMessageToView("ERROR: "+message, getInstance().redColor);  // 显示 ERROR 级别日志为红色
	}

	static int line;
	// 将日志信息与视图结合，显示日志
	private void addLogMessageToView(String message, Color color) {
		String lineStr = ""+line+++" ";
		Label.LabelStyle style = logStyleManager.getLogStyle(color);  // 获取颜色对应的样式
		Label label = new Label(lineStr+message, style);
		table.add(label).left().align(Align.left).pad(logPad).row();  // 将日志添加到 Table 中

		if (autoScroll) {
			scrollPane.layout();  // 使 ScrollPane 更新布局
			scrollPane.scrollTo(0, 0, 0, 0);  // 滚动到顶部（默认）或者底部
		}
	}

	// 获取日志消息
	public Array<String> getLogMessages() {
		return loggerService.getLogMessages();
	}

	// 清除日志消息
	public void clear() {
		getLogMessages().clear();
		table.clear();
	}

	// 设置是否自动滚动到底部
	public void setAutoScroll(boolean autoScroll) {
		this.autoScroll = autoScroll;
	}

	// 获取 ScrollPane
	public ScrollPane getScrollPane() {
		return scrollPane;
	}

	public Actor getRootLayout(){
		return rootLayout;
	}

	public Window getWindowLayout(){
		return window;
	}

	@Override
	public void dispose() {
		fnt.dispose();
		skin.dispose();
	}

	public static LogViewerService getInstance(){
		return instance;
	}
}
