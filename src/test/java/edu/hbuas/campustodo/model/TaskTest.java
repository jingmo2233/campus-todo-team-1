package edu.hbuas.campustodo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

    @Test
    void shouldCompleteOnlyOnce() {
        Task task = new Task(1L, "提交周报");

        task.complete();

        assertTrue(task.isCompleted());
        assertThrows(IllegalStateException.class, task::complete);
    }
}
