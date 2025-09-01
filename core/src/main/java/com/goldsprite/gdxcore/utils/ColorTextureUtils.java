package com.goldsprite.gdxcore.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ColorTextureUtils {

	public static Drawable createColorDrawable(Color color) {
		return new TextureRegionDrawable(createColorTextureRegion(color));
	}

	public static TextureRegion createColorTextureRegion(Color color) {
		return new TextureRegion(createColorTexture(color));
	}

	public static Texture createColorTexture(Color color) {
		Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.setColor(color);
		pm.fill();
		Texture tex = new Texture(pm);
		pm.dispose();
		return tex;
	}

}
