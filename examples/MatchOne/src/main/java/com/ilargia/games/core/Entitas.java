package com.ilargia.games.core;

import java.util.Stack;
import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.api.*;

/**
 * ---------------------------------------------------------------------------
 * '<auto-generated>' This code was generated by CodeGeneratorApp.
 * ---------------------------------------------------------------------------
 */
public class Entitas implements IContexts {

	public InputContext input;
	public GameContext game;
	public GameStateContext gamestate;

	public Entitas() {
		input = createInputContext();
		game = createGameContext();
		gamestate = createGamestateContext();
	}

	public InputContext createInputContext() {
		return new InputContext(InputComponentIds.totalComponents, 0,
				new ContextInfo("Input", InputComponentIds.componentNames(),
						InputComponentIds.componentTypes()),
				factoryInputEntity());
	}

	public GameContext createGameContext() {
		return new GameContext(GameComponentIds.totalComponents, 0,
				new ContextInfo("Game", GameComponentIds.componentNames(),
						GameComponentIds.componentTypes()), factoryGameEntity());
	}

	public GameStateContext createGamestateContext() {
		return new GameStateContext(GamestateComponentIds.totalComponents, 0,
				new ContextInfo("GameState", GamestateComponentIds
						.componentNames(), GamestateComponentIds
						.componentTypes()),
				factoryGameStateEntity());
	}

	@Override
	public Context[] allContexts() {
		return new Context[]{input, game, gamestate};
	}

	public EntityBaseFactory<InputEntity> factoryInputEntity() {
		return () -> {
			return new InputEntity();
		};
	}

	public EntityBaseFactory<GameEntity> factoryGameEntity() {
		return () -> {
			return new GameEntity();
		};
	}

	public EntityBaseFactory<GameStateEntity> factoryGameStateEntity() {
		return () -> {
			return new GameStateEntity();
		};
	}
}