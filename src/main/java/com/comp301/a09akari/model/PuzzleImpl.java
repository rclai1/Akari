package com.comp301.a09akari.model;

public class PuzzleImpl implements Puzzle {
  private final int[][] board;

  public PuzzleImpl(int[][] board) {
    // Your constructor code here
    if (board == null) {
      throw new IllegalArgumentException("board cannot be null.");
    }
    this.board = board;
  }

  @Override
  public int getWidth() {
    return board[0].length;
  }

  @Override
  public int getHeight() {
    return board.length;
  }

  @Override
  public CellType getCellType(int r, int c) throws IndexOutOfBoundsException {
    if (r < 0 || r >= getHeight() || c < 0 || c >= getWidth()) {
      throw new IndexOutOfBoundsException("Row or Column is out of bounds.");
    }
    if (board[r][c] < 5) {
      return CellType.CLUE;
    } else if (board[r][c] == 5) {
      return CellType.WALL;
    } else {
      return CellType.CORRIDOR;
    }
  }

  @Override
  public int getClue(int r, int c) throws IndexOutOfBoundsException, IllegalArgumentException {
    if (r < 0 || r >= getHeight() || c < 0 || c >= getWidth()) {
      throw new IndexOutOfBoundsException("Row or Column is out of bounds.");
    }
    if (getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cell is not a clue.");
    }
    return board[r][c];
  }
}
