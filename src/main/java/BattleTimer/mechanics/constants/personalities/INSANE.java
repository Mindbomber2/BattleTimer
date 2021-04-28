package BattleTimer.mechanics.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class INSANE extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-5.15f, -6); }
    @Override
    public AbstractPersonality nextPersonality() { return new IMPOSSIBLE(); }
}