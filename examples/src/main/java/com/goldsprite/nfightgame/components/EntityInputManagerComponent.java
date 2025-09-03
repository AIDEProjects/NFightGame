package com.goldsprite.nfightgame.components;

import com.goldsprite.gdxcore.ecs.component.Component;
import com.goldsprite.nfightgame.fsm.interfaces.IEntityFsm;
import com.goldsprite.nfightgame.inputs.GameInputSystem;
import com.goldsprite.utils.math.Vector2;

import java.util.function.Consumer;

public class EntityInputManagerComponent extends Component {
	private IEntityFsm fsm;
	public boolean active = true;
	private float lastFaceX;
	private final Consumer<Vector2> onMove = vector -> {
		if(!active) return;
		System.out.println("onMove " + vector);
		int faceX = (int)Math.signum(vector.x);
		if(faceX != lastFaceX) {
			fsm.setMoveKeyProtect(false);
		}
		lastFaceX = faceX;
		fsm.setKeyDirX(vector.x);
	};
	private final Consumer<Boolean> onAttack = down -> {
		if(!active) return;
		System.out.println("onAttack_down " + down);
		fsm.setKeyAttack(down);
	};
	private final Consumer<Boolean> onJump = down -> {
		if(!active) return;
		System.out.println("onJump_down " + down);
		fsm.setKeyJump(down);
	};
	private final Consumer<Boolean> onCrouch = down -> {
		if(!active) return;
		System.out.println("onCrouch_down " + down);
		fsm.setKeyCrouch(down);
	};
	private final Consumer<Boolean> onSpeedBoost = down -> {
		if(!active) return;
		System.out.println("onSpeedBoost_down " + down);
		fsm.setKeySpeedBoost(down);
	};
	private final Consumer<Boolean> onHurt = down -> {
		if(!active) return;
		System.out.println("onHurt_down " + down);
		fsm.beHurt(5);
		fsm.setKeyHurt(down);
	};
	private final Consumer<Boolean> onRespawn = down -> {
		if(!active) return;
		System.out.println("onRespawn_down " + down);
		if(down) fsm.respawn();
	};
//	public Consumer<Boolean> onChangeRole = down -> {
//		System.out.println("ChangeRole: " + down);
//		active = this.equals(role);
//	};

	public void init() {
		GameInputSystem inputSystem = GameInputSystem.getInstance();

		inputSystem.registerActionListener(inputSystem.getInputAction("Move"), onMove, Vector2.class);
		inputSystem.registerActionListener(inputSystem.getInputAction("Attack"), onAttack, Boolean.class);
		inputSystem.registerActionListener(inputSystem.getInputAction("Jump"), onJump, Boolean.class);
		inputSystem.registerActionListener(inputSystem.getInputAction("Crouch"), onCrouch, Boolean.class);
		inputSystem.registerActionListener(inputSystem.getInputAction("SpeedBoost"), onSpeedBoost, Boolean.class);
		inputSystem.registerActionListener(inputSystem.getInputAction("Hurt"), onHurt, Boolean.class);
		inputSystem.registerActionListener(inputSystem.getInputAction("Respawn"), onRespawn, Boolean.class);
	}

	public void bindFsm(IEntityFsm fsm) {
		this.fsm = fsm;
		init();
	}
}
