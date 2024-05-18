package ru.ap1kkk.records;

import ru.ap1kkk.models.Stone;
import ru.ap1kkk.models.enums.SurroundColor;

import java.util.List;

public record PaintedData (
        SurroundColor[][] painted,
        List<Stone> playerBoundStones
){}
