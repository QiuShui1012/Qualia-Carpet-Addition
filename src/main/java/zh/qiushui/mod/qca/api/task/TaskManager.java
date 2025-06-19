package zh.qiushui.mod.qca.api.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import zh.qiushui.mod.qca.Qca;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class TaskManager<I> {
    private final Set<I> identifiers = Sets.newConcurrentHashSet();
    private final LinkedList<Task> tasks = Lists.newLinkedList();

    public void runQuests() {
        for (Iterator<Task> iterator = this.tasks.iterator(); iterator.hasNext(); ) {
            Task task = iterator.next();
            task.run();
            iterator.remove();
        }
    }

    public void runQuestsIf(BooleanSupplier filter) {
        if (filter.getAsBoolean()) {
            runQuests();
        }
    }

    public boolean register(I identifier) {
        return this.identifiers.add(identifier);
    }

    public boolean registerIf(I identifier, BooleanSupplier filter) {
        return filter.getAsBoolean() && register(identifier);
    }

    public boolean remove(I identifier) {
        return this.provideQuest(
            identifier, new Task(
                () -> this.identifiers.remove(identifier), "Remove Identifier %s".formatted(identifier)
            )
        );
    }

    public boolean removeIf(I identifier, BooleanSupplier filter) {
        return filter.getAsBoolean() && remove(identifier);
    }

    public boolean provideQuest(I identifier, Runnable function) {
        return this.provideQuest(identifier, new Task(function, ""));
    }

    public <R> boolean provideQuest(I identifier, Callable<R> function, Consumer<R> consumer) {
        return this.provideQuest(
            identifier, new Task(
                () -> {
                    try {
                        consumer.accept(function.call());
                    } catch (Exception e) {
                        Qca.LOGGER.error("Task didn't run successfully.", e);
                    }
                }, ""
            )
        );
    }

    public boolean provideQuest(I identifier, BooleanSupplier function) {
        return this.provideQuest(identifier, new Task(function::getAsBoolean, ""));
    }

    public boolean provideQuest(I identifier, Task task) {
        if (this.identifiers.contains(identifier)) {
            return this.tasks.offer(task);
        }

        return false;
    }

    public boolean provideQuestIf(I identifier, Runnable function, BooleanSupplier filter) {
        return this.provideQuestIf(identifier, new Task(function, ""), filter);
    }

    public <R> boolean provideQuestIf(I identifier, Callable<R> function, Consumer<R> consumer, BooleanSupplier filter) {
        return this.provideQuestIf(
            identifier, new Task(
                () -> {
                    try {
                        consumer.accept(function.call());
                    } catch (Exception e) {
                        Qca.LOGGER.error("Task didn't run successfully.", e);
                    }
                }, ""
            ), filter
        );
    }

    public boolean provideQuestIf(I identifier, BooleanSupplier function, BooleanSupplier filter) {
        return this.provideQuestIf(identifier, new Task(function::getAsBoolean, ""), filter);
    }

    public boolean provideQuestIf(I identifier, Task task, BooleanSupplier filter) {
        return filter.getAsBoolean() && provideQuest(identifier, task);
    }
}
