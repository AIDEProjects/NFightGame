package com.goldsprite.utils.math;

public class Rectangle {
	public Vector2 center = new Vector2();
	public Vector2 halfSize = new Vector2().set(0.5f);
	public Vector2 prevCenter = new Vector2();

	public Rectangle() {
	}

	public Rectangle(float cx, float cy, float halfWidth, float halfHeight) {
		set(cx, cy, halfWidth, halfHeight);
	}

	public static boolean checkAABBCollision(Rectangle rect, Rectangle other) {
		boolean outBound =
			rect.leftX() > other.rightX() ||
				rect.rightX() < other.leftX() ||
				rect.bottomY() > other.topY() ||
				rect.topY() < other.bottomY();
		return !outBound;
	}

	public Rectangle set(float cx, float cy, float halfWidth, float halfHeight) {
		center.set(cx, cy);
		halfSize.set(halfWidth, halfHeight);
		return this;
	}

	public boolean checkAABBCollision(Rectangle other) {
		return checkAABBCollision(this, other);
	}

	public float leftX() {
		return center.x - halfSize.x;
	}

	public float bottomY() {
		return center.y - halfSize.y;
	}

	public float rightX() {
		return center.x + halfSize.x;
	}

	public float topY() {
		return center.y + halfSize.y;
	}

	public float getLeftDownX() {
		return center.x - halfSize.x;
	}

	public float getLeftDownY() {
		return center.y - halfSize.y;
	}

	public float getCenterX() {
		return center.x;
	}

	public float getCenterY() {
		return center.y;
	}

	public float getWidth() {
		return halfSize.x * 2;
	}

	public float getHeight() {
		return halfSize.y * 2;
	}

	@Override
	public String toString() {
		return String.format("{%s, %s, %s, %s}", MathUtils.preciNum(center.x, 2), MathUtils.preciNum(center.y, 2), MathUtils.preciNum(halfSize.x, 2), MathUtils.preciNum(halfSize.y, 2));
	}

	public void setHalfSize(float halfWidth, float halfHeight) {
		this.halfSize.set(halfWidth, halfHeight);
	}
}
