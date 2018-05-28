package com.dekinci.eden.model.animal.ai;

import com.dekinci.eden.model.Settings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrainTest {

    @Test
    void breed() {
        Brain brain = new Brain();
        Brain brain2 = new Brain();
        Brain brain3 = brain.breed(brain2);

        assertNotEquals(brain, brain3);
        assertNotEquals(brain2, brain3);
    }

    @Test
    void makeDecision() {
    }
}