package ru.ap1kkk.models;

import lombok.Data;
import ru.ap1kkk.models.enums.Color;

@Data
public class Stone {
    private Color color; // Цвет камня
    private int x; // Координата x
    private int y; // Координата y

    public Stone(Color color) {
        this.color = color;
    }

    public Stone(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public static String getView(Stone stone) {
        if(stone == null)
            return "-";
        if(stone.getColor().equals(Color.BLACK))
            return "B";
        return "W";
    }
}

