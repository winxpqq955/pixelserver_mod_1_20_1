//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.ho3.classictweaks.ui;

import java.util.function.Supplier;

import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Classic_1_7_10_GameMenuScreen extends GameMenuScreen {
    private static final int GRID_COLUMNS = 2;
    private static final int BUTTONS_TOP_MARGIN = 50;
    private static final int GRID_MARGIN = 4;
    private static final int WIDE_BUTTON_WIDTH = 204;
    private static final int NORMAL_BUTTON_WIDTH = 98;
    private static final Text RETURN_TO_GAME_TEXT = Text.translatable("menu.returnToGame");
    private static final Text ADVANCEMENTS_TEXT = Text.translatable("gui.advancements");
    private static final Text STATS_TEXT = Text.translatable("gui.stats");
    private static final Text SEND_FEEDBACK_TEXT = Text.translatable("menu.sendFeedback");
    private static final Text REPORT_BUGS_TEXT = Text.translatable("menu.reportBugs");
    private static final Text OPTIONS_TEXT = Text.translatable("menu.options");
    private static final Text SHARE_TO_LAN_TEXT = Text.translatable("menu.shareToLan");
    private static final Text PLAYER_REPORTING_TEXT = Text.translatable("menu.playerReporting");
    private static final Text RETURN_TO_MENU_TEXT = Text.translatable("menu.returnToMenu");
    private static final Text DISCONNECT_TEXT = Text.translatable("menu.disconnect");
    private static final Text SAVING_LEVEL_TEXT = Text.translatable("menu.savingLevel");
    private static final Text GAME_TEXT = Text.translatable("menu.game");
    private static final Text PAUSED_TEXT = Text.translatable("menu.paused");
    private final boolean showMenu;

    public Classic_1_7_10_GameMenuScreen(boolean showMenu) {
        super(showMenu);
        this.showMenu = showMenu;
    }

    protected void init() {
        if (this.showMenu) {
            this.initWidgets();
        }

        this.addDrawableChild(new TextWidget(0, this.showMenu ? 40 : 10, this.width, 9, this.title, this.textRenderer));
    }

    private void initWidgets() {
        final var client = this.getMinecraft();
        assert client != null;
        GridWidget gridlayout = new GridWidget();
        gridlayout.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder gridlayout$rowhelper = gridlayout.createAdder(2);
        gridlayout$rowhelper.add(ButtonWidget.builder(RETURN_TO_GAME_TEXT, (button) -> {
            client.setScreen(null);
            client.mouse.lockCursor();
        }).width(204).build(), 2, gridlayout.copyPositioner().marginTop(50));
        gridlayout$rowhelper.add(this.createButton(ADVANCEMENTS_TEXT, () -> new AdvancementsScreen(client.player.networkHandler.getAdvancementHandler())));
        gridlayout$rowhelper.add(this.createButton(STATS_TEXT, () -> new StatsScreen(this, client.player.getStatHandler())));
        gridlayout$rowhelper.add(new EmptyWidget(204, ButtonWidget.DEFAULT_HEIGHT), 2);
        gridlayout$rowhelper.add(this.createButton(OPTIONS_TEXT, () -> new OptionsScreen(this, client.options)));
        if (client.isIntegratedServerRunning() && !client.getServer().isRemote()) {
            gridlayout$rowhelper.add(this.createButton(SHARE_TO_LAN_TEXT, () -> new OpenToLanScreen(this)));
        } else {
            gridlayout$rowhelper.add(this.createButton(PLAYER_REPORTING_TEXT, SocialInteractionsScreen::new));
        }

//        gridlayout$rowhelper.add(ButtonWidget.builder(Text.translatable("fml.menu.mods"), (button) -> {
//            client.setScreen(new ModListScreen(instance));
//        }).width(204).build(), 2);
        Text component = client.isInSingleplayer() ? RETURN_TO_MENU_TEXT : DISCONNECT_TEXT;
        gridlayout$rowhelper.add(ButtonWidget.builder(component, (arg) -> {
            arg.active = false;
            client.getAbuseReportContext().tryShowDraftScreen(client, this, this::disconnect, true);
        }).width(204).build(), 2);
        gridlayout.refreshPositions();
        SimplePositioningWidget.setPos(gridlayout, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridlayout.forEachChild(this::addDrawableChild);
    }

    private void disconnect() {
        boolean flag = this.client.isInSingleplayer();
        boolean flag1 = this.client.isRealmsEnabled();
        this.client.world.disconnect();
        if (flag) {
            this.client.disconnect(new MessageScreen(SAVING_LEVEL_TEXT));
        } else {
            this.client.disconnect();
        }

        TitleScreen titlescreen = new TitleScreen();
        if (flag) {
            this.client.setScreen(titlescreen);
        } else if (flag1) {
            this.client.setScreen(new RealmsMainScreen(titlescreen));
        } else {
            this.client.setScreen(new MultiplayerScreen(titlescreen));
        }

    }

    private ButtonWidget createButton(Text text, Supplier<Screen> screenSupplier) {
        return ButtonWidget.builder(text, (arg) -> {
            this.client.setScreen((Screen)screenSupplier.get());
        }).width(98).build();
    }

}
