package net.replaceitem.symbolchat;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SymbolTab implements Comparable<SymbolTab> {
    private final Identifier id;

    private final String icon;
    private final Text tooltipText;
    private List<SymbolList> symbols;
    private final int order;
    private final Type type;
    private final boolean searchBar;

    public SymbolTab(Identifier id, String icon, int order, Type type, boolean searchBar, List<SymbolList> symbols) {
        this.id = id;
        this.icon = icon;
        this.order = order;
        this.type = type;
        this.tooltipText = Text.translatable(id.toTranslationKey("symbolchat.tab"));
        this.searchBar = searchBar;
        this.symbols = symbols;
    }

    public Identifier getId() {
        return id;
    }
    
    public void setSymbols(List<SymbolList> symbols) {
        this.symbols = symbols;
    }

    public boolean hasSearchBar() {
        return searchBar;
    }

    public List<SymbolList> getSymbols() {
        return symbols;
    }

    public Stream<String> streamSymbols() {
        return this.symbols.stream().flatMap(SymbolList::stream);
    }

    public Text getTooltipText() {
        return tooltipText;
    }

    @Override
    public int compareTo(@NotNull SymbolTab o) {
        return Integer.compare(order, o.order);
    }

    public Type getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }

    public static class Type {
        private static final Map<String, Type> TYPES = new HashMap<>();

        public static final Type SYMBOLS = register("symbols", new Type(i -> i, true, false));
        public static final Type KAOMOJIS = register("kaomojis", new Type(i -> 1, false, true));
        
        private final Int2IntFunction columnFunction;
        private final boolean hasTooltip;
        private final boolean fullWidthButtons;

        public Type(Int2IntFunction columnFunction, boolean hasTooltip, boolean fullWidthButtons) {
            this.columnFunction = columnFunction;
            this.hasTooltip = hasTooltip;
            this.fullWidthButtons = fullWidthButtons;
        }

        public static Type register(String name, Type type) {
            TYPES.put(name, type);
            return type;
        }

        @NotNull
        public static Type getOrDefault(String name, @NotNull Type defaultType) {
            Type type = get(name);
            return type == null ? defaultType : type;
        }

        @Nullable
        public static Type get(String name) {
            return TYPES.get(name);
        }

        public int getColumns(int panelColumns) {
            return columnFunction.get(panelColumns);
        }

        public boolean hasTooltip() {
            return hasTooltip;
        }

        public boolean hasFullWidthButtons() {
            return fullWidthButtons;
        }
    }
}
