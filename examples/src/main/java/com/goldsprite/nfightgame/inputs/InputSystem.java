package com.goldsprite.nfightgame.inputs;

import com.badlogic.gdx.InputAdapter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InputSystem extends InputAdapter {
	private static InputSystem instance;
	private final Map<Consumer, Object> actionValues = new LinkedHashMap<>();
	private final Map<InputAction, Boolean> pressedActions = new LinkedHashMap<>();
	private InputActions inputActions;

	public static InputSystem getInstance() {
		if (instance == null) {
			instance = new InputSystem();
		}
		return instance;
	}

	public InputActions getInputActions() {
		return inputActions;
	}

	public void setInputActions(InputActions inputActions) {
		this.inputActions = inputActions;
	}

	public <T> void registerActionListener(InputAction action, Consumer<T> listener, Class<T> valueClazz) {
		Consumer<InputAction.InputActionContext> proxy = c -> {
			T val = action.readValue(valueClazz);
			listener.accept(val);
		};
//		//log提示
//		action.started.add(c -> System.out.println("started"));
//		action.performed.add(c -> System.out.println("performed"));
//		action.canceled.add(c -> System.out.println("canceled"));
		action.started.add(proxy);
		action.performed.add(proxy);
		action.canceled.add(proxy);

		actionValues.put(listener, null);
	}

	@Override
	public boolean keyDown(int keyboardCode) {
		//遍历按键行为表的每个action中的每个binding
		for (InputAction action : inputActions.keyActions.values()) {
			for (InputBinding binding : action.inputBindings) {
				//未匹配到按键码跳过
				if (!binding.matchesKey(keyboardCode)) continue;
				//未开始则调用开始回调
				if (!action.isStarted()) {
					//更新值
					binding.updateValue();
					action.started.invoke(action.inputActionContext);
					action.setStarted(true);
				}
				//找到一组binding后打断，因为不同组映射无法一起检测：A和→就不行
				break;
			}
		}
		return super.keyDown(keyboardCode);
	}

	@Override
	public boolean keyUp(int keyboardCode) {
		//遍历按键行为表的每个action中的每个binding
		for (InputAction action : inputActions.keyActions.values()) {
			boolean isMatches = false, isActive = false;
			for (InputBinding binding : action.inputBindings) {
				//未匹配到按键码跳过
				if (!binding.matchesKey(keyboardCode)) continue;
				else isMatches = true;
				//匹配到则更新值
				if (binding.updateValue() && binding.isHold()) {
					//有更新且有输入则调用持续回调
					action.performed.invoke(action.inputActionContext);
				}
				//当仍有任意绑定有输入时记录为激活
				if (binding.isHold()) isActive = true;
			}
			//所有绑定都无输入则调用释放回调
			if (isMatches && !isActive) {
				action.canceled.invoke(action.inputActionContext);
				action.setStarted(false);
			}
		}
		return super.keyUp(keyboardCode);
	}

	public void update(float delta) {
		//遍历按键行为表的每个action中的每个binding
		for (InputAction action : inputActions.keyActions.values()) {
			for (InputBinding binding : action.inputBindings) {
				boolean isStarted = action.isStarted();
				boolean isModified = binding.updateValue();
				boolean isHold = binding.isHold();
				//虚拟触控输入
				if((binding instanceof VirtualJoystickBinding) || binding instanceof VirtualButtonBinding){
					//未开始且有更新 开始
					if (!isStarted && isModified) {
						action.setStarted(true);
						action.started.invoke(action.inputActionContext);
						break;
					}
					//已开始且无输入 取消
					if (isStarted && !isHold) {
						action.setStarted(false);
						action.canceled.invoke(action.inputActionContext);
						break;
					}
				}

				//已开始且有值变更(且有值时) 持续回调
				if (isStarted && isModified) {
					action.performed.invoke(action.inputActionContext);
					//找到一组binding后打断， 因为不同组映射无法一起检测：A和→就不行
					break;
				}
			}
		}
	}
}
