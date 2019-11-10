package net.salkcoding.evatunaloreeffect.effect;

import net.salkcoding.evatunaloreeffect.EvaTunaLoreEffect;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandEffect implements IEffect {

    //                     <Lore, Command>
    private static HashMap<String, String> commandLoreMap = new HashMap<>();

    private Player player;
    private String lore;

    private String runCommand;

    private final Pattern numberLoreRegex = Pattern.compile("<number>");
    private final Pattern numberRegex = Pattern.compile("\\d+");

    public CommandEffect(Player player, String lore) {
        this.player = player;
        this.lore = lore;
    }

    @Override
    public boolean isLoreMatch() {
        for (Map.Entry<String, String> entry : commandLoreMap.entrySet()) {
            String loreLine = entry.getKey();

            Matcher matcher = numberLoreRegex.matcher(entry.getKey());
            final boolean find = matcher.find();
            if (find) {
                loreLine = loreLine.substring(0, matcher.start());
            }
            //아이템 lore에 현재 command lore가 있는지
            if (lore.contains(loreLine)) {
                if (find) {
                    Matcher numberMatch = numberRegex.matcher(lore);
                    if (numberMatch.find()) {
                        String value = lore.substring(numberMatch.start(), numberMatch.end());
                        runCommand = entry.getValue().replaceFirst(numberLoreRegex.pattern(), value);
                    } else
                        return false;
                } else {
                    runCommand = entry.getValue();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void applyEffect() {
        try {
            if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), runCommand.replace("<player>", player.getName())))
                EvaTunaLoreEffect.getInstance().getLogger().warning(lore + " → target player is not found");
        } catch (CommandException e) {
            EvaTunaLoreEffect.getInstance().getLogger().warning(lore + " → the executor for the given command fails with an unhandled exception");
        }
    }

    public static void load(FileConfiguration config) {
        try {
            ConfigurationSection section = config.getConfigurationSection("commands");
            for (String root : section.getKeys(false))
                commandLoreMap.put(root, section.getString(root).replaceAll("[\\[\\]]", ""));
        } catch (Exception e) {
            EvaTunaLoreEffect.getInstance().getLogger().warning("Wrong config file error");
        }
    }

}
