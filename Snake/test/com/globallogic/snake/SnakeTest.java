package com.globallogic.snake;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SnakeTest {

	private static final int BOARD_SIZE = 7;
	private Board board;
	private Snake snake;
	private Stone stone;
	private Apple apple;
	
	@Before
	public void startGame() {
		startGameWithStoneAt(0, 0); // это так, чтобы камень не мешал  
	}
		
	// На поле появляется змейка 
	@Test
	public void shouldSnakeAtBoardWhenGameStart() {		
		assertNotNull("змейка должна быть", board.getSnake());
	}
	
	// змейка находится в центре экрана при старте игры
	// исправить координаты центры змейки на старте	
	@Test
	public void shouldSnakeAtCenterOfBoardWhenGameStart() {		
		assertSnakeAt(3, 3);		
	}

	/**
	 * Метод, который проверит, что голова змейки находится в конкретной позиции доски.
	 * @param x координата X
	 * @param y координата Y
	 */
	private void assertSnakeAt(int x, int y) {
		assertEquals("позиция X змейки", x, snake.getX());
		assertEquals("позиция Y змейки", y, snake.getY());
	}
	
	// теперь мне надо воспользоваться методом триангуляции и сделать так, чтобы змейка 
	// появлялась не в позиции 4,4 а все таки в центре доски игральной
	@Test
	public void shouldSnakeAtCenterOfSmallBoardWhenGameStart() {
		board = new Board(3); // тут немного неуклюже, а потому я отсавлю TODO и потом подумаю как это исправить
		snake = board.getSnake();
		
		assertSnakeAt(1, 1);		
	}
	
	// Змейка размером в две клеточки. 
	@Test
	public void shouldSnakeLengthIs2WhenStartGame() {
		assertSnakeSize(2);
	}

	/**
	 * Метод проверит, что змейка длинной в две клеточки. 
	 * @param expectedLength ожидаемая ждлинна змейки
	 */
	private void assertSnakeSize(int expectedLength) {
		assertEquals("длинна змейки", expectedLength, snake.getLength());		
	}
	
	// Поле имеет квадрутную форму, кратную двум + 1. 
	// Тут просто, если мы зададим размер поля какой-то другой, то будет исключение
	@Test(expected = IllegalArgumentException.class)
	public void shouldExceptionWhenBadBoardSize() {
		new Board(4);
	}
	
	// Направление движеня змейки изначально в право.
	@Test
	public void shouldSnakeHasRightDirectionWhenGameStart() {
		assertSnakeDirection(Direction.RIGHT); 		
	}

	/**
	 * Метод проверяет куда направлена змейка сейчас. 
	 */
	private void assertSnakeDirection(Direction expectedDirection) {
		assertEquals("направление движения змейки", 
				expectedDirection, snake.getDirection());		
	}
	
	// Поле содержит один камень для начала. 
	@Test
	public void shouldBoardContainStoneWhenGameStart() {
		Stone stone = board.getStone();
		assertNotNull("Поле должно содержать камень", stone);	
	}
		
	// камень будет (при каждом обращении к нему через доску) 
	// иметь разные координаты что недопустимо 
	@Test
	public void shouldSnakeAtOnePositionDurringOnegame() {
		assertSame(stone, board.getStone()); // если объект один и тот же, то и будет там те же координаты
	}
		
	// при перемещении в право не должна меняться Y координата 
	@Test
	public void shouldChangeXPositionWhenTurnRight() {
		int oldX = snake.getX();
		
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X после перемещения должна увеличиться", oldX + 1, newX);
	}
	
	// при перемещении вправо не должна меняться Y координата
	@Test
	public void shouldNotChangeYPositionWhenTurnRight() {
		int oldY = snake.getY();
		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y после перемещения не должна меняться", oldY, newY);
	}
	
	// Управлять змейкой можно движениями влево, вправо, вверх, вниз и ускорение. 
	// Ускорение можно реализовать на UI путем учеличения числа циклов в секунду, 
	// модель наша будет оперировать циклами - одним перемещением змейки) 
	// так как это модель, то тут нет никаких UI кнопок и прочих штук - реализуем 
	// один едиственный метод turnDown() который будет перемещать змейку за следующий такт вниз.
	// такты будем отсчитывать соовтествующим методом.	
	// при перемещении вниз меняется координата Y в большую сторону - это и проверяем
	@Test
	public void shouldTurnDownWhenCallSnakeDown() {
		int oldY = snake.getY();
		
		snake.turnDown();
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при повороте змейки вниз должна увеличится", oldY + 1, newY);
	}
	
	// теперь я могу проверить как змейка двигается по инерции вниз
	@Test 
	public void shouldGoDownInertia() {
		snake.turnDown();		
		board.tact();
		
		int oldY = snake.getY();		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при движении змейки вниз должна увеличится", oldY + 1, newY);
	}
	
	// проверить что при перемещении вниз координата X не меняется
	@Test
	public void shouldNotChangeXPositionWhenMoveDown() {
		int oldX = snake.getX();
		
		snake.turnDown();
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X при повороте змейки вниз не должна меняться", oldX, newX);
	}
	
    // проверить что при перемещении вверх меняется координата Y в меньшую сторону
	// координата 0,0 размещена в левом верхнем углу. Почему так? не знаю, наверное из прошлого привычка
	@Test
	public void shouldTurnUpWhenCallSnakeUp() {
		int oldY = snake.getY();
		
		snake.turnUp();
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при повороте змейки вниз должна уменьшиться", oldY - 1, newY);
	}
	
	// проверить что при перемещении вверх координата X не меняется
	@Test // как обычно - ломаем
	public void shouldNotChangeXPositionWhenMoveUp() {
		int oldX = snake.getX();
		
		snake.turnUp();
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X при повороте змейки вверх не должна меняться", oldX, newX);
	}
	
	// проверить движение вверх по инерции
	@Test 
	public void shouldGoUpInertia() {
		snake.turnUp();		
		board.tact();
		
		int oldY = snake.getY();		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при движении змейки вверх должна уменьшиться", oldY - 1, newY);
	}
	
	// При движении в противоположном направлении змейка сама себя съедает, т.е. умирает.
	@Test  
	public void shouldGameOverWhenSnakeEatItselfAtStartGame() {
		snake.turnLeft();
		
		assertGameOver();
	}
		
	/**
	 * Метод првоеряет, что игра окончена
	 */
	private void assertGameOver() {
		assertTrue("Ожидается конец игры", board.isGameOver());		
	}
	
	// Умрет - значит конец игры. Если конец игры, значит любое обращение 
	// к доске (методам доски) вызывает исключение. 
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeUpAfterGameOver() {
		killSnake();
				
		snake.turnUp();
	}
	
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeDownAfterGameOver() {
		killSnake();
		
		snake.turnDown();
	}
	
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeLeftAfterGameOver() {		
		killSnake();
		
		snake.turnLeft();
	}
	
	/**
	 * Метод убивающий змейку в начале игры
	 */
	private void killSnake() {
		snake.turnLeft();
		board.tact();
	}
	
	// проверить поворот вправо	
	@Test  
	public void shouldMoveRightWhenTurnRight() {
		snake.turnDown();
		board.tact();

		int oldX = snake.getX();
		
		snake.turnRight();
		board.tact();
				
		int newX = snake.getX();
		
		assertEquals("новая позиция по X после поворота вправо должна увеличиться", oldX + 1, newX);
		
	}
	
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeRightAfterGameOver() {
		killSnake();
		
		snake.turnRight();
	}
		
	// проверить как змея ест сама себя при движении вниз
	@Test  
	public void shouldGameOverWhenSnakeEatItselfDuringMoveDown() {
		snake.turnDown();
		board.tact();
		
		snake.turnUp();
 
		assertGameOver();
	}
	
	// проверить как змея ест сама себя при движении вверх
	@Test  
	public void shouldGameOverWhenSnakeEatItselfDuringMoveUp() {
		snake.turnUp();
		board.tact();
		
		snake.turnDown();
		
		assertGameOver();
	}
	
	// проверить как змея ест сама себя при движении влево
	@Test  
	public void shouldGameOverWhenSnakeEatItselfDuringMoveLeft() {
		snake.turnDown();
		board.tact();
		snake.turnLeft();
		board.tact();
		
		snake.turnRight();
		
		assertGameOver();
	}
	
	// проверить как змея ест сама себя при движении вправо
	@Test  
	public void shouldGameOverWhenSnakeEatItselfDuringMoveRight() {
		snake.turnDown();
		board.tact();
		snake.turnRight();
		board.tact();
				
		snake.turnLeft();
		
		assertGameOver();
	}
	
	// проверить что при перемещении влево меняется координата X  в меньшую сторону
	@Test
	public void shouldChangeXPositionWhenTurnLeft() {
		snake.turnDown();
		board.tact();
		
		int oldX = snake.getX();
		
		snake.turnLeft();
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X после перемещения влево уменьшается", oldX - 1, newX);
	}
	
	// проверить что при перемещении влево координата Y не меняется
	@Test
	public void shouldNotChangeYPositionWhenTurnLeft() {
		snake.turnDown();
		board.tact();
		
		int oldY = snake.getY();
		
		snake.turnLeft();
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y после перемещения влево не должна меняться", oldY, newY);
	} 
	
	// проверить движение влево по инерции
	@Test
	public void shouldNotChangeYPositionWhenTurnLeftInertia() {
		snake.turnDown();
		board.tact();
		snake.turnLeft();
		board.tact();
		
		int oldY = snake.getY();
		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при движении влево по инерции не должна меняться", oldY, newY);
	}
	
	@Test
	public void shouldChangeXPositionWhenTurnLeftInertia() {
		snake.turnDown();
		board.tact();
		snake.turnLeft();
		board.tact();
		
		int oldX = snake.getX();
		
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X при движении влево по инерции уменьшается", oldX - 1, newX);
	} 
	
	// Если змейка наткнется на камень, то она умрет. 
	// наткнуться на камень можно одним из 4 способов 
	// начнем с простого - 1) змейка движется по инерции вправо и натыкается на камень
	@Test
	public void shouldGameOverWhenEatStoneDurringMoveRight() {		
		startGameWithStoneAt(snake.getX() + 1, snake.getY()); // прямо на пути камень		

		board.tact();

		assertGameOver(); 
	}
	
	// Если змейка наткнется на камень, то она умрет. 
	// наткнуться на камень можно одним из 4 способов
	// 2) двигаясь по инерции вниз пока не наткнется на камень
	@Test
	public void shouldGameOverWhenEatStoneDurringMoveDown() {		
		startGameWithStoneAt(snake.getX(), snake.getY() + 1); // внизу камень		
		snake.turnDown();
		
		board.tact();

		assertGameOver();
	} 
	
	// Если змейка наткнется на камень, то она умрет. 
	// наткнуться на камень можно одним из 4 способов
	// 3) двигаясь по инерции вверх пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatStoneDurringMoveUp() {		
		startGameWithStoneAt(snake.getX(), snake.getY() - 1); // вверху камень		
		snake.turnUp();
		
		board.tact();

		assertGameOver();
	} 
	
	// Если змейка наткнется на камень, то она умрет. 
	// наткнуться на камень можно одним из 4 способов
	// 4) двигаясь по инерции влево пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatStoneDurringMoveLeft() {		
		startGameWithStoneAt(snake.getX() - 1, snake.getY() + 1); // слева снизу камень		
		snake.turnDown();
		board.tact(); 
		snake.turnLeft();
		
		board.tact();

		assertGameOver();
	}
	
	/**
	 * Метод стартует игру с камнем в заданной позиции
	 * @param x позиция X камня 
	 * @param y позиция Y камня 
	 */
	private void startGameWithStoneAt(int x, int y) {		 	
		startGameWith(new HaveStone(x, y));		
	}
	
	/**
	 * Метод стартует игру с яблоком в заданной позиции
	 * @param x позиция X яблока 
	 * @param y позиция Y яблока 
	 */
	private void startGameWithAppleAt(int x, int y) {		 	
		startGameWith(new HaveApple(x, y));		
	}
	
	/**
	 * Метод стартует игру с моковым генератором камней
	 * @param mockedStoneGenerator моковый генератор камней
	 */
	private void startGameWith(ArtifactGenerator generator) {
		board = new Board(generator, BOARD_SIZE);
		snake = board.getSnake();
		stone = board.getStone();
		apple = board.getApple();
	}
	
	class HaveApple implements ArtifactGenerator {
			
		private int x;
		private int y;

		public HaveApple(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public Stone generateStone(Snake snake, int boardSize) {
			return new Stone(-1, -1);
		}

		@Override
		public Apple generateApple(Snake snake, int boardSize) {
			return new Apple(x, y);
		}
	}
	
	class HaveStone implements ArtifactGenerator {
		
		private int x;
		private int y;

		public HaveStone(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public Stone generateStone(Snake snake, int boardSize) {
			return new Stone(x, y);
		}

		@Override
		public Apple generateApple(Snake snake, int boardSize) {
			return new Apple(-1, -1);
		}
	}
	
	// если змейка наткнется на одну из 4х стен, то она умрет. 
	// насткнуться на стену она может одним из 12 способов:
	// 1) двигаясь по инерции влево пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatWallDurringMoveLeft() {				
		snake.turnDown();
		board.tact();
		snake.turnLeft();
		
		board.tact();
		board.tact();
		board.tact();
		board.tact();			

		assertGameOver();
	}
	
	
	// если змейка наткнется на одну из 4х стен, то она умрет. 
	// насткнуться на стену она может одним из 12 способов:
	// 2) двигаясь по инерции вниз пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatWallDurringMoveDown() {				
		snake.turnDown();		
		
		board.tact();
		board.tact();
		board.tact();
		board.tact();

		assertGameOver();
	}
	
	// если змейка наткнется на одну из 4х стен, то она умрет. 
	// насткнуться на стену она может одним из 12 способов:
	// 3) двигаясь по инерции вверх пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatWallDurringMoveUp() {				
		snake.turnUp();		
		
		board.tact(); 
		board.tact();
		board.tact();
		board.tact(); 					

		assertGameOver();
	}	
	
	// если змейка наткнется на одну из 4х стен, то она умрет. 
	// насткнуться на стену она может одним из 12 способов:
	// 4) двигаясь по инерции вправо пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatWallDurringMoveRight() {							
		board.tact();
		board.tact();
		board.tact();	
		board.tact();

		assertGameOver();
	}	
	
	// проверить что нельзя больше вызывать tact когда игра закончена
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryTotactAfterGameOver() {
		killSnake();
		
		board.tact();
	}
	
	// яблоко может появиться в любом месте поля
	@Test
	public void shouldBoardContainAppleWhenGameStart() {
		Apple apple = board.getApple();
		assertNotNull("Поле должно содержать яблоко", apple);	
	}
	
	// после съедения яблока появляется тут же другое яблоко.
	@Test
	public void shouldAppearNewAppleWhenEatApple() {
		int appleX = snake.getX() + 1;
		int appleY = snake.getY();
		startGameWithAppleAt(appleX, appleY); // на пути змейки есть яблоко
		board.tact();		
		
		Apple newApple = board.getApple();
		assertEquals(appleX, newApple.getX());
		assertEquals(appleY, newApple.getY());
	}
	
	// Если змейка съест сама себя - она умрет. 
	// Тут надо, чтобы змейка была нормальной длинны, чтобы иметь возможность съесть себя за хвост.
	
	// На поле случайным образом во времени и пространстве появляются яблоки.
	
	// Змейка может съесть яблоки и при этом ее длинна увеличится на 1. 
		
	// яблоко не может появиться на змейке
	
	// яблоко не может появиться на камне	
	
	// Размер поля можно задать в начале игры.
	// Этот тест уже реализован, но все же пока оставим его. 
	// Тут я бы проверил, что размеры поля все же имеют те значения, что мы задали, 
	// а для этого надо научиться перемещать змейку и после перемещения спрашивать где 
	// она оказывается. Она должна быть во всех координатах от 1 до N (по X и Y), где 
	// N - размер поля. 
	
	
}
