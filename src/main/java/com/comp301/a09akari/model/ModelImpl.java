package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private final PuzzleLibrary library;
  private int activePuzzleIndex;
  private int[][] lampLocations;
  private final List<ModelObserver> observerList;

  public ModelImpl(PuzzleLibrary library) {
    // Your constructor code here
    if (library == null) {
      throw new IllegalArgumentException();
    }
    this.library = library;
    int puzzleHeight = getActivePuzzle().getHeight();
    int puzzleWidth = getActivePuzzle().getWidth();

    this.activePuzzleIndex = 0;
    this.lampLocations = new int[puzzleHeight][puzzleWidth];
    this.observerList = new ArrayList<>();
  }

  @Override
  public void addLamp(int r, int c) throws IllegalArgumentException, IndexOutOfBoundsException {
    if (r < 0 || r >= lampLocations.length || c < 0 || c >= lampLocations[0].length) {
      throw new IndexOutOfBoundsException("Index is out of bounds");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor.");
    }
    lampLocations[r][c] = 1;
    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) throws IllegalArgumentException, IndexOutOfBoundsException {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Index is out of bounds");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor.");
    }
    lampLocations[r][c] = 0;
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) throws IllegalArgumentException, IndexOutOfBoundsException {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Index is out of bounds");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor.");
    }

    if (isLamp(r, c)) {
      return true;
    }
    return checkCorridors(r, c);
  }

  @Override
  public boolean isLamp(int r, int c) throws IndexOutOfBoundsException, IllegalArgumentException {
    if (r < 0 || r >= lampLocations.length || c < 0 || c >= lampLocations[0].length) {
      throw new IndexOutOfBoundsException("Index is out of bounds");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor.");
    }
    return (lampLocations[r][c] == 1);
  }

  @Override
  public boolean isLampIllegal(int r, int c)
      throws IndexOutOfBoundsException, IllegalArgumentException {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Index is out of bounds");
    }
    if (lampLocations[r][c] != 1) {
      throw new IllegalArgumentException("Cell is not a Lamp.");
    }
    return checkCorridors(r, c);
  }

  @Override
  public Puzzle getActivePuzzle() {
    return library.getPuzzle(activePuzzleIndex);
  }

  @Override
  public int getActivePuzzleIndex() {
    return activePuzzleIndex;
  }

  @Override
  public void setActivePuzzleIndex(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= getPuzzleLibrarySize()) {
      throw new IndexOutOfBoundsException();
    }
    activePuzzleIndex = index;
    resetPuzzle();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return library.size();
  }

  @Override
  public void resetPuzzle() {
    lampLocations = new int[getActivePuzzle().getHeight()][getActivePuzzle().getWidth()];
    notifyObservers();
  }

  @Override
  public boolean isSolved() {
    for (int row = 0; row < getActivePuzzle().getHeight(); row++) {
      for (int column = 0; column < getActivePuzzle().getWidth(); column++) {
        CellType tileType = getActivePuzzle().getCellType(row, column);

        if (tileType == CellType.CORRIDOR && !isLit(row, column)) {
          return false;
        }

        if (tileType == CellType.CLUE && !isClueSatisfied(row, column)) {
          return false;
        }
        if (tileType == CellType.CORRIDOR) {
          if (isLamp(row, column) && isLampIllegal(row, column)) {
            return false;
          }
        }
      }
    }
    // notifyObservers();
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Index is out of bounds");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cell is not a Lamp.");
    }
    // logic: check each direction and count number of lamps found.
    // Then, check if lampsFound == clue type.
    int lampsFound = getLampsFound(r, c);
    return (lampsFound == getActivePuzzle().getClue(r, c));
  }

  private int getLampsFound(int r, int c) {
    // checks each direction immediately adjacent to r, c for lamps.
    int lampsFound = 0;

    int[][] direction = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    for (int[] d : direction) {
      int row = r + d[0];
      int column = c + d[1];

      if (row >= 0
          && row < lampLocations.length
          && column >= 0
          && column < lampLocations[0].length) {
        if (lampLocations[row][column] == 1) {
          lampsFound += 1;
        }
      }
    }
    return lampsFound;
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observerList.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observerList.remove(observer);
  }

  private void notifyObservers() {
    for (ModelObserver observer : observerList) {
      observer.update(this);
    }
  }

  private boolean checkCorridors(int r, int c) {
    // Logic: Look at current tile, and check in each direction until I either find a wall or a
    // lamp.
    // While my row and column are in bounds still, if I see a non-corridor, break and return false.
    // If I see a lamp first, then I return true.

    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

    for (int[] direction : directions) {
      int row = r + direction[0];
      int column = c + direction[1];

      while (row >= 0
          && row < getActivePuzzle().getHeight()
          && column >= 0
          && column < getActivePuzzle().getWidth()) {
        if (getActivePuzzle().getCellType(row, column) != CellType.CORRIDOR) {
          break;
        }
        if (lampLocations[row][column] == 1) {
          return true;
        }

        row += direction[0];
        column += direction[1];
      }
    }
    return false;
  }

  public boolean isEveryCellLit() {
    for (int row = 0; row < getActivePuzzle().getHeight(); row++) {
      for (int column = 0; column < getActivePuzzle().getWidth(); column++) {
        if (getActivePuzzle().getCellType(row, column) == CellType.CORRIDOR) {
          if (!isLit(row, column)) {
            return false;
          }
        }
      }
    }
    return true;
  }
}
