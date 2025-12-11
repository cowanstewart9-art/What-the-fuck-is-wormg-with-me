class TicTacToe:
    def __init__(self):
        self.board = [' ' for _ in range(9)]

    def print_board(self):
        for i in range(0, 9, 3):
            print(f' {self.board[i]} | {self.board[i+1]} | {self.board[i+2]} ')
            if i < 6:
                print('---|---|---')

    def is_winner(self, player):
        win_conditions = [
            [0, 1, 2], [3, 4, 5], [6, 7, 8],
            [0, 3, 6], [1, 4, 7], [2, 5, 8],
            [0, 4, 8], [2, 4, 6]
        ]
        for condition in win_conditions:
            if all(self.board[i] == player for i in condition):
                return True
        return False

    def is_draw(self):
        return ' ' not in self.board

    def make_move(self, position, player):
        if self.board[position] == ' ':
            self.board[position] = player
            return True
        return False

    def minimax(self, is_maximizing):
        if self.is_winner('O'):
            return 1
        if self.is_winner('X'):
            return -1
        if self.is_draw():
            return 0

        if is_maximizing:
            best_score = -float('inf')
            for i in range(9):
                if self.board[i] == ' ':
                    self.board[i] = 'O'
                    score = self.minimax(False)
                    self.board[i] = ' '
                    best_score = max(score, best_score)
            return best_score
        else:
            best_score = float('inf')
            for i in range(9):
                if self.board[i] == ' ':
                    self.board[i] = 'X'
                    score = self.minimax(True)
                    self.board[i] = ' '
                    best_score = min(score, best_score)
            return best_score

    def ai_move(self):
        best_score = -float('inf')
        best_move = -1
        for i in range(9):
            if self.board[i] == ' ':
                self.board[i] = 'O'
                score = self.minimax(False)
                self.board[i] = ' '
                if score > best_score:
                    best_score = score
                    best_move = i
        self.make_move(best_move, 'O')

def main():
    game = TicTacToe()
    player = 'X'
    try:
        while True:
            game.print_board()
            if player == 'X':
                try:
                    move = int(input('Enter your move (0-8): '))
                    if 0 <= move <= 8 and game.make_move(move, player):
                        if game.is_winner(player):
                            game.print_board()
                            print(f'{player} wins!')
                            break
                        if game.is_draw():
                            game.print_board()
                            print('It\'s a draw!')
                            break
                        player = 'O'
                    else:
                        print('Invalid move. Try again.')
                except ValueError:
                    print('Invalid input. Please enter a number.')
            else:
                game.ai_move()
                if game.is_winner('O'):
                    game.print_board()
                    print('AI wins!')
                    break
                if game.is_draw():
                    game.print_board()
                    print('It\'s a draw!')
                    break
                player = 'X'
    except (KeyboardInterrupt, EOFError):
        print("\nExiting game...")

if __name__ == '__main__':
    main()
