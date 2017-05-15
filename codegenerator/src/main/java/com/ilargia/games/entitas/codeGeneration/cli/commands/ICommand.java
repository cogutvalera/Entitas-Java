package com.ilargia.games.entitas.codeGeneration.cli.commands;


public interface ICommand {

    String trigger();

    String description();

    String example();

    void run(String[] args);

}
