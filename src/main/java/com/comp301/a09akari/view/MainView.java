package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ControllerImpl;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelObserver;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView implements FXComponent, ModelObserver {
  private final Model model;
  private final ControllerImpl controller;
  private final Stage stage;

  public MainView(Model model, ControllerImpl controller, Stage stage) {
    this.model = model;
    this.controller = controller;
    this.stage = stage;
  }

  @Override
  public void update(Model model) {
    stage.getScene().setRoot(render());
    stage.getScene().getStylesheets().add("main.css");
  }

  @Override
  public Parent render() {
    VBox vbox = new VBox();
    vbox.getChildren().add(new TopView(model).render());
    vbox.getChildren().add(new PuzzleView(model, controller).render());
    vbox.getChildren().add(new ControlView(controller).render());
    return vbox;
  }
}
