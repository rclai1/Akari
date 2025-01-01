package com.comp301.a09akari.view;

import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelImpl;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TopView implements FXComponent {
  private final Model model;

  public TopView(Model model) {
    this.model = model;
  }

  @Override
  public Parent render() {
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    Label akari = new Label("Akari");
    akari.setId("gameTitle");
    Label currentPuzzle =
        new Label(
            "Puzzle "
                + (model.getActivePuzzleIndex() + 1)
                + " out of "
                + model.getPuzzleLibrarySize());
    Label winMessage = new Label("Congratulations! You've solved the puzzle.");
    winMessage.setId("notDone");
    winMessage.setAlignment(Pos.BOTTOM_CENTER);
    vbox.getChildren().addAll(akari, currentPuzzle, winMessage);

    if (model.isSolved()) {
      winMessage.setId("win");
    } else if (((ModelImpl) model).isEveryCellLit()) {
      winMessage.setId("notSolved");
      winMessage.setText("There are mistakes in your solution.");
    }

    return vbox;
  }
}
