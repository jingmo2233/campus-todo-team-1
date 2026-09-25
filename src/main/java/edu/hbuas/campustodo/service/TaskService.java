package edu.hbuas.campustodo.service;

import edu.hbuas.campustodo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 任务应用服务。学生将在功能分支中逐步扩展该类。
 */
public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    public Task addTask(String title) {
        Task task = new Task(nextId++, title);
        tasks.add(task);
        return task;
    }

    public List<Task> listAll() {
        return List.copyOf(tasks);
    }

    /**
     * 按优先级筛选任务。
     *
     * @param priority 目标优先级，不能为 {@code null}
     * @return 匹配的任务快照；没有匹配任务时返回空列表
     * @throws IllegalArgumentException 当 {@code priority} 为 {@code null} 时
     */
    public List<Task> filterByPriority(Task.Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("任务优先级不能为空");
        }
        return tasks.stream()
                .filter(task -> task.getPriority() == priority)
                .toList();
    }

    /**
     * 按编号完成任务。
     *
     * @param id 任务编号，必须为正数
     * @return 已完成的任务实例
     * @throws IllegalArgumentException 当 {@code id} 非正或对应任务不存在时
     * @throws IllegalStateException    当任务已完成时
     */
    public Task completeTask(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("任务编号必须为正数");
        }
        Task task = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("任务不存在: " + id));
        task.complete();
        return task;
    }
}
