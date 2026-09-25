package edu.hbuas.campustodo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskServiceTest {

    @Test
    void shouldAddTask() {
        TaskService service = new TaskService();

        var task = service.addTask("完成需求评审");

        assertEquals(1L, task.getId());
        assertEquals("完成需求评审", task.getTitle());
        assertFalse(task.isCompleted());
        assertEquals(1, service.listAll().size());
    }

    @Test
    void shouldRejectBlankTitle() {
        TaskService service = new TaskService();

        assertThrows(IllegalArgumentException.class,
                () -> service.addTask("   "));
    }

    // ---------- Issue #2 完成任务 ----------

    @Test
    void shouldCompleteTaskById() {
        TaskService service = new TaskService();
        var task = service.addTask("提交周报");

        var completed = service.completeTask(task.getId());

        assertSame(task, completed);
        assertTrue(completed.isCompleted());
    }

    @Test
    @DisplayName("完成某个任务不影响其他任务")
    void shouldNotAffectOtherTasks() {
        TaskService service = new TaskService();
        var first = service.addTask("提交周报");
        var second = service.addTask("整理会议记录");

        service.completeTask(first.getId());

        assertTrue(first.isCompleted());
        assertFalse(second.isCompleted());
    }

    @Test
    void shouldRejectUnknownTaskId() {
        TaskService service = new TaskService();
        service.addTask("提交周报");

        assertThrows(NoSuchElementException.class,
                () -> service.completeTask(99L));
    }

    @Test
    void shouldRejectNonPositiveTaskId() {
        TaskService service = new TaskService();

        assertThrows(IllegalArgumentException.class,
                () -> service.completeTask(0L));
        assertThrows(IllegalArgumentException.class,
                () -> service.completeTask(-1L));
    }

    @Test
    @DisplayName("重复完成同一任务抛出 IllegalStateException")
    void shouldRejectCompletingTwice() {
        TaskService service = new TaskService();
        var task = service.addTask("提交周报");
        service.completeTask(task.getId());

        assertThrows(IllegalStateException.class,
                () -> service.completeTask(task.getId()));
        assertTrue(task.isCompleted());
    }
}
