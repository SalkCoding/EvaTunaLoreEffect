package net.salkcoding.evatunaloreeffect.listener;

import net.salkcoding.evatunaloreeffect.eat.ItemUsingTimer;
import net.salkcoding.evatunaloreeffect.effect.BuffEffect;
import net.salkcoding.evatunaloreeffect.effect.CommandEffect;
import net.salkcoding.evatunaloreeffect.effect.HungerEffect;
import net.salkcoding.evatunaloreeffect.effect.IEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack itemOnMain = player.getInventory().getItemInMainHand();
        ItemStack itemOnOff = player.getInventory().getItemInOffHand();

        if (itemOnMain.getType() != Material.AIR) {
            List<String> lore = itemOnMain.getLore();
            if (lore == null || lore.size() <= 0) return;

            if (ItemUsingTimer.isPlayerUsing(event.getPlayer())) {
                event.setCancelled(true);
                return;
            }

            if (!applyEffectWithLore(player, lore)) return;

            boolean isFood = !hasCommandEffect(player, lore);

            if (itemOnMain.getItemMeta() != null) {
                ItemMeta meta = itemOnMain.getItemMeta();
                if (meta.getDisplayName().isEmpty())
                    ItemUsingTimer.playerStartUsing(player, itemOnMain.getI18NDisplayName(), isFood);
                else ItemUsingTimer.playerStartUsing(player, meta.getDisplayName(), isFood);
            }
            itemOnMain.setAmount(itemOnMain.getAmount() - 1);

        } else if (itemOnOff.getType() != Material.AIR) {
            List<String> lore = itemOnOff.getLore();
            if (lore == null || lore.size() <= 0) return;

            boolean isFood = !hasCommandEffect(player, lore);

            if (ItemUsingTimer.isPlayerUsing(event.getPlayer())) {
                event.setCancelled(true);
                return;
            }

            if (!applyEffectWithLore(player, lore)) return;

            if (itemOnOff.getItemMeta() != null) {
                ItemMeta meta = itemOnOff.getItemMeta();
                if (meta.getDisplayName().isEmpty())
                    ItemUsingTimer.playerStartUsing(player, itemOnOff.getI18NDisplayName(), isFood);
                else ItemUsingTimer.playerStartUsing(player, meta.getDisplayName(), isFood);
            }
            itemOnOff.setAmount(itemOnOff.getAmount() - 1);
        } else return;
        event.setCancelled(true);
    }

    private boolean applyEffectWithLore(Player player, List<String> lore /*item lore*/) {
        List<IEffect> effectList = new ArrayList<>();
        for (String line : lore) {
            String stripLore = ChatColor.stripColor(line);
            HungerEffect hunger = new HungerEffect(player, stripLore);
            BuffEffect buff = new BuffEffect(player, stripLore);
            CommandEffect command = new CommandEffect(player, stripLore);

            if (hunger.isLoreMatch())
                effectList.add(hunger);
            else if (buff.isLoreMatch())
                effectList.add(buff);
            else if (command.isLoreMatch())
                effectList.add(command);
        }
        if (effectList.size() <= 0) return false;
        for (IEffect effect : effectList) effect.applyEffect();
        return true;
    }

    private boolean hasCommandEffect(Player player, List<String> lore) {
        for (String line : lore) {
            String stripLore = ChatColor.stripColor(line);
            CommandEffect command = new CommandEffect(player, stripLore);
            if (command.isLoreMatch())
                return true;
        }
        return false;
    }

}
