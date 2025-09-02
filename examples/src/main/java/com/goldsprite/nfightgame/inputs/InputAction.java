package com.goldsprite.nfightgame.inputs;

import com.goldsprite.gdxcore.utils.KVPair;
import com.goldsprite.utils.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InputAction {
	private KVPair<InputBinding, Boolean> isStarted = new KVPair<>(null, false);
	public InputActionContext inputActionContext = new InputActionContext();
	public Event<InputActionContext> started = new Event<>();
	public Event<InputActionContext> performed = new Event<>();
	public Event<InputActionContext> canceled = new Event<>();
	public List<InputBinding> inputBindings = new ArrayList<>();
	private Class valueType;
	private Object value;

	public InputAction() {
	}

	public Object readValueAsObject(){
		return value;
	}
	public <T> T readValue(Class<T> type){
		return (T) value;
	}

	public void addInputBinding(InputBinding binding) {
		this.inputBindings.add(binding);
		binding.setInputAction(this);
	}

	public void setValueType(Class valueType) {
		this.valueType = valueType;

		initDefaultValue(valueType);
	}

	private void initDefaultValue(Class valueType) {
		if(valueType.equals(Vector2.class)) this.value = new Vector2();
		if(valueType.equals(Boolean.class)) this.value = false;
	}

	public Class getValueType() {
		return valueType;
	}

	public boolean isStarted(){
		return isStarted.getValue();
	}
	public void setStarted(boolean started) {
		isStarted.setValue(started);
	}
	public boolean isStarted(InputBinding binding) {
		return binding.equals(isStarted.getKey()) ? isStarted.getValue() : false;
	}
	public void setStarted(InputBinding binding, boolean started) {
		isStarted.set(binding, started);
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public class InputActionContext {}
}
