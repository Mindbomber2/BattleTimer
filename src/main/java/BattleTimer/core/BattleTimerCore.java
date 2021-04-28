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
public class BattleTimerCore implements EditStringsSubscriber, PostInitializeSubscriber, OnCardUseSubscriber {

    public static final String modID = "battletimer";
    private static SpireConfig modConfig = null;
    private float xPos = 350f, yPos = 750f, orgYPos = 750f;
    private ModPanel settingsPanel;
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
        try {
            Properties defaults = new Properties();
            modConfig = new SpireConfig("PrismRiver", "Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadConfig();
    }

    public void receivePostInitialize(){
        UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(makeID("OptionsMenu"));
        String[] TEXT = UIStrings.TEXT;
        settingsPanel = new ModPanel();
        ModLabeledToggleButton wildCardButton = new ModLabeledToggleButton(TEXT[1], xPos, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, true, settingsPanel, l -> {
        },
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("wildCard", button.enabled);
                        saveConfig();
                    }
                });
        registerUIElement(wildCardButton, true);
        if (pages.size() > 1) {
            ModLabeledButton FlipPageBtn = new ModLabeledButton(TEXT[3], xPos + 450f, orgYPos + 45f, Settings.CREAM_COLOR, Color.WHITE, FontHelper.cardEnergyFont_L, settingsPanel,
                    button ->
                    {
                        if (pages.containsKey(curPage + 1)) {
                            changePage(curPage + 1);
                        } else {
                            changePage(1);
                        }
                    });
            settingsPanel.addUIElement(FlipPageBtn);
        }
        //currentKey = modConfig.getString("currentKey");
        //wildCardMode = modConfig.getBool("wildCard");
        BaseMod.registerModBadge(ImageMaster.loadImage(modID + "Resources/images/modBadge.png"), modID, "squeeny", "", settingsPanel);

    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        AddDelayCardQueueAction.addDelayCardQueueAction();
    }

    private final float pageOffset = 12000f;
    private HashMap<Integer, ArrayList<IUIElement>> pages = new HashMap<Integer, ArrayList<IUIElement>>() {{
        put(1, new ArrayList<>());
    }};
    private float elementSpace = 50f;
    private float yThreshold = yPos - elementSpace * 12;

    private void registerUIElement(IUIElement elem, boolean decrement) {
        settingsPanel.addUIElement(elem);

        int page = pages.size() + (yThreshold == yPos ? 1 : 0);
        if (!pages.containsKey(page)) {
            pages.put(page, new ArrayList<>());
            yPos = orgYPos;
            elem.setY(yPos);
        }
        if (page > curPage) {
            elem.setX(elem.getX() + pageOffset);
        }
        pages.get(page).add(elem);

        if (decrement) {
            yPos -= elementSpace;
        }
    }

    private void changePage(int i) {
        for (IUIElement e : pages.get(curPage)) {
            e.setX(e.getX() + pageOffset);
        }

        for (IUIElement e : pages.get(i)) {
            e.setX(e.getX() - pageOffset);
        }
        curPage = i;
    }

    private static void saveConfig() {
        try {
            //;currentKey = modConfig.getString("currentKey");
            //wildCardMode = modConfig.getBool("wildCard");
            modConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadConfig() {
        //currentKey = modConfig.getString("currentKey");
        //wildCardMode = modConfig.getBool("wildCard");
    }

    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(UIStrings.class, modID + "Resources/localization/eng/ui.json");
    }

}
