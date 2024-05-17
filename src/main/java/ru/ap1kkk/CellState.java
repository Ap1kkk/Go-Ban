package ru.ap1kkk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CellState {
    EMPTY("-"),
    WHITE_SURROUNDED("W"),
    BLACK_SURROUNDED("B"),
    WHITE_TO_BE_EATEN("w"),
    BLACK_TO_BE_EATEN("b");

    private final String view;

    public static CellState fromColor(Color color) {
        if(color.equals(Color.BLACK))
            return BLACK_SURROUNDED;
        else if(color.equals(Color.WHITE))
            return WHITE_SURROUNDED;
        return EMPTY;
    }
    public static CellState eatenFromColor(Color color) {
        if(color.equals(Color.BLACK))
            return BLACK_TO_BE_EATEN;
        else if(color.equals(Color.WHITE))
            return WHITE_TO_BE_EATEN;
        return EMPTY;
    }
}
