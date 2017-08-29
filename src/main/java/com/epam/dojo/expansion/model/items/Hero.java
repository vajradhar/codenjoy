package com.epam.dojo.expansion.model.items;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.joystick.MessageJoystick;
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.Forces;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.interfaces.ICell;
import com.epam.dojo.expansion.model.interfaces.IField;
import com.epam.dojo.expansion.model.interfaces.IItem;
import com.epam.dojo.expansion.services.CodeSaver;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Hero extends MessageJoystick implements Joystick, Tickable {

    public static final int INITIAL_FORCES_COUNT = 10; // TODO move to constant
    private boolean alive;
    private boolean win;
    private Integer resetToLevel;
    private int goldCount;
    private List<Forces> increase;
    private List<Forces> movements;
    private IField field;
    private Point position;

    public Hero() {
        resetFlags();
    }

    private void resetFlags() {
        increase = null;
        movements = null;
        win = false;
        resetToLevel = null;
        alive = true;
        goldCount = 0;
    }

    public void setField(IField field) {
        this.field = field;
        reset(field);
    }

    private void reset(IField field) {
        resetFlags();
        setPosition();
        field.increase(this, position.getX(), position.getY(), INITIAL_FORCES_COUNT);
        field.reset();
    }

    public void reset() {
        act(0);
    }

    public void loadLevel(int level) {
        act(0, level);
    }

    @Override
    public void act(int... p) {
        if (!alive) {
            return;
        }

        if (p.length == 0) return;

        if (p[0] == 0) {
            if (p.length == 2) {
                resetToLevel = p[1];
            } else {
                resetToLevel = -1;
            }
        }
    }

    @Override
    public void message(String command) {
        if (command.contains("$%&")) {
            parseSaveCodeMessage(command);
        } else {
            parseMoveMessage(command);
        }
    }

    private void parseMoveMessage(String json) {
        JSONObject data = new JSONObject(json);
        /* format
        {'increase':
            [
                {region:{x:3, y:0}, count:1},
                {region:{x:2, y:2}, count:2},
                {region:{x:1, y:3}, count:2},
                {region:{x:0, y:4}, count:3}
            ],
        'movements':
            [
                {region:{x:3, y:0}, direction:'right', count:1},
                {region:{x:2, y:2}, direction:'up', count:3},
                {region:{x:1, y:3}, direction:'right_down', count:20},
                {region:{x:0, y:4}, direction:'left_top', count:5}
            ]}
        */

        increase = parseForces(data.getJSONArray("increase"));
        movements = parseForces(data.getJSONArray("movements"));
    }

    @NotNull
    private List<Forces> parseForces(JSONArray data) {
        List<Forces> result = new LinkedList<>();
        for (Object element : data) {
            JSONObject json = (JSONObject) element;
            Forces forces = new Forces(json);
            result.add(forces);
        }
        return result;
    }

    private void parseSaveCodeMessage(String command) {
        try { // TODO подумать и исправить это безобразие
            String[] parts = command.split("\\|\\$\\%\\&\\|");
            String user = parts[0];
            long date = Long.valueOf(parts[1]);
            int index = Integer.valueOf(parts[2]);
            int count = Integer.valueOf(parts[3]);
            String part = parts[4];
            CodeSaver.save(user, date, index, count, part);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public void tick() {
        if (!alive) {
            return;
        }

        if (resetToLevel != null) {
            resetToLevel = null;
            reset(field);
            return;
        }

        if (increase != null || movements != null) {
            for (Forces forces : increase) {
                Point to = forces.getRegion();
                if (!field.isBarrier(to.getX(), to.getX())) {
                    field.increase(this, to.getX(), to.getX(), forces.getCount());
                }
            }

            for (Forces forces : movements) {
                Point from = forces.getRegion();
                Point to = forces.getDirection().change(from);

                if (!field.isBarrier(from.getX(), from.getX())) {
                    field.decrease(this, from.getX(), from.getX(), forces.getCount());
                    field.increase(this, to.getX(), to.getX(), forces.getCount());
                }
            }

            setPosition();
        }

        increase = null;
        movements = null;
    }

    private void setPosition() {
        if (movements != null && !movements.isEmpty()) {
            position = movements.get(movements.size() - 1).getRegion();
        } else if (increase != null && !increase.isEmpty()) {
            position = increase.get(increase.size() - 1).getRegion();
        } else {
            position = field.getStartPosition();
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setWin() {
        win = true;
    }

    public void die() {
        alive = false;
    }

    public boolean isWin() {
        return win;
    }

    public void pickUpGold() {
        goldCount++;
    }

    public int getGoldCount() {
        return goldCount;
    }

    public boolean isChangeLevel() {
        return resetToLevel != null;
    }

    public int getLevel() {
        int result = resetToLevel;
        resetToLevel = null;
        return result;
    }

    public Point getPosition() {
        return position;
    }
}
