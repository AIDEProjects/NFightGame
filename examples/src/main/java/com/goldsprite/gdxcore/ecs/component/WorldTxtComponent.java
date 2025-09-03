package com.goldsprite.gdxcore.ecs.component;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.goldsprite.gdxcore.utils.FontUtils;
import com.goldsprite.utils.math.Vector2;

public class WorldTxtComponent extends Component{
	private BitmapFont font;
	private String text;

	public WorldTxtComponent() {
	}

	public void initFnt(){
		font = FontUtils.generate();
	}
	public void initFnt(int fntSize){
		font = FontUtils.generate(fntSize);
	}

	public Vector2 getLeftDownPos() {
		return transform.getPosition();
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
