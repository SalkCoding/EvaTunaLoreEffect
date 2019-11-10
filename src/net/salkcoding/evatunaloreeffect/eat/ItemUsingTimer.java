package net.salkcoding.evatunaloreeffect.eat;

import net.salkcoding.evatunaloreeffect.EvaTunaLoreEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class ItemUsingTimer {

    private static HashSet<UUID> usingSet = new HashSet<>();

    public static void playerStartUsing(Player player, String itemName, boolean isFood) {
        UsingTimer timer = new UsingTimer(player, itemName, isFood);
        Bukkit.getScheduler().runTaskLaterAsynchronously(EvaTunaLoreEffect.getInstance(), timer, 20);
        usingSet.add(player.getUniqueId());
    }

    public static boolean isPlayerUsing(Player player) {
        return usingSet.contains(player.getUniqueId());
    }

    synchronized static void playerFinishedUsing(Player player) {
        usingSet.remove(player.getUniqueId());
    }

}

class UsingTimer implements Runnable {

    private Player player;
    private String itemName;
    private boolean isFood;

    public UsingTimer(Player player, String itemName, boolean isFood) {
        this.player = player;
        this.itemName = itemName;
        this.isFood = isFood;
    }

    @Override
    public void run() {
        ItemUsingTimer.playerFinishedUsing(player);
        if (isFood) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
            player.sendActionBar(itemName + ChatColor.GRAY + "을/를 섭취하였습니다.");
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
            player.sendActionBar(itemName + ChatColor.GRAY + "을/를 사용하였습니다.");
        }
    }
}
