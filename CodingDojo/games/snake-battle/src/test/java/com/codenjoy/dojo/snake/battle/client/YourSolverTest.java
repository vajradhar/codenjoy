package com.codenjoy.dojo.snake.battle.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.snake.battle.model.Player;
import com.codenjoy.dojo.snake.battle.model.board.SnakeBoard;
import com.codenjoy.dojo.snake.battle.model.hero.Hero;
import com.codenjoy.dojo.snake.battle.model.level.LevelImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.codenjoy.dojo.services.Direction.RIGHT;
import static com.codenjoy.dojo.services.Direction.UP;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author K.ilya
 *         Это пример для реализации unit-тестов твоего бота
 *         Необходимо раскомментировать существующие тесты, добиться их выполнения ботом.
 *         Затем создавай свои тесты, улучшай бота и проверяй что не испортил предыдущие достижения.
 */

public class YourSolverTest {

    Solver<Board> yourSolver;
    Board b;

    private Dice dice;

    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
        yourSolver = new YourSolver(dice);
    }

    private void givenFl(String board) {
        b = new Board();
        b.forString(board);
//      System.out.println("Размер доски: " + b.size());
    }

    private void testSolution(Direction expected) {
        testSolution(expected.toString());
    }

    private void testSolution(String expected) {
        assertEquals(expected, yourSolver.get(b));
    }

    // корректный старт змейки из "стартового бокса"
    @Test
    public void startFromBox() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "→►     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");
        testSolution(RIGHT);
    }

//    // некуда поворачивать кроме как вверх
//    @Test
//    public void onlyUpTurn() {
//        givenFl("☼☼☼☼☼☼☼☼" +
//                "☼☼     ☼" +
//                "☼#     ☼" +
//                "☼☼     ☼" +
//                "☼☼ →►☼ ☼" +
//                "☼☼  ☼☼ ☼" +
//                "☼☼     ☼" +
//                "☼☼☼☼☼☼☼☼");
//        testSolution(UP);
//    }
}
