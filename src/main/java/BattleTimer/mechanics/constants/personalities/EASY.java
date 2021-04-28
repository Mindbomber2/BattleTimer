package BattleTimer.mechanics.constants.personalities;


import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EASY extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-1f, -2); }

    @Override
    public AbstractPersonality nextPersonality() { return new MEDIUM(); }
}