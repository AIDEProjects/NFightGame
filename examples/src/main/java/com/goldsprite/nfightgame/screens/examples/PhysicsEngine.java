package com.goldsprite.nfightgame.screens.examples;

import com.goldsprite.gdxcore.physics.CollInfo;
import com.goldsprite.gdxcore.physics.RectCollider;
import com.goldsprite.utils.math.Rectangle;
import com.goldsprite.utils.math.Vector2;

public class PhysicsEngine {
	private static PhysicsEngine instance;
	public static PhysicsEngine getInstance() {
		if (instance == null) {
			instance = new PhysicsEngine();
		}
		return instance;
	}

	private float calculatePenetrationX(Rectangle rectA, Rectangle rectB) {
		float minX = rectA.leftX();
		float maxX = rectA.rightX();
		float min2X = rectB.leftX();
		float max2X = rectB.rightX();

		float overlapLeft =  maxX - min2X;
		float overlapRight = minX - max2X;

		//B在A左外 或B在A右外
		if(overlapLeft < 0 || overlapRight > 0){
			return 0;
		}

		// 选择绝对值较小的那个作为穿透深度（因为两个值一正一负）
		return Math.min(Math.abs(overlapLeft), Math.abs(overlapRight));
	}

	private float calculatePenetrationY(Rectangle rectA, Rectangle rectB) {
		float minY = rectA.bottomY();
		float maxY = rectA.topY();
		float min2Y = rectB.bottomY();
		float max2Y = rectB.topY();

		float overlapBottom = maxY - min2Y;
		float overlapTop = minY - max2Y;

		if(overlapBottom < 0 || overlapTop > 0){
			return 0;
		}

		// 选择绝对值较小的那个作为穿透深度
		return Math.min(Math.abs(overlapTop), Math.abs(overlapBottom));
	}
}
