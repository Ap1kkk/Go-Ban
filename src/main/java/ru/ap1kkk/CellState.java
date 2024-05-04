package ru.ap1kkk;

public enum CellState {
    EMPTY, WHITE_SURROUNDED, BLACK_SURROUNDED;

    public static CellState fromColor(Color color) {
        if(color.equals(Color.BLACK))
            return BLACK_SURROUNDED;
        else if(color.equals(Color.WHITE))
            return WHITE_SURROUNDED;
        return EMPTY;
    }

    public String getView() {
        if(this.equals(EMPTY))
            return "-";
        if(this.equals(WHITE_SURROUNDED))
            return "W";
        return "B";
    }
}
