package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ControllerImpl;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ControlView implements FXComponent {
  private final ControllerImpl controller;

  public ControlView(ControllerImpl controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.TOP_CENTER);
    Button next = new Button("Next Puzzle");
    Button prev = new Button("Prev Puzzle");
    Button shuffle = new Button("Shuffle");
    Button reset = new Button("↺");

    hbox.getStyleClass().add("button-panel");
    next.getStyleClass().add("puzzle-controls");
    prev.getStyleClass().add("puzzle-controls");
    shuffle.getStyleClass().add("puzzle-controls");
    reset.getStyleClass().add("reset-button");

    next.setOnAction((ActionEvent event) -> controller.clickNextPuzzle());
    prev.setOnAction((ActionEvent event) -> controller.clickPrevPuzzle());
    shuffle.setOnAction((ActionEvent event) -> controller.clickRandPuzzle());
    reset.setOnAction((ActionEvent event) -> controller.clickResetPuzzle());

    hbox.getChildren().addAll(prev, next, shuffle, reset);
    return hbox;
  }
}
