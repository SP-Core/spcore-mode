package spcore.fabric.screens;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import spcore.GlobalContext;
import spcore.SpCoreLogger;
import spcore.api.delegates.Action;
import spcore.api.delegates.Action1;
import spcore.appapi.Terminal;
import spcore.appapi.background.BackgroundJobManager;
import spcore.appapi.configuration.KnowApplicationManager;
import spcore.appapi.models.KnownApplication;
import spcore.appapi.models.SpCoreInfo;

import javax.script.ScriptException;
import java.util.*;

@Environment(value= EnvType.CLIENT)
public class TerminalScreen
        extends Screen {
    private int tickCounter;
    private final SelectionManager currentPageSelectionManager = new SelectionManager(this::getCurrentPageContent, this::setPageContent, this::getClipboard, this::setClipboard, string -> string.length() < 1024 && this.textRenderer.getWrappedLinesHeight(string, 114) <= 128);
    private long lastClickTime;
    private int lastClickIndex = -1;
    @Nullable
    private TerminalScreen.PageContent pageContent = TerminalScreen.PageContent.EMPTY;
//    private final Text  pageIndicatorText = Text.literal("SP-Terminal");
    private String pageStringContent;
    public static final Identifier TERMINAL_TEXTURE = new Identifier("spcore:textures/gui/terminal.png");

    private final Terminal terminal;
    private static Terminal lastTerminal;
    private static boolean showMainSpace = true;
    private final List<Drawable> drawables = new ArrayList<>();
    private KnownApplication currentApp = null;
    public static Terminal getCurrent(){
        return lastTerminal;
    }
    public TerminalScreen(Action exit, Action1<String> sign) {
        super(Text.literal("SP-Terminal"));
        if (pageStringContent == null) {
            pageStringContent = "";
        }
        terminal = new Terminal(currentPageSelectionManager, exit, sign);
        lastTerminal = terminal;
    }

    public TerminalScreen(Terminal terminal) {
        super(Text.literal("SP-Terminal"));
        this.terminal = new Terminal(currentPageSelectionManager, terminal.exitAction, terminal.signAction);
        if (pageStringContent == null) {
            pageStringContent = "";
        }
    }


    private void setClipboard(String clipboard) {
        if (this.client != null) {
            SelectionManager.setClipboard(this.client, clipboard);
        }
    }

    private String getClipboard() {
        return this.client != null ? SelectionManager.getClipboard(this.client) : "";
    }

    @Override
    public void tick() {
        super.tick();
        ++this.tickCounter;
    }

    @Override
    protected void init() {
        this.invalidatePageContent();
        this.terminal.init();
        initWidgets();
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if(showMainSpace){
            return false;
        }
        boolean bl = this.keyPressedEditMode(keyCode, scanCode, modifiers);
        this.terminal.onKeyDown(getCurrentPageContent());
        if (bl) {
            this.invalidatePageContent();
            return true;
        }
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (super.charTyped(chr, modifiers)) {
            return true;
        }
        if(showMainSpace){
            return false;
        }
        if (SharedConstants.isValidChar(chr)) {
            this.currentPageSelectionManager.insert(Character.toString(chr));
            this.invalidatePageContent();
            return true;
        }
        return false;
    }

    private boolean keyPressedEditMode(int keyCode, int scanCode, int modifiers) {
        if (Screen.isSelectAll(keyCode)) {
            this.currentPageSelectionManager.selectAll();
            return true;
        }
        if (Screen.isCopy(keyCode)) {
            this.currentPageSelectionManager.copy();
            return true;
        }
        if (Screen.isPaste(keyCode)) {
            this.currentPageSelectionManager.paste();
            return true;
        }
        if (Screen.isCut(keyCode)) {
            this.currentPageSelectionManager.cut();
            return true;
        }
        SelectionManager.SelectionType selectionType = Screen.hasControlDown() ? SelectionManager.SelectionType.WORD : SelectionManager.SelectionType.CHARACTER;
        switch (keyCode) {
            case 259 -> {
                this.currentPageSelectionManager.delete(-1, selectionType);
                return true;
            }
            case 261 -> {
                this.currentPageSelectionManager.delete(1, selectionType);
                return true;
            }
            case 257, 335 -> {
                this.currentPageSelectionManager.insert("\n");
                terminal.onLineEnd(getCurrentPageContent());
                return true;
            }
            case 263 -> {
                this.currentPageSelectionManager.moveCursor(-1, Screen.hasShiftDown(), selectionType);
                return true;
            }
            case 262 -> {
                this.currentPageSelectionManager.moveCursor(1, Screen.hasShiftDown(), selectionType);
                return true;
            }
            case 265 -> {
                this.moveUpLine();
                return true;
            }
            case 264 -> {
                this.moveDownLine();
                return true;
            }
            case 268 -> {
                this.moveToLineStart();
                return true;
            }
            case 269 -> {
                this.moveToLineEnd();
                return true;
            }
        }
        return false;
    }

    private void moveUpLine() {
        this.moveVertically(-1);
    }

    private void moveDownLine() {
        this.moveVertically(1);
    }

    private void moveVertically(int lines) {
        int i = this.currentPageSelectionManager.getSelectionStart();
        int j = this.getPageContent().getVerticalOffset(i, lines);
        this.currentPageSelectionManager.moveCursorTo(j, Screen.hasShiftDown());
    }

    private void moveToLineStart() {
        if (Screen.hasControlDown()) {
            this.currentPageSelectionManager.moveCursorToStart(Screen.hasShiftDown());
        } else {
            int i = this.currentPageSelectionManager.getSelectionStart();
            int j = this.getPageContent().getLineStart(i);
            this.currentPageSelectionManager.moveCursorTo(j, Screen.hasShiftDown());
        }
    }

    private void moveToLineEnd() {
        if (Screen.hasControlDown()) {
            this.currentPageSelectionManager.moveCursorToEnd(Screen.hasShiftDown());
        } else {
            TerminalScreen.PageContent pageContent = this.getPageContent();
            int i = this.currentPageSelectionManager.getSelectionStart();
            int j = pageContent.getLineEnd(i);
            this.currentPageSelectionManager.moveCursorTo(j, Screen.hasShiftDown());
        }
    }


    private String getCurrentPageContent() {
        if(pageStringContent == null){
            pageStringContent = "";
        }
        return pageStringContent;
    }

    private void setPageContent(String newContent) {
        this.pageStringContent = newContent;
        this.invalidatePageContent();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        this.setFocused(null);
        if(!showMainSpace){
            int i = (this.width - 192) / 2;
            int j = 2;
            context.drawTexture(TERMINAL_TEXTURE, i, 2, 0, 0, 192, 192);
            int n = this.textRenderer.getWidth(this.title);
            context.drawText(this.textRenderer, this.title, i - n + 192 - 44, 18, 14277081, false);
            TerminalScreen.PageContent pageContent = this.getPageContent();
            for (TerminalScreen.Line line : pageContent.lines) {
                context.drawText(this.textRenderer, line.text, line.x, line.y, 14277081, false);
            }
            this.drawSelection(context, pageContent.selectionRectangles);
            this.drawCursor(context, pageContent.position, pageContent.atEnd);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void drawCursor(DrawContext context, TerminalScreen.Position position, boolean atEnd) {
        if (this.tickCounter / 6 % 2 == 0) {
            position = this.absolutePositionToScreenPosition(position);
            if (!atEnd) {
                context.fill(position.x, position.y - 1, position.x + 1, position.y + this.textRenderer.fontHeight, 14277081);
            } else {
                context.drawText(this.textRenderer, "_", position.x, position.y, 14277081, false);
            }
        }
    }

    private void drawSelection(DrawContext context, Rect2i[] selectionRectangles) {
        for (Rect2i rect2i : selectionRectangles) {
            int i = rect2i.getX();
            int j = rect2i.getY();
            int k = i + rect2i.getWidth();
            int l = j + rect2i.getHeight();
            context.fill(RenderLayer.getGuiTextHighlight(), i, j, k, l, -16776961);
        }
    }

    private TerminalScreen.Position screenPositionToAbsolutePosition(TerminalScreen.Position position) {
        return new TerminalScreen.Position(position.x - (this.width - 192) / 2 - 36, position.y - 32);
    }

    private TerminalScreen.Position absolutePositionToScreenPosition(TerminalScreen.Position position) {
        return new TerminalScreen.Position(position.x + (this.width - 192) / 2 + 36, position.y + 32);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if(showMainSpace){
            return false;
        }
        if (button == 0) {
            long l = Util.getMeasuringTimeMs();
            TerminalScreen.PageContent pageContent = this.getPageContent();
            int i = pageContent.getCursorPosition(this.textRenderer, this.screenPositionToAbsolutePosition(new TerminalScreen.Position((int)mouseX, (int)mouseY)));
            if (i >= 0) {
                if (i == this.lastClickIndex && l - this.lastClickTime < 250L) {
                    if (!this.currentPageSelectionManager.isSelecting()) {
                        this.selectCurrentWord(i);
                    } else {
                        this.currentPageSelectionManager.selectAll();
                    }
                } else {
                    this.currentPageSelectionManager.moveCursorTo(i, Screen.hasShiftDown());
                }
                this.invalidatePageContent();
            }
            this.lastClickIndex = i;
            this.lastClickTime = l;
        }
        return true;
    }

    private void selectCurrentWord(int cursor) {
        String string = this.getCurrentPageContent();
        this.currentPageSelectionManager.setSelection(TextHandler.moveCursorByWords(string, -1, cursor, false), TextHandler.moveCursorByWords(string, 1, cursor, false));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        if(showMainSpace){
            return false;
        }
        if (button == 0) {
            TerminalScreen.PageContent pageContent = this.getPageContent();
            int i = pageContent.getCursorPosition(this.textRenderer, this.screenPositionToAbsolutePosition(new TerminalScreen.Position((int)mouseX, (int)mouseY)));
            this.currentPageSelectionManager.moveCursorTo(i, true);
            this.invalidatePageContent();
        }
        return true;
    }

    private TerminalScreen.PageContent getPageContent() {
        if (this.pageContent == null) {
            this.pageContent = this.createPageContent();
        }
        return this.pageContent;
    }

    private void invalidatePageContent() {
        this.pageContent = null;
    }

    private void changePage() {
        this.currentPageSelectionManager.putCursorAtEnd();
        this.invalidatePageContent();
    }

    private TerminalScreen.PageContent createPageContent() {
        int l;
        TerminalScreen.Position position;
        boolean bl;
        String string = this.getCurrentPageContent();
        if (string.isEmpty()) {
            return TerminalScreen.PageContent.EMPTY;
        }
        int i = this.currentPageSelectionManager.getSelectionStart();
        int j = this.currentPageSelectionManager.getSelectionEnd();
        IntArrayList intList = new IntArrayList();
        ArrayList list = Lists.newArrayList();
        MutableInt mutableInt = new MutableInt();
        MutableBoolean mutableBoolean = new MutableBoolean();
        TextHandler textHandler = this.textRenderer.getTextHandler();
        textHandler.wrapLines(string, 114, Style.EMPTY, true, (style, start, end) -> {
            int ii = mutableInt.getAndIncrement();
            String s = string.substring(start, end);
            mutableBoolean.setValue(s.endsWith("\n"));
            String string2 = StringUtils.stripEnd(s, " \n");
            int jj = ii * this.textRenderer.fontHeight;
            TerminalScreen.Position position2 = this.absolutePositionToScreenPosition(new TerminalScreen.Position(0, jj));
            intList.add(start);
            list.add(new TerminalScreen.Line(style, string2, position2.x, position2.y));
        });
        int[] is = intList.toIntArray();
        boolean bl2 = bl = i == string.length();
        if (bl && mutableBoolean.isTrue()) {
            position = new TerminalScreen.Position(0, list.size() * this.textRenderer.fontHeight);
        } else {
            int k = TerminalScreen.getLineFromOffset(is, i);
            l = this.textRenderer.getWidth(string.substring(is[k], i));
            position = new TerminalScreen.Position(l, k * this.textRenderer.fontHeight);
        }
        ArrayList<Rect2i> list2 = Lists.newArrayList();
        if (i != j) {
            int o;
            l = Math.min(i, j);
            int m = Math.max(i, j);
            int n = TerminalScreen.getLineFromOffset(is, l);
            if (n == (o = TerminalScreen.getLineFromOffset(is, m))) {
                int p = n * this.textRenderer.fontHeight;
                int q = is[n];
                list2.add(this.getLineSelectionRectangle(string, textHandler, l, m, p, q));
            } else {
                int p = n + 1 > is.length ? string.length() : is[n + 1];
                list2.add(this.getLineSelectionRectangle(string, textHandler, l, p, n * this.textRenderer.fontHeight, is[n]));
                for (int q = n + 1; q < o; ++q) {
                    int r = q * this.textRenderer.fontHeight;
                    String string2 = string.substring(is[q], is[q + 1]);
                    int s = (int)textHandler.getWidth(string2);
                    list2.add(this.getRectFromCorners(new TerminalScreen.Position(0, r), new TerminalScreen.Position(s, r + this.textRenderer.fontHeight)));
                }
                list2.add(this.getLineSelectionRectangle(string, textHandler, is[o], m, o * this.textRenderer.fontHeight, is[o]));
            }
        }
        return new TerminalScreen.PageContent(string, position, bl, is, (Line[]) list.toArray(new Line[0]), list2.toArray(new Rect2i[0]));
    }

    static int getLineFromOffset(int[] lineStarts, int position) {
        int i = Arrays.binarySearch(lineStarts, position);
        if (i < 0) {
            return -(i + 2);
        }
        return i;
    }

    private Rect2i getLineSelectionRectangle(String string, TextHandler handler, int selectionStart, int selectionEnd, int lineY, int lineStart) {
        String string2 = string.substring(lineStart, selectionStart);
        String string3 = string.substring(lineStart, selectionEnd);
        TerminalScreen.Position position = new TerminalScreen.Position((int)handler.getWidth(string2), lineY);
        TerminalScreen.Position position2 = new TerminalScreen.Position((int)handler.getWidth(string3), lineY + this.textRenderer.fontHeight);
        return this.getRectFromCorners(position, position2);
    }

    private Rect2i getRectFromCorners(TerminalScreen.Position start, TerminalScreen.Position end) {
        TerminalScreen.Position position = this.absolutePositionToScreenPosition(start);
        TerminalScreen.Position position2 = this.absolutePositionToScreenPosition(end);
        int i = Math.min(position.x, position2.x);
        int j = Math.max(position.x, position2.x);
        int k = Math.min(position.y, position2.y);
        int l = Math.max(position.y, position2.y);
        return new Rect2i(i, k, j - i, l - k);
    }

    @Environment(value=EnvType.CLIENT)
    static class PageContent {
        static final TerminalScreen.PageContent EMPTY = new TerminalScreen.PageContent("", new TerminalScreen.Position(0, 0), true, new int[]{0}, new TerminalScreen.Line[]{new TerminalScreen.Line(Style.EMPTY, "", 0, 0)}, new Rect2i[0]);
        private final String pageContent;
        final TerminalScreen.Position position;
        final boolean atEnd;
        private final int[] lineStarts;
        final TerminalScreen.Line[] lines;
        final Rect2i[] selectionRectangles;

        public PageContent(String pageContent, TerminalScreen.Position position, boolean atEnd, int[] lineStarts, TerminalScreen.Line[] lines, Rect2i[] selectionRectangles) {
            this.pageContent = pageContent;
            this.position = position;
            this.atEnd = atEnd;
            this.lineStarts = lineStarts;
            this.lines = lines;
            this.selectionRectangles = selectionRectangles;
        }

        public int getCursorPosition(TextRenderer renderer, TerminalScreen.Position position) {
            int i = position.y / renderer.fontHeight;
            if (i < 0) {
                return 0;
            }
            if (i >= this.lines.length) {
                return this.pageContent.length();
            }
            TerminalScreen.Line line = this.lines[i];
            return this.lineStarts[i] + renderer.getTextHandler().getTrimmedLength(line.content, position.x, line.style);
        }

        public int getVerticalOffset(int position, int lines) {
            int m;
            int i = TerminalScreen.getLineFromOffset(this.lineStarts, position);
            int j = i + lines;
            if (0 <= j && j < this.lineStarts.length) {
                int k = position - this.lineStarts[i];
                int l = this.lines[j].content.length();
                m = this.lineStarts[j] + Math.min(k, l);
            } else {
                m = position;
            }
            return m;
        }

        public int getLineStart(int position) {
            int i = TerminalScreen.getLineFromOffset(this.lineStarts, position);
            return this.lineStarts[i];
        }

        public int getLineEnd(int position) {
            int i = TerminalScreen.getLineFromOffset(this.lineStarts, position);
            return this.lineStarts[i] + this.lines[i].content.length();
        }
    }

    @Environment(value=EnvType.CLIENT)
    static class Line {
        final Style style;
        final String content;
        final Text text;
        final int x;
        final int y;

        public Line(Style style, String content, int x, int y) {
            this.style = style;
            this.content = content;
            this.x = x;
            this.y = y;
            this.text = Text.literal(content).setStyle(style);
        }
    }

    @Environment(value=EnvType.CLIENT)
    static class Position {
        public final int x;
        public final int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void initWidgets() {
        initLeftWidgets();
        initTerminalButton();
    }

    private void initLeftWidgets(){
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(2, 2, 2, 0);
        GridWidget.Adder adder = gridWidget.createAdder(4);

        List<KnownApplication> apps;
        if(this.currentApp == null){
            apps = KnowApplicationManager.getApplications();
        }
        else{
            apps = KnowApplicationManager.getApplications()
                    .stream().filter(p -> p.Hash == this.currentApp.Hash)
                    .toList();
        }


        for (var app: apps
        ) {
            adder.add(ButtonWidget.builder(Text.of(app.Manifest.manifest.name), (button) -> {
                sendCommand("exe " + app.Manifest.manifest.appId);
            }).width(120).build());
            adder.add(ButtonWidget.builder(Text.of("reset"), (button) -> {
                sendCommand("up " + app.Manifest.manifest.appId);
            }).width(60).build());
            adder.add(ButtonWidget.builder(Text.of("up!"), (button) -> {
                sendCommand("up " + app.Manifest.manifest.appId);
            }).width(25).build());
            adder.add(ButtonWidget.builder(Text.of("⚙"), (button) -> {
                this.currentApp = app;
                this.clearAndInit();
            }).width(20).build());
        }

        int i;
        if(showMainSpace){
            i = this.width / 2 - (192 / 2);
        }
        else{
            i = this.width / 2 + (192 / 2);
        }
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, i, 5, this.width, this.height, 0F, 0F);
        gridWidget.forEachChild(this::addDrawableChild);

        if(currentApp != null){
            GridWidget settingGridWidget = new GridWidget();
            settingGridWidget.getMainPositioner().margin(2, 2, 2, 0);

            GridWidget.Adder settingAdder = settingGridWidget.createAdder(1);

            settingAdder.add(new TextWidget(Text.of("id: " + currentApp.Manifest.manifest.appId), this.textRenderer));
            settingAdder.add(new TextWidget(Text.of("version: " + currentApp.Manifest.manifest.version), this.textRenderer));
            settingAdder.add(new TextWidget(Text.of("lifetime: " + currentApp.Manifest.manifest.lifetime), this.textRenderer));
            settingAdder.add(new TextWidget(Text.of("server_domain: " + currentApp.Manifest.manifest.server_domain), this.textRenderer));
            settingAdder.add(new TextWidget(Text.of("host_access: " + currentApp.Manifest.manifest.host_access), this.textRenderer));
            settingAdder.add(new TextWidget(Text.of("source: " + currentApp.Manifest.absolute), this.textRenderer));
            if(currentApp.Manifest.manifest.lifetime.equals("background")){

                if(BackgroundJobManager.getInstance()
                        .getJobs().stream().anyMatch(p -> p.AppInfo.hashCode() == currentApp.Hash)){
                    settingAdder.add(ButtonWidget.builder(Text.of("Остановить фоновый процесс"), (button) -> {
                        currentApp.DisableBackground = true;
                        KnowApplicationManager.Save();
                        BackgroundJobManager.getInstance()
                                .remove(currentApp.Manifest);
                        clearAndInit();

                    }).width(200).build());
                }
                else{
                    settingAdder.add(ButtonWidget.builder(Text.of("Запустить фоновый процесс"), (button) -> {
                        currentApp.DisableBackground = false;
                        KnowApplicationManager.Save();
                        try {
                            BackgroundJobManager.getInstance()
                                    .register(currentApp.Manifest, currentApp.DisableBackground);
                        } catch (ScriptException e) {
                            GlobalContext.LOGGER.error(e.getMessage());
                        }

                        clearAndInit();
                    }).width(200).build());
                }

            }


            settingGridWidget.refreshPositions();
            SimplePositioningWidget.setPos(settingGridWidget, i, 45, this.width, this.height, 0F, 0F);
            settingGridWidget.forEachChild(this::addDrawableChild);
        }

    }


    private void initTerminalButton(){

        Text text = null;

        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(2, 2, 2, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);
        if(currentApp != null){
            adder.add(ButtonWidget.builder(Text.of("Закрыть настройки"), (button) -> {
                currentApp = null;
                this.clearAndInit();
            }).width(150).build());
        }

        if(GlobalContext.LOGGER.LoggingChat){
            text = Text.of("Отключить логи");
        }
        else{
            text = Text.of("Включить логи");
        }

        adder.add(ButtonWidget.builder(text, (button) -> {
            GlobalContext.LOGGER.LoggingChat = !GlobalContext.LOGGER.LoggingChat;
            this.clearAndInit();
        }).width(150).build());

        if(showMainSpace){
            text = Text.of("Open terminal");
        }
        else{
            text = Text.of("Open GUI");
        }

        adder.add(ButtonWidget.builder(text, (button) -> {
            showMainSpace = !showMainSpace;
            this.clearAndInit();
        }).width(150).build());

        int h;
        if(currentApp != null){
            h = this.height - 80;
        }
        else{
            h = this.height - 60;
        }
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, this.width - 170, h, this.width, this.height, 0F, 0F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    protected void showTerminal(){
        if(!showMainSpace){
            return;
        }
        showMainSpace = false;
        this.clearAndInit();
    }

    protected void showGui(){
        if(showMainSpace){
            return;
        }
        showMainSpace = true;
        this.clearAndInit();
    }

    protected void sendCommand(String command){
        terminal.reset();
        currentPageSelectionManager.insert(command);
        currentPageSelectionManager.insert("\n");
        terminal.onLineEnd(getCurrentPageContent());
    }


}

