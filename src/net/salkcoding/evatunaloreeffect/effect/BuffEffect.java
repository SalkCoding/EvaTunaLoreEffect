package net.salkcoding.evatunaloreeffect.effect;

import net.salkcoding.evatunaloreeffect.EvaTunaLoreEffect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuffEffect implements IEffect {

    private static Pattern buffRegex = Pattern.compile("[가-힣]*[\\s]?[가-힣]*\\s[디]?버프\\s[\\d]+레벨\\s[\\d]+초");

    private static Pattern buffKindRegex = Pattern.compile("[가-힣]*[\\s]?[가-힣]*\\s[디]?버프");
    private static Pattern buffAmplifierRegex = Pattern.compile("[\\d]+레벨");
    private static Pattern buffDurabilityRegex = Pattern.compile("[\\d]+초");

    private Player player;
    private String line;

    private PotionEffectType effectType;
    private int amplifier;
    private int durability;

    private boolean isMatched = false;

    public BuffEffect(Player player, String line) {
        this.player = player;
        this.line = line;
    }

    @Override
    public boolean isLoreMatch() {
        Matcher buffMatcher = buffRegex.matcher(line);
        if (!buffMatcher.find()) return false;
        Matcher buffKindMatcher = buffKindRegex.matcher(line);
        Matcher buffAmplifierMatcher = buffAmplifierRegex.matcher(line);
        Matcher buffDurabilityMatcher = buffDurabilityRegex.matcher(line);
        if (!buffKindMatcher.find()) return false;
        if (!buffAmplifierMatcher.find()) return false;
        if (!buffDurabilityMatcher.find()) return false;
        //Remove all of 버프, White space
        String buffKind = line.substring(buffKindMatcher.start(), buffKindMatcher.end() - 3).replaceAll("[\\s]+", "");
        String buffAmplifier = line.substring(buffAmplifierMatcher.start(), buffAmplifierMatcher.end() - 2);
        String buffDurability = line.substring(buffDurabilityMatcher.start(), buffDurabilityMatcher.end() - 1);

        switch (buffKind) {
            case "흡수":
                effectType = PotionEffectType.ABSORPTION;
                break;
            case "흉조":
                effectType = PotionEffectType.BAD_OMEN;
                break;
            case "실명":
                effectType = PotionEffectType.BLINDNESS;
                break;
            case "전달체의힘":
                effectType = PotionEffectType.CONDUIT_POWER;
                break;
            case "멀미":
                effectType = PotionEffectType.CONFUSION;
                break;
            case "저항":
                effectType = PotionEffectType.DAMAGE_RESISTANCE;
                break;
            case "돌고래의우아함":
                effectType = PotionEffectType.DOLPHINS_GRACE;
                break;
            case "성급함":
                effectType = PotionEffectType.FAST_DIGGING;
                break;
            case "화염저항":
                effectType = PotionEffectType.FIRE_RESISTANCE;
                break;
            case "발광":
                effectType = PotionEffectType.GLOWING;
                break;
            case "즉시피해":
                effectType = PotionEffectType.HARM;
                break;
            case "즉시치유":
                effectType = PotionEffectType.HEAL;
                break;
            case "생명력강화":
                effectType = PotionEffectType.HEALTH_BOOST;
                break;
            case "마을의영웅":
                effectType = PotionEffectType.HERO_OF_THE_VILLAGE;
                break;
            case "허기":
                effectType = PotionEffectType.HUNGER;
                break;
            case "힘":
                effectType = PotionEffectType.INCREASE_DAMAGE;
                break;
            case "투명":
                effectType = PotionEffectType.INVISIBILITY;
                break;
            case "점프강화":
                effectType = PotionEffectType.JUMP;
                break;
            case "공중부양":
                effectType = PotionEffectType.LEVITATION;
                break;
            case "행운":
                effectType = PotionEffectType.LUCK;
                break;
            case "야간투시":
                effectType = PotionEffectType.NIGHT_VISION;
                break;
            case "독":
                effectType = PotionEffectType.POISON;
                break;
            case "재생":
                effectType = PotionEffectType.REGENERATION;
                break;
            case "포화":
                effectType = PotionEffectType.SATURATION;
                break;
            case "감속":
                effectType = PotionEffectType.SLOW;
                break;
            case "채굴피로":
                effectType = PotionEffectType.SLOW_DIGGING;
                break;
            case "느린낙하":
                effectType = PotionEffectType.SLOW_FALLING;
                break;
            case "신속":
                effectType = PotionEffectType.SPEED;
                break;
            case "불운":
                effectType = PotionEffectType.UNLUCK;
                break;
            case "수중호흡":
                effectType = PotionEffectType.WATER_BREATHING;
                break;
            case "나약함":
                effectType = PotionEffectType.WEAKNESS;
                break;
            case "시듦":
                effectType = PotionEffectType.WITHER;
                break;
            default:
                effectType = null;
                break;
        }
        if (effectType == null) {
            EvaTunaLoreEffect.getInstance().getLogger().warning(line + " → " + buffKind + " is not PotionEffectType");
            return false;
        }
        try {
            //Zero base
            amplifier = Integer.parseInt(buffAmplifier) - 1;
        } catch (
                NumberFormatException e) {
            EvaTunaLoreEffect.getInstance().getLogger().warning(line + " → " + buffAmplifier + " is not correct number");
            return false;
        }
        try {
            //1sec = 20tick
            durability = Integer.parseInt(buffDurability) * 20;
        } catch (
                NumberFormatException e) {
            EvaTunaLoreEffect.getInstance().getLogger().warning(line + " → " + buffDurability + " is not correct number");
            return false;
        }

        isMatched = true;
        return true;
    }

    @Override
    public void applyEffect() {
        if (isMatched) {
            player.removePotionEffect(effectType);
            player.addPotionEffect(new PotionEffect(effectType, durability, amplifier));
        } else {
            throw new IllegalStateException("applyEffect() method couldn't call before isLoreMatch() method return true");
        }
    }
}
