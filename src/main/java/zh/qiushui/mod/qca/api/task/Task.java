package zh.qiushui.mod.qca.api.task;

import lombok.Getter;
import zh.qiushui.mod.qca.Qca;
import zh.qiushui.mod.qca.QcaServerRules;

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
        if (QcaServerRules.qcaDebugLog && this.getInfo() != null) {
            Qca.LOGGER.debug("Task {} successfully run.", this.getInfo());
        }
    }
}
