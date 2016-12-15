package com.ilargia.games.entitas.components;

import com.badlogic.gdx.math.Shape2D;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Test"})
public class View implements IComponent {
    public Shape2D shape;

    public View(Shape2D shape) {
        this.shape = shape;
    }
}