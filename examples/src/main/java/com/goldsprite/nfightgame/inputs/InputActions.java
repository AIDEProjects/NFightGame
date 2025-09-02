package com.goldsprite.nfightgame.inputs;

import java.util.LinkedHashMap;
import java.util.Map;

public class InputActions {
	public Map<String, InputAction> keyActions = new LinkedHashMap<>();

	public void addAction(String name, InputAction keyAction) {
		this.keyActions.put(name, keyAction);
	}

	public InputAction getAction(String name) {
		return this.keyActions.get(name);
	}

	public InputAction handleInputActionsByKeyboard(int keyboardCode) {
		for(InputAction keyAction : this.keyActions.values()) {
			return handleInputActionByKeyboard(keyAction, keyboardCode);
		}
		return null;
	}
	public InputAction handleInputActionByKeyboard(InputAction keyAction, int keyboardCode) {
		for(InputBinding inputBinding : keyAction.inputBindings) {
			//匹配单键按键行为
			if(inputBinding instanceof KeyBinding) {
				KeyBinding keyBinding = (KeyBinding)inputBinding;
				if(keyBinding.matchesKey(keyboardCode)){
					keyAction.readValueAsObject();
					return keyAction;
				}
			}
			//匹配Vector2按键行为
			if(inputBinding instanceof Vector2KeyBinding) {
				Vector2KeyBinding vector2KeyBinding = (Vector2KeyBinding)inputBinding;
				if(vector2KeyBinding.matchesKey(keyboardCode)) return keyAction;
			}
		}
		return null;
	}
}
