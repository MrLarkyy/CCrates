package cz.larkyy.ccrates.runnables.animations;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CSGOAnimation extends BukkitRunnable {

    private int tick = 0;
    private final int length;
    private boolean done = false;
    private double delay = 0;
    private int index = 0;
    private List<ItemStack> rewards;
    private Inventory gui;
    private int nexttick;
    private int calc = 1;

    public CSGOAnimation(int length, List<ItemStack> rewards, Inventory gui) {
        this.length = 0 * 20;
        this.rewards = rewards;
        this.gui = gui;
        this.nexttick = 1;

        if (this.length == 0)
            calc = 2;
    }

    @Override
    public void run() {
        if (done)
            return;
        tick++;
        if (tick % 2 == 0 && tick < length) {
            for (int i = 9; i < 18; i++) {
                gui.setItem(i, rewards.get(i + index));
            }
            index++;
        }
        if (tick >= length && tick <= length + 40) {
            if (tick >= nexttick + length) {
                for (int i = 9; i < 18; i++) {
                    gui.setItem(i, rewards.get(i + index - 9));
                }
                index++;
                calc++;
                nexttick = nexttick + calc;
            }
        }

        if (tick > length + 40)
            cancel();

    }

}
