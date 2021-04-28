package BattleTimer.mechanics.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MEDIUM extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-2f, -3); }
    @Override
    public AbstractPersonality nextPersonality() { return new HARD(); }
}