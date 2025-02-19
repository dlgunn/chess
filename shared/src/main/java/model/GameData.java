package model;

import chess.ChessGame;

record GameData(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}
