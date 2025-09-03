package com.goldsprite.infinityworld.assets;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.goldsprite.gdxcore.utils.FontUtils;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class GlobalAssets {
	//单例
	private static GlobalAssets instance;

	//静态路径
	public static String skinPath = "ui_skins/shade/skin/uiskin.json";

	//预设常量
	public static final int editorFontSize = 40;
	public static final float editorFontScl = 0.5f;
	public static final float loggerFontScl = 0.4f;
	public static final float editorSplitHandleSize = 12;
	public static final int editorWindowResizeBorderSize = 20;

	//实例资源
	public Texture cellTextures;
	public Texture avatarTex;

	public Skin defaultSkin;
	public BitmapFont font;
	public BitmapFont smallFont;

	public Skin editorSkin;
	public BitmapFont editorFont, editorFontGray;


	public GlobalAssets() {
		instance = this;

		//材质

		//默认主题
		defaultSkin = new Skin(Gdx.files.internal(skinPath));
		font = FontUtils.generate();
		smallFont = FontUtils.generate(18);

		//编辑器主题
		{
			editorFont = FontUtils.generate(editorFontSize);
			editorFont.getData().setScale(editorFontScl);
			editorFontGray = FontUtils.generate(editorFontSize);
			editorFontGray.getData().setScale(editorFontScl);
			editorFontGray.setColor(Color.GRAY);

			BitmapFont loggerFont = FontUtils.generate(editorFontSize);
			loggerFont.getData().setScale(loggerFontScl);

			editorSkin = new Skin(Gdx.files.internal(GlobalAssets.skinPath));

			Label.LabelStyle labelStyle = editorSkin.get(Label.LabelStyle.class);
			labelStyle.font = editorFont;

			Label.LabelStyle loggerLabelStyle = editorSkin.get(Label.LabelStyle.class);
			loggerLabelStyle.font = loggerFont;
			editorSkin.add("loggerLabelStyle", loggerLabelStyle);

			Label.LabelStyle titleLabelStyle = editorSkin.get("title", Label.LabelStyle.class);
			titleLabelStyle.font = editorFont;
			editorSkin.add("title", titleLabelStyle);

			Label.LabelStyle subtitleLabelStyle = editorSkin.get("subtitle", Label.LabelStyle.class);
			subtitleLabelStyle.font = smallFont;
			editorSkin.add("subtitle", subtitleLabelStyle);

			TextButton.TextButtonStyle textButtonStyle = editorSkin.get(TextButton.TextButtonStyle.class);
			textButtonStyle.font = editorFont;

			TextField.TextFieldStyle textFieldStyle = editorSkin.get(TextField.TextFieldStyle.class);
			textFieldStyle.font = editorFont;
			textFieldStyle.messageFont = editorFontGray;

			SplitPane.SplitPaneStyle splitPaneStyle = editorSkin.get("default-vertical", SplitPane.SplitPaneStyle.class);
			Drawable handle = splitPaneStyle.handle;
			handle.setMinHeight(editorSplitHandleSize);
			SplitPane.SplitPaneStyle splitPaneStyleHori = editorSkin.get("default-horizontal", SplitPane.SplitPaneStyle.class);
			Drawable handleHori = splitPaneStyleHori.handle;
			handleHori.setMinWidth(editorSplitHandleSize);

			Touchpad.TouchpadStyle touchpadStyle = editorSkin.get(Touchpad.TouchpadStyle.class);
			Drawable knob = touchpadStyle.knob;
			knob.setMinWidth(45);
			knob.setMinHeight(45);

			List.ListStyle listStyle = editorSkin.get(List.ListStyle.class);
			listStyle.font = editorFont;
		}
	}

	public static GlobalAssets getInstance() {
		return instance;
	}
}

