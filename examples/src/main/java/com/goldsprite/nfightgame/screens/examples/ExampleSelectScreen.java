package com.goldsprite.nfightgame.screens.examples;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.gdxcore.screens.IGScreen;
import com.goldsprite.nfightgame.assets.GlobalAssets;
import com.goldsprite.nfightgame.screens.tests.TestDeltaScreen;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExampleSelectScreen extends GScreen {
	private Map<String, Class<? extends IGScreen>> screenMapping = new LinkedHashMap<String, Class<? extends IGScreen>>() {{
		put("输入管理器", InputManagerExampleScreen.class);
		put("测试delta一致性", TestDeltaScreen.class);
		put("物理碰撞演示", CollisionDetectionExampleScreen.class);
	}};
	private Skin skin;
	private Stage stage;
	private float layoutInScreenScl = 4 / 5f;
	private float layout_padding = 80;
	private float cell_margin = 30;
	private float cell_width = -1;
	private float cell_height = 50;
	private Table rootTable;


	@Override
	public void create() {
		skin = GlobalAssets.getInstance().defaultSkin;

		stage = new Stage(getViewport());
		getImp().addProcessor(stage);

		float stageWidth = stage.getWidth();
		float stageHeight = stage.getHeight();

		//创建主视图
		rootTable = new Table();
		float fix = layoutInScreenScl;
		rootTable.setSize(stageWidth * fix, stageHeight * fix);

		// 将 rootTable 居中
		Vector2 rootTablePos = new Vector2();
		rootTablePos.set(stageWidth / 2, stageHeight / 2);
		rootTablePos.sub(rootTable.getWidth() / 2, rootTable.getHeight() / 2);
		rootTable.setPosition(rootTablePos.x, rootTablePos.y);
		stage.addActor(rootTable);

		// 按钮列表
		Table buttonList = getButtonTable();

		// 将按钮列表包裹在滚动面板中
		ScrollPane scrollPane = new ScrollPane(buttonList, skin);
		scrollPane.setScrollingDisabled(true, false); // 禁用水平滚动，启用垂直滚动
		scrollPane.setFadeScrollBars(true); // 保持滚动条可见
		rootTable.add(scrollPane).expand().fill();
	}

	//返回vertical按钮列表Table
	private Table getButtonTable() {
		Table buttonTable = new Table();
		buttonTable.pad(layout_padding)//内边距
			.defaults().space(cell_margin); //默认单元格间距

		//创建按钮列表
		for (String title : screenMapping.keySet()) {
			Class<? extends IGScreen> key = screenMapping.get(title);
			TextButton button = new TextButton(title, skin);
			Cell<TextButton> cell = buttonTable.add(button);

			if (cell_width > -1) cell.width(cell_width);
			else cell.expandX().fillX();
			cell.height(cell_height);

			buttonTable.row();
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					getScreenManager().setCurScreen(key, true);
				}
			});
		}
		return buttonTable;
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		rootTable.setSize(stage.getWidth() * layoutInScreenScl, stage.getHeight() * layoutInScreenScl);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

}
