package com.me4502.SolarTennis.entities.components;

import com.me4502.SolarTennis.entities.Entity;

import java.util.Arrays;

public class ComponentManager {

	Component[] components = new Component[0];

	public boolean isBoundToComponent(Entity ent, Class<? extends Component> component) {

		for(Component com : components) {
			if(com.getClass().equals(component)) {
				return com.has(ent.getId());
			}
		}

		return false;
	}

	public Component getComponent(Class<? extends Component> component) {

		for(Component com : components) {
			if(com.getClass().equals(component)) {
				return com;
			}
		}

		return null;
	}

	public void registerComponent(Component component) {

		components = Arrays.copyOf(components, components.length + 1);
		components[components.length - 1] = component;
	}
}