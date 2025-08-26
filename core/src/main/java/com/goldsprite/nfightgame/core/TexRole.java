package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.utils.math.Vector2Int;

public class TexRole {
	public Texture roleTex;
	public TextureRegion region;
	public Vector2Int texSplit;
	public Vector2 pivotPos = new Vector2(200, 250);//锚点位置
	public Vector2Int face = new Vector2Int(1, 1);//人物朝向(-1左下1右上
	public Vector2 roleBound = new Vector2();//角色原始大小
	public float roleScl = 2f;//角色缩放
	Vector2Int footOffset = new Vector2Int(119, 36);

	public TexRole() {
		roleTex = new Texture(Gdx.files.internal("hero/hero_sheet.png"));
		texSplit = new Vector2Int(256, 256);
		region = new TextureRegion(roleTex, 0, 0, texSplit.x, texSplit.y);
	}
}
