package ru.ap1kkk;

public enum Color {
    BLACK,
    WHITE;

    public Color getOpponent() {
        if(this.equals(BLACK))
            return WHITE;
        return BLACK;
    }
}
