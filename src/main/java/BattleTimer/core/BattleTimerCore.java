package BattleTimer.core;

import BattleTimer.mechanics.AddDelayCardQueueAction;
import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class BattleTimerCore implements OnCardUseSubscriber {

    public static final String modID = "battletimer";
    private float xPos = 350f, yPos = 750f, orgYPos = 750f;
    private int curPage = 1;

    public static final String minionsID = "Friendly_Minions_0987678";
    public static final boolean hasMinions;
    static { hasMinions = Loader.isModLoaded(minionsID); }

    public static final Logger logger = LogManager.getLogger(BattleTimerCore.class.getName());
    public static String makeID(String idText) {
        return modID + ":" + idText;
    }
    
    public BattleTimerCore() { BaseMod.subscribe(this); }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static void initialize() {
        BattleTimerCore thismod = new BattleTimerCore();
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        AddDelayCardQueueAction.addDelayCardQueueAction();
    }

    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(UIStrings.class, modID + "Resources/localization/eng/ui.json");
    }

}
