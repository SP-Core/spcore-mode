package spcore.fabric.models;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;

import java.util.regex.Pattern;
public class SPCoreCard {

    public final String Title;
    public final String Author;
    public final String Version;
    public final String System;
    public final String Data;
    public final String CryptoCode;
    public final String CardCode;
    public final String Auth;
    public final String Domain;
    public final String Signature;

    private static final String onePageRegex = "version:\\s+([\\w.]+)\\s+system:\\s+(\\w+)\\s+data:\\s+([а-яА-Я\\w]+)|\\s+#+\\s+([\\w\\d\\n]+)\\s+#";
    public SPCoreCard(String title, String author, String version, String system, String data, String cryptoCode, String cardCode, String auth, String domain, String signature) {
        Title = title;
        Author = author;
        Version = version;
        System = system;
        Data = data;
        CryptoCode = cryptoCode;
        CardCode = cardCode;
        Auth = auth;
        Domain = domain;
        Signature = signature;
    }

    public static SPCoreCard Parse(ItemStack stack){
        var nbt = stack.getNbt();
        assert nbt != null;
        var title = nbt.getString("title");
        var author = nbt.getString("author");
        String version = null;
        String system = null;
        String data = null;
        String cryptoCode = null;
        var pages = ((NbtList)nbt.get("pages"));
        assert pages != null;
        var onePage = pages.getString(0);
        var pattern = Pattern.compile(onePageRegex);
        var matcher = pattern.matcher(onePage);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                version = matcher.group(1);
                system = matcher.group(2);
                data = matcher.group(3);
            } else if (matcher.group(4) != null) {
                cryptoCode = matcher.group(4);
            }
        }

        return new SPCoreCard(
                title,
                author,
                version,
                system,
                data,
                cryptoCode,
                null,
                null,
                null,
                null
        );
    }

}
