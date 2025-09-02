package com.goldsprite.nfightgame.ecs.components.basics;

import com.goldsprite.gdxcore.ecs.component.Component;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;
import com.goldsprite.nfightgame.inputs.InputSystem;

public class EntityInputManagerComponent extends Component {
	private IEntityFsm fsm;

	public void init() {
		InputSystem inputSystem = InputSystem.getInstance();

//		inputManager.registerKey(GameVirtualKey.UP)
//			.onDown(() -> System.out.println("上键按下"))
//			.onUp(() -> System.out.println("上键抬起"));
//
//		inputManager.registerKey(GameVirtualKey.DOWN)
//			.onDown(() -> System.out.println("下键按下"))
//			.onUp(() -> System.out.println("下键抬起"));

//		inputManager.registerActionListener(GameVirtualKey.Left)
//			.onDown(() -> fsm.setKeyDirX(-1))
//			.onUp(() -> fsm.setKeyDirX(0));
//
//		inputManager.registerKey(GameVirtualKey.Right)
//			.onDown(() -> fsm.setKeyDirX(1))
//			.onUp(() -> fsm.setKeyDirX(0));
//
//		inputManager.registerKey(GameVirtualKey.Horizontal)
//			.onDown(dir -> fsm.setKeyDirX(dir))
//			.onHold(dir -> fsm.setKeyDirX(dir))
//			.onUp(dir -> fsm.setKeyDirX(dir));
//
//		inputManager.registerKey(GameVirtualKey.Attack)
//			.onDown(() -> fsm.setKeyAttack(true))
//			.onUp(() -> fsm.setKeyAttack(false));
//
//		inputManager.registerKey(GameVirtualKey.Jump)
//			.onDown(() -> fsm.setKeyJump(true))
//			.onUp(() -> fsm.setKeyJump(false));

	}

	public void bindFsm(IEntityFsm fsm) {
		this.fsm = fsm;
		init();
	}
}
