package net.salkcoding.evatunaloreeffect.effect;

import net.salkcoding.evatunaloreeffect.EvaTunaLoreEffect;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HungerEffect implements IEffect {

    private static Pattern hungerRegex = Pattern.compile("허기\\s[\\d]+칸");

    private static Pattern hungerLevelRegex = Pattern.compile("\\s[\\d]+칸");

    private Player player;
    private String line;

    private double foodLevel;

    private boolean isMatched = false;

    public HungerEffect(Player player, String line) {
        this.player = player;
        this.line = line;
    }

    @Override
    public boolean isLoreMatch() {
        Matcher hungerMatcher = hungerRegex.matcher(line);
        if (!hungerMatcher.find()) return false;
        Matcher hungerLevelMatcher = hungerLevelRegex.matcher(line);
        if (!hungerLevelMatcher.find()) return false;
        String hungerFoodLevel = line.substring(hungerLevelMatcher.start(), hungerLevelMatcher.end() - 1);

        try {
            foodLevel = Double.parseDouble(hungerFoodLevel);
        } catch (NumberFormatException e) {
            EvaTunaLoreEffect.getInstance().getLogger().warning(line + " → " + hungerFoodLevel + " is not correct number");
            return false;
        }

        isMatched = true;
        return true;
    }

    @Override
    public void applyEffect() {
        if (isMatched) {
            player.setFoodLevel(player.getFoodLevel() + (int) (foodLevel * 2));
        } else {
            throw new IllegalStateException("applyEffect() method couldn't call before isLoreMatch() method return true");
        }
    }
}
