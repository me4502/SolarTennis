package com.me4502.SolarTennis.entities.components;

import com.me4502.SolarTennis.entities.Entity;

import java.util.Arrays;

public class ComponentManager {

    private static final Component[] EMPTY_COMPONENT_ARRAY = new Component[0];

    private Component[] components = EMPTY_COMPONENT_ARRAY;

    public boolean isBoundToComponent(Entity ent, Class<? extends Component> component) {
        for (Component com : components) {
            if (com.getClass().equals(component)) {
                return com.has(ent.getId());
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> component) {
        for (Component com : components) {
            if (com.getClass().equals(component)) {
                return (T) com;
            }
        }

        return null;
    }

    public void registerComponent(Component component) {
        components = Arrays.copyOf(components, components.length + 1);
        components[components.length - 1] = component;
    }
}