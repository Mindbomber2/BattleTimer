package BattleTimer.mechanics.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class COMINGFORYOU extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-8.25f, -9); }
    @Override
    public AbstractPersonality nextPersonality() { return new HAHAHAHAHAHA(); }
}