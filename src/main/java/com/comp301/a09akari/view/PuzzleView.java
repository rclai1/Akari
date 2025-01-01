package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.CellType;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.Puzzle;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class PuzzleView implements FXComponent {
  private final Model model;
  private final ClassicMvcController controller;

  public PuzzleView(Model model, ClassicMvcController controller) {
    this.model = model;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    GridPane grid = new GridPane();
    grid.getStyleClass().add("grid");
    grid.getChildren().clear();
    Puzzle puzzle = model.getActivePuzzle();

    for (int r = 0; r < puzzle.getHeight(); r++) {
      for (int c = 0; c < puzzle.getWidth(); c++) {
        Button button = new Button();
        int lastR = r;
        int lastC = c;
        if (puzzle.getCellType(r, c) == CellType.CORRIDOR) {
          ImageView lBulb = new ImageView(new Image("light-bulb.png"));
          ImageView rBulb = new ImageView(new Image("red-bulb.png"));
          if (model.isLamp(r, c)) {
            button.setMaxSize(50, 50);
            button.setMinSize(10, 10);
            if (model.isLampIllegal(r, c)) {
              button.setId("illegalB");
              rBulb.setFitHeight(30);
              rBulb.setFitWidth(30);
              button.setGraphic(rBulb);
            } else {
              button.setId("legalB");
              lBulb.setFitWidth(25);
              lBulb.setFitHeight(25);
              button.setGraphic(lBulb);
            }
            grid.add(button, c, r);
          } else {
            // not lamp
            button.setMaxSize(50, 50);
            button.setPrefSize(50, 50);
            if (model.isLit(r, c)) {
              button.setId("lit");

            } else {
              button.setId("unlit");
            }
            grid.add(button, c, r);
          }
          button.setOnAction((ActionEvent event) -> controller.clickCell(lastR, lastC));
        }
        if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
          Rectangle rect = new Rectangle(50, 50);
          if (puzzle.getCellType(r, c) == CellType.WALL) {
            rect.setId("wall");
            grid.add(rect, c, r);
          } else {
            int clue = puzzle.getClue(r, c);
            String str = Integer.toString(clue);
            Label text = new Label(str);
            if (model.isClueSatisfied(r, c)) {
              text.setId("solvedClue");
            } else {
              text.setId("unsolvedClue");
            }
            grid.add(rect, c, r);
            GridPane.setHalignment(text, HPos.CENTER);
            grid.add(text, c, r);
          }
        }
      }
    }
    return grid;
  }
}
