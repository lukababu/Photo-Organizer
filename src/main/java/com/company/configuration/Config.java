package com.company.configuration;

import java.util.List;

public final class Config {
    private List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return getTasks().toString();
    }
}
