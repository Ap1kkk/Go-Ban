package ru.ap1kkk;

import java.util.List;

public record PaintedData (
        SurroundColor[][] painted,
        List<Stone> playerBoundStones
){}
