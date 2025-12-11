import unittest
import sys
import os

# Ensure the script can import from the same directory
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from tictactoe import TicTacToe

class TestTicTacToe(unittest.TestCase):

    def setUp(self):
        """Create a new game instance before each test."""
        self.game = TicTacToe()

    def test_is_winner(self):
        """Test all possible win conditions."""
        win_conditions = [
            [0, 1, 2], [3, 4, 5], [6, 7, 8],  # Rows
            [0, 3, 6], [1, 4, 7], [2, 5, 8],  # Columns
            [0, 4, 8], [2, 4, 6]             # Diagonals
        ]
        for player in ['X', 'O']:
            for condition in win_conditions:
                self.setUp()
                for pos in condition:
                    self.game.board[pos] = player
                self.assertTrue(self.game.is_winner(player))

    def test_is_draw(self):
        """Test the is_draw method."""
        self.game.board = ['X', 'O', 'X', 'O', 'X', 'O', 'O', 'X', 'O']
        self.assertTrue(self.game.is_draw())

    def test_make_move(self):
        """Test the make_move method."""
        self.assertTrue(self.game.make_move(0, 'X'))
        self.assertEqual(self.game.board[0], 'X')
        self.assertFalse(self.game.make_move(0, 'O'))

    def test_ai_move(self):
        """Test the ai_move method."""
        self.game.make_move(0, 'X')
        self.game.make_move(1, 'X')
        self.game.ai_move()  # AI should block at position 2
        self.assertEqual(self.game.board[2], 'O')

if __name__ == '__main__':
    unittest.main()
