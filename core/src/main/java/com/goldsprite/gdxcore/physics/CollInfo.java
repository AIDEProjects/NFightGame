package com.goldsprite.gdxcore.physics;

import com.goldsprite.utils.math.MathUtils;
import com.goldsprite.utils.math.Vector2;

public class CollInfo {
	public boolean isCollision;
	public Vector2 normalDirection = new Vector2();
	public float distance;
	public Vector2 newVelocity = new Vector2();

	public void set(CollInfo collInfo){
		isCollision = collInfo.isCollision;
		normalDirection.set(collInfo.normalDirection);
		distance = collInfo.distance;
		newVelocity.set(collInfo.newVelocity);
	}

	public CollInfo reset(){
		isCollision = false;
		normalDirection.set(0);
		distance = 0;
		newVelocity.set(0);
		return this;
	}

	@Override
	public String toString() {
		return String.format("{isColl: %s, 法线方向: %s, 距离: %s, 新速度: %s}", isCollision, normalDirection.isZero() ?"○" : normalDirection.getDirectionString(), MathUtils.preciNum(distance), newVelocity);
	}

}
