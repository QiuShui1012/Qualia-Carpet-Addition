package zh.qiushui.mod.qca.api.task;

import lombok.Getter;
import zh.qiushui.mod.qca.QcaExtension;
import zh.qiushui.mod.qca.QcaSettings;

public class Task implements Runnable {
    @Getter
    private final String info;
    private final Runnable task;

    public Task(Runnable task, String info) {
        this.task = task;
        this.info = info;
    }

    @Override
    public void run() {
        this.task.run();
        if (QcaSettings.qcaDebugLog && this.getInfo() != null) {
            QcaExtension.LOGGER.debug("Task {} successfully run.", this.getInfo());
        }
    }
}
