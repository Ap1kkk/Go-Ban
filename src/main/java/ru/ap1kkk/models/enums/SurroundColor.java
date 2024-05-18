package ru.ap1kkk.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SurroundColor {
    IMPOSSIBLE_TO_SURROUND("I"),
    ENEMY("E"),
    PLAYER("P");

    private final String view;

    public static String getView(SurroundColor color){
        if(color == null)
            return "-";
        return color.getView();
    }
}
