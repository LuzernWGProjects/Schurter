package ch.ice.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Bool {

	private BooleanProperty bool = new SimpleBooleanProperty(false);

	public boolean getBool() {
		return bool.get();
	}

	public void setBool(boolean booli) {
		bool.set(booli);
	}

	public BooleanProperty getBoolProp() {
		return bool;
	}
}
