package zh.qiushui.mod.qca.api.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import zh.qiushui.mod.qca.QcaExtension;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class TaskManager<I> {
    private final Set<I> identifiers = Sets.newConcurrentHashSet();
    private final LinkedList<Task> tasks = Lists.newLinkedList();

    public void runQuests() {
        for (Iterator<Task> iterator = this.tasks.iterator(); iterator.hasNext();) {
            Task task = iterator.next();
            task.run();
            iterator.remove();
        }
    }

    public void register(I identifier) {
        this.identifiers.add(identifier);
    }
    public void remove(I identifier) {
        this.provideQuest(identifier, new Task(
                () -> this.identifiers.remove(identifier), "Remove Identifier %s".formatted(identifier)
        ));
    }

    public void provideQuest(I identifier, Runnable function) {
        this.provideQuest(identifier, new Task(function, ""));
    }
    public <R> void provideQuest(I identifier, Callable<R> function, Consumer<R> consumer) {
        this.provideQuest(identifier, new Task(() -> {
            try {
                consumer.accept(function.call());
            } catch (Exception e) {
                QcaExtension.LOGGER.error("Task didn't run successfully.", e);
            }
        }, ""));
    }
    public void provideQuest(I identifier, BooleanSupplier function) {
        this.provideQuest(identifier, new Task(function::getAsBoolean, ""));
    }
    public void provideQuest(I identifier, Task task) {
        if (this.identifiers.contains(identifier)) {
            this.tasks.offer(task);
        }
    }
}
