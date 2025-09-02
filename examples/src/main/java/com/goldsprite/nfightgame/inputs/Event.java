package com.goldsprite.nfightgame.inputs;

import java.util.*;
import java.util.function.Consumer;

public class Event<T> {
	private final List<Consumer<T>> listeners = new ArrayList<>();

	public void add(Consumer<T> listener) {
		listeners.add(listener);
	}

	public void remove(Consumer<T> listener) {
		listeners.remove(listener);
	}

	public void invoke(T arg) {
		for (Consumer<T> l : listeners) {
			l.accept(arg);
		}
	}
}

