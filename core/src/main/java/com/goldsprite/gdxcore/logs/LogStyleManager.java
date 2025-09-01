package com.goldsprite.gdxcore.logs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.goldsprite.gdxcore.utils.FontUtils;

import java.util.HashMap;
import java.util.Map;

public class LogStyleManager {
	private static LogStyleManager instance;
	public static LogStyleManager getInstance(){ return instance; }
	private Map<Color, Label.LabelStyle> styleCache = new HashMap<>();
	private BitmapFont font;
	private Skin skin;

	public LogStyleManager(Skin skin, int fntSize, float fntScale) {
		instance = this;
		this.skin = skin;
		font = FontUtils.generate(fntSize); // 假设你有一个 FontUtils 类来生成字体
		font.getData().setScale(fntScale);
	}

	// 获取日志样式，如果不存在就创建
	public Label.LabelStyle getLogStyle(Color color) {
		if (!styleCache.containsKey(color)) {
			createLogStyle(color);
		}
		return styleCache.get(color);
	}

	// 创建新的样式并缓存
	private void createLogStyle(Color color) {
		Label.LabelStyle labelStyle = null;
		//如果skin有该样式则设置皮肤样式，否则默认
		Label.LabelStyle skinStyle = skin.get(Label.LabelStyle.class);
		boolean hasStyle = skinStyle != null;
		if (hasStyle) labelStyle = new Label.LabelStyle(skinStyle);
		else labelStyle = new Label.LabelStyle();

		//设置字体与颜色
		labelStyle.font = font;
		labelStyle.fontColor = color;

		// 缓存该样式
		styleCache.put(color, labelStyle);
	}
}

