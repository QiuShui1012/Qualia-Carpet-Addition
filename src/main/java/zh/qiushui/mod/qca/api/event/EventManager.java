package zh.qiushui.mod.qca.api.event;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import zh.qiushui.mod.qca.QcaExtension;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;

public class EventManager<I> {
    private final Set<I> identifiers = Sets.newConcurrentHashSet();
    private final Queue<BooleanSupplier> quests = Queues.newConcurrentLinkedQueue();

    public void runQuests() {
        for (BooleanSupplier quest : this.quests) {
            if (!quest.getAsBoolean()) {
                QcaExtension.LOGGER.warn("Quest %s didn't run successfully".formatted(quest), new QuestDidNotRunSuccessfullyWarn());
            }

            this.quests.remove(quest);
        }
    }

    public boolean register(I identifier) {
        return this.identifiers.add(identifier);
    }
    public void remove(I identifier) {
        this.identifiers.remove(identifier);
    }

    public boolean provideQuest(I identifier, Runnable quest) {
        if (this.identifiers.contains(identifier)) {
            return this.quests.add(() -> {
                quest.run();
                return true;
            });
        }

        return false;
    }
    public boolean provideQuest(I identifier, Callable<?> quest) {
        if (this.identifiers.contains(identifier)) {
            return this.quests.add(() -> {
                try {
                    quest.call();
                    return true;
                } catch (Throwable e) {
                    return false;
                }
            });
        }

        return false;
    }
    public boolean provideQuest(I identifier, BooleanSupplier quest) {
        if (this.identifiers.contains(identifier)) {
            return this.quests.add(quest);
        }

        return false;
    }

    protected static class QuestDidNotRunSuccessfullyWarn extends Exception {
        public QuestDidNotRunSuccessfullyWarn() {
            super();
        }

        public QuestDidNotRunSuccessfullyWarn(String message) {
            super(message);
        }

        public QuestDidNotRunSuccessfullyWarn(String message, Throwable cause) {
            super(message, cause);
        }

        public QuestDidNotRunSuccessfullyWarn(Throwable cause) {
            super(cause);
        }
    }
}
