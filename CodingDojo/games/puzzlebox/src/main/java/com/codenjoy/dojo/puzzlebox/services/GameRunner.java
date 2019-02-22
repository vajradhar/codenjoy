package com.codenjoy.dojo.puzzlebox.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.puzzlebox.client.Board;
import com.codenjoy.dojo.puzzlebox.client.ai.AISolver;
import com.codenjoy.dojo.puzzlebox.model.*;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    private final Level level;

    public GameRunner() {
        new Scores(0, settings);
        level = new LevelImpl(getMap());
    }

    protected String getMap() {
        return "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼               #            ☼" +
                "☼     ☼☼☼               0   #☼" +
                "☼     ☼                   ☼☼☼☼" +
                "☼            0               ☼" +
                "☼0           ☼☼☼☼☼☼☼☼        ☼" +
                "☼            ☼0    0☼        ☼" +
                "☼       #                    ☼" +
                "☼            ☼0    0☼        ☼" +
                "☼            ☼☼☼  ☼☼☼☼       ☼" +
                "☼     ☼        ☼  ☼☼☼#☼      ☼" +
                "☼     ☼☼☼                    ☼" +
                "☼              ☼ #           ☼" +
                "☼       #      ☼        #   ☼☼" +
                "☼              ☼#            ☼" +
                "☼              0       #☼    ☼" +
                "☼               ☼☼☼     ☼    ☼" +
                "☼                       ☼    ☼" +
                "☼                           ☼☼" +
                "☼              0             ☼" +
                "☼              ☼☼            ☼" +
                "☼   ☼           #            ☼" +
                "☼                   ☼ ☼      ☼" +
                "☼                   ☼ ☼      ☼" +
                "☼    0        ☼     ☼0☼      ☼" +
                "☼☼☼☼        ☼☼☼      ☼       ☼" +
                "☼#                           ☼" +
                "☼                           0☼" +
                "☼0  ☼  0                   0#☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";
    }

    @Override
    public GameField createGame(int levelNumber) {
        return new PuzzleBox(level, getDice());
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "puzzlebox";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return AISolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.MULTIPLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        return new Player(listener);
    }
}
