package edu.hbuas.campustodo.service;

import edu.hbuas.campustodo.model.Task;

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

    // ---------- Issue #1 优先级筛选 ----------

    @Test
    void shouldUseMediumPriorityByDefault() {
        TaskService service = new TaskService();

        var task = service.addTask("完成需求评审");

        assertEquals(Task.Priority.MEDIUM, task.getPriority());
    }

    @Test
    void shouldFilterTasksByPriority() {
        TaskService service = new TaskService();
        var high = service.addTask("提交实验报告");
        high.setPriority(Task.Priority.HIGH);
        var medium = service.addTask("参加小组例会");
        var low = service.addTask("整理会议记录");
        low.setPriority(Task.Priority.LOW);

        var result = service.filterByPriority(Task.Priority.HIGH);

        assertEquals(1, result.size());
        assertEquals(high, result.get(0));
        assertEquals(Task.Priority.MEDIUM, medium.getPriority());
        assertEquals(3, service.listAll().size());
    }

    @Test
    void shouldReturnEmptyListWhenNoTaskMatchesPriority() {
        TaskService service = new TaskService();
        service.addTask("参加小组例会");

        var result = service.filterByPriority(Task.Priority.HIGH);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRejectNullPriority() {
        TaskService service = new TaskService();

        assertThrows(IllegalArgumentException.class,
                () -> service.filterByPriority(null));
    }
}
