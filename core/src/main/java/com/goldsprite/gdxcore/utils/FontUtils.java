package com.goldsprite.gdxcore.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontUtils {
	public static String fnt1Path = "ZhengQingKeZhiYaTiRegular-2.ttf";
	public static String fnt2Path = "NotoSansMonoCJKsc-Regular.otf";
	public static String fnt3Path = "pixelFont-7-8x14-sproutLands.ttf";
	
	public static int defaultFntSize = 35;

	public static BitmapFont generate() {
		return generate(defaultFntSize);
	}

	public static BitmapFont generate(int fntSize) {
		// 加载字体
		String fntPath = fnt3Path;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fntPath));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = fntSize; // 字体大小
		parameter.mono = false;
		parameter.incremental = true;
		parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
		return generator.generateFont(parameter);
	}
}
