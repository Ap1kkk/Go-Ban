package ru.ap1kkk.models.enums;

public enum Color {
    BLACK,
    WHITE;

    public Color getOpponent() {
        if(this.equals(BLACK))
            return WHITE;
        return BLACK;
    }
}
