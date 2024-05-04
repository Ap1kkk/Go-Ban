package ru.ap1kkk;

import lombok.Data;

@Data
public class Stone {
    private Color color; // Цвет камня
    private int x; // Координата x
    private int y; // Координата y

    public Stone(Color color) {
        this.color = color;
    }

    public static String getView(Stone stone) {
        if(stone == null)
            return "-";
        if(stone.getColor().equals(Color.BLACK))
            return "B";
        return "W";
    }
}

