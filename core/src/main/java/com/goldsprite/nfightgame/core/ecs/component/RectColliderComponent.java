/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.ecs.component;
import com.goldsprite.utils.math.*;

public class RectColliderComponent extends Component {
	private Vector2 size = new Vector2();

	public void setSize(float sizeX, float sizeY) {
		this.size.set(sizeX, sizeY);
	}

}

