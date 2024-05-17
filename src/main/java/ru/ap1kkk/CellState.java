package ru.ap1kkk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CellState {
    EMPTY("-"),
    WHITE_SURROUNDED("W"),
    BLACK_SURROUNDED("B"),
    TO_BE_EATEN("E");

    private final String view;

    public static CellState fromColor(Color color) {
        if(color.equals(Color.BLACK))
            return BLACK_SURROUNDED;
        else if(color.equals(Color.WHITE))
            return WHITE_SURROUNDED;
        return EMPTY;
    }
}
