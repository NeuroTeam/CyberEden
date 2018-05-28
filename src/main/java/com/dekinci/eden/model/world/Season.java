package com.dekinci.eden.model.world;

import javafx.scene.effect.*;

public enum Season {
    SPRING(0, new Lighting()),
    SUMMER(1, new Bloom(1)),
    AUTUMN(2, new InnerShadow()),
    WINTER(3, new ColorAdjust(0.8, 0.8, 1, 1)),
    NOSEASON(-1, null);

    private int id;
    private Effect relatedEffect;

    Season(int id, Effect effect) {
        this.id = id;
        relatedEffect = effect;
    }

    public static Season getById(int id) {
        Season[] seasons = Season.values();
        for (Season s : seasons)
            if (s.id == id)
                return s;
        return NOSEASON;
    }

    public int getId() {
        return id;
    }

    public Effect getRelatedEffect() {
        return relatedEffect;
    }
}
