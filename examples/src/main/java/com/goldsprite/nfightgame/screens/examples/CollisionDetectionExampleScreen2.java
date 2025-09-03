package com.goldsprite.nfightgame.screens.examples;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.utils.math.Vector2;

public class CollisionDetectionExampleScreen2 extends ExampleGScreen{
	private Skin skin;

	@Override
	public String getIntroduction() {
		return "物理碰撞演示: ";
	}

	@Override
	public void create() {
		skin = GlobalAssets.getInstance().editorSkin;
	}

	@Override
	public void render(float delta) {
		super.render(delta);


	}

	public class PhysicsEngine {

		public void resolveCollision(Rectangle rectA, Rectangle rectB) {
			// 1. 计算当前帧的穿透深度
			float penetrationX = calculatePenetrationX(rectA, rectB);
			float penetrationY = calculatePenetrationY(rectA, rectB);

			// 如果 somehow 没有穿透，直接返回
			if (penetrationX <= 0 || penetrationY <= 0) {
				return;
			}

			// 2. 根据速度方向决定优先尝试哪个轴
			boolean tryYFirst = Math.abs(rectA.vel.y) > Math.abs(rectA.vel.x);

			// 3. 定义两个解决方案的变量
			Vector2 solutionX = null;
			Vector2 solutionY = null;
			boolean xAxisValid = false;
			boolean yAxisValid = false;

			// 4. 尝试解决Y轴碰撞（假设先撞Y轴）
			if (rectA.vel.y != 0) {
				solutionY = new Vector2(rectA.pos.x, 0);
				if (rectA.vel.y > 0) { // 向下运动，撞到顶部
					solutionY.y = rectB.getY() - rectA.getHeight();
				} else { // 向上运动，撞到底部
					solutionY.y = rectB.getY() + rectB.getHeight();
				}
				// 计算发生Y碰撞时刻的X位置
				float tImpactY = penetrationY / Math.abs(rectA.vel.y);
				float xAtImpactY = rectA.prevPos.x + rectA.vel.x * tImpactY;
				// 创建假想矩形进行验证
				Rectangle projectedRectY = new Rectangle(xAtImpactY, solutionY.y, rectA.getWidth(), rectA.getHeight());
				yAxisValid = checkAABBCollision(projectedRectY, rectB);
			}

			// 5. 尝试解决X轴碰撞（假设先撞X轴）
			if (rectA.vel.x != 0) {
				solutionX = new Vector2(0, rectA.pos.y);
				if (rectA.vel.x > 0) { // 向右运动，撞到左侧
					solutionX.x = rectB.getX() - rectA.getWidth();
				} else { // 向左运动，撞到右侧
					solutionX.x = rectB.getX() + rectB.getWidth();
				}
				// 计算发生X碰撞时刻的Y位置
				float tImpactX = penetrationX / Math.abs(rectA.vel.x);
				float yAtImpactX = rectA.prevPos.y + rectA.vel.y * tImpactX;
				// 创建假想矩形进行验证
				Rectangle projectedRectX = new Rectangle(solutionX.x, yAtImpactX, rectA.getWidth(), rectA.getHeight());
				xAxisValid = checkAABBCollision(projectedRectX, rectB);
			}

			// 6. 决策与响应
			if (yAxisValid && !xAxisValid) {
				// Y轴是第一接触点
				rectA.pos.y = solutionY.y;
				rectA.vel.y = 0; // 阻挡Y轴速度
			} else if (xAxisValid && !yAxisValid) {
				// X轴是第一接触点
				rectA.pos.x = solutionX.x;
				rectA.vel.x = 0; // 阻挡X轴速度
			} else if (xAxisValid && yAxisValid) {
				// 角落碰撞，选择穿透深度小的轴处理
				if (penetrationX < penetrationY) {
					rectA.pos.x = solutionX.x;
					rectA.vel.x = 0;
				} else {
					rectA.pos.y = solutionY.y;
					rectA.vel.y = 0;
				}
			} else {
				// 降级方案：通常由于速度为0或其他异常情况导致，选择穿透大的轴
				if (penetrationX > penetrationY) {
					rectA.pos.x = solutionX != null ? solutionX.x : rectA.pos.x;
					rectA.vel.x = 0;
				} else {
					rectA.pos.y = solutionY != null ? solutionY.y : rectA.pos.y;
					rectA.vel.y = 0;
				}
			}
		}

		// 更高效的X轴穿透深度计算
		private float calculatePenetrationX(Rectangle a, Rectangle b) {
			// 计算两个矩形在X轴上的重叠部分
			float overlapLeft = b.getX() - (a.getX() + a.getWidth());
			float overlapRight = (b.getX() + b.getWidth()) - a.getX();

			// 如果都是负数，说明没有重叠
			if (overlapLeft < 0 && overlapRight < 0) {
				return 0;
			}

			// 选择绝对值较小的那个作为穿透深度（因为两个值一正一负）
			return Math.min(Math.abs(overlapLeft), Math.abs(overlapRight));
		}

		// 更高效的Y轴穿透深度计算
		private float calculatePenetrationY(Rectangle a, Rectangle b) {
			// 计算两个矩形在Y轴上的重叠部分
			float overlapTop = b.getY() - (a.getY() + a.getHeight());
			float overlapBottom = (b.getY() + b.getHeight()) - a.getY();

			// 如果都是负数，说明没有重叠
			if (overlapTop < 0 && overlapBottom < 0) {
				return 0;
			}

			// 选择绝对值较小的那个作为穿透深度
			return Math.min(Math.abs(overlapTop), Math.abs(overlapBottom));
		}

		// 辅助方法：AABB碰撞检测
		private boolean checkAABBCollision(Rectangle a, Rectangle b) {
			return a.getX() < b.getX() + b.getWidth() &&
				a.getX() + a.getWidth() > b.getX() &&
				a.getY() < b.getY() + b.getHeight() &&
				a.getY() + a.getHeight() > b.getY();
		}
	}

	// 简单的矩形类，需要包含位置、速度、上一帧位置和尺寸
	class Rectangle {
		public Vector2 pos;
		public Vector2 prevPos; // 这是实现CCD的关键，需要记录上一帧的位置
		public Vector2 vel;
		private float width;
		private float height;

		public Rectangle(float x, float y, float width, float height) {
			this.pos = new Vector2(x, y);
			this.prevPos = new Vector2(x, y);
			this.width = width;
			this.height = height;
		}

		// Getter methods...
		public float getX() { return pos.x; }
		public float getY() { return pos.y; }
		public float getWidth() { return width; }
		public float getHeight() { return height; }
	}
}
