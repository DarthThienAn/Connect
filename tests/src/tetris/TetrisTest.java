package com.tetris;

import android.test.ActivityInstrumentationTestCase;

import com.example.android.snake.Snake;

/**
 * Make sure that the main launcher activity opens up properly, which will be
 * verified by {@link ActivityTestCase#testActivityTestCaseSetUpProperly}.
 */
public class TetrisTest extends ActivityInstrumentationTestCase<Snake> {
  
  public TetrisTest() {
      super("com.tetris", Tetris.class);
  }
  
}
