package zh.qiushui.mod.qca.api.task;

import lombok.Getter;
import zh.qiushui.mod.qca.QcaExtension;
import zh.qiushui.mod.qca.QcaSettings;

public class Task implements Runnable {
    private final Runnable task;

    public Task(Runnable task, String info) {
        this.task = task;
    }

    @Override
    public void run() {
        this.task.run();
    }
}
