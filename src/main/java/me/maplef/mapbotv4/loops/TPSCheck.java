package me.maplef.mapbotv4.loops;

import me.maplef.mapbotv4.Main;
import me.maplef.mapbotv4.utils.BotOperator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class TPSCheck implements Job {
    final FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    private static int tpsLowCount = 0;
    private static boolean tpsWarnFlag = false;
    private final Long opGroup = config.getLong("op-group");
    private final double tpsThreshold = config.getDouble("tps-check.threshold");

    @Override
    public void execute(JobExecutionContext context){
        double tps = Bukkit.getServer().getTPS()[0];

        if (tps < tpsThreshold) {
            if(tpsLowCount < 2){
                BotOperator.sendGroupMessage(opGroup, String.format("检测到服务器tps已低于%.1f，当前tps: %.1f", tpsThreshold, tps));
                tpsLowCount++;
            } else {
                if(!tpsWarnFlag){
                    BotOperator.sendGroupMessage(opGroup, String.format("检测到服务器tps已低于%.1f，当前tps: %.1f", tpsThreshold, tps));
                    BotOperator.sendGroupMessage(opGroup, String.format("服务器tps已连续3分钟低于%.1f，将暂停tps告警直至tps回升至%.1f以上", tpsThreshold, tpsThreshold));
                    tpsWarnFlag = true;
                }
            }
        } else {
            tpsLowCount = 0; tpsWarnFlag = false;
        }
    }
}
