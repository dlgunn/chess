package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import server.ServerFacade;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PostLoginClient extends Client {
    private ServerFacade server;

    public PostLoginClient(ServerFacade server) {
        this.server = server;
    }


    @Override
    public String eval(String input, Repl repl) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(repl, params);
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
//                case "login" -> signIn(params);
//                case "rescue" -> rescuePet(params);
                case "list" -> listGames(params);
//                case "signout" -> signOut();
//                case "adopt" -> adoptPet(params);
//                case "adoptall" -> adoptAllPets();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String logout(Repl repl, String[] params) throws Exception {
        if (params.length == 0) {
            server.logout();
            repl.setClient(new PreLoginClient(server));
            return "You have been logged out";
        }
        throw new Exception();
    }

    private String joinGame(String[] params) throws Exception {
        if (params.length == 2) {
            ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
            GameData gameData = server.joinGame(Integer.parseInt(params[0]), color);
            printBoard(gameData.game().getBoard(), color);
            return "";
        }
        throw new Exception();
    }

    public void clear() throws Exception {
        server.clear();
    }

    private String observeGame(String[] params) throws Exception {
        if (params.length == 1) {
            GameData gameData = server.observeGame(Integer.parseInt(params[0]));
            printBoard(gameData.game().getBoard(), ChessGame.TeamColor.WHITE);
        }
        return "";
    }

    public void printBoard(ChessBoard board, ChessGame.TeamColor color) {
        int inc = 1;
        int start = 0;
        if (color == ChessGame.TeamColor.BLACK) {
            inc = -1;
            start = 9;
        }
        int end = start + inc * 8;
        for (int i = 8; i > 0; i--) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(start + inc * i, start + inc * j));
                printSquareArt(piece, start + inc * i, start + inc * j);
            }
            System.out.print(RESET_BG_COLOR + "\n");
        }
    }

    private void printSquareArt(ChessPiece piece, int row, int col) {
        if ((row + col) % 2 == 0) {
            System.out.print(SET_BG_COLOR_BLUE);
            System.out.print(SET_TEXT_COLOR_BLACK);
        } else {
            System.out.print(SET_BG_COLOR_WHITE);
            System.out.print(SET_TEXT_COLOR_BLACK);
        }

        if (piece == null) {
            System.out.print(EMPTY);
        } else {
            System.out.print(getIcon(piece));
        }
    }

    private String getIcon(ChessPiece piece) {


        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        }
    }

    private String createGame(String[] params) throws Exception {
        if (params.length == 1) {

            server.createGame(params[0]);
            return String.format("You have created a game named %s", params[0]);
        }

        throw new Exception();
    }

    private String listGames(String[] params) throws Exception {
        if (params.length == 0) {
            GameData[] games = server.listGames();
            var result = new StringBuilder();
            int i = 1;
            for (var game : games) {
                result.append(i).append(" ").append(game.gameName()).append('\n');
                ++i;
            }
            return result.toString();
        }
        throw new Exception();
    }

    public String help() {
        return """
                    Possible Commands (case insensitive)
                    - create <GAME NAME>
                    - list
                    - join <ID> [WHITE][BLACK]
                    - observe <ID>
                    - logout
                    - quit
                    - help
                    """;
    }
}
