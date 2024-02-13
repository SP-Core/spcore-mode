package spcore;

import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.report.log.ChatLog;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.logging.LogManager;

public class GlobalContext {
    public static final String MOD_ID = "spcore";
    public static final SpCoreLogger LOGGER = new SpCoreLogger();
    public static final String API_URL = "https://spcore.ru/";

    private static String UserName;
    private static String Uuid;

    private static boolean IsInit;


    public static String getUserName()
    {
        return "Dima5x9";
        //return UserName;
    }

    public static String getUuid()
    {
        return Uuid;
    }

    public static boolean getIsInit()
    {
        return IsInit;
    }




    public static void Init(final ClientPlayerEntity player){
        if(IsInit){
            return;
        }
        UserName = player.getName().getString();
        Uuid = player.getUuidAsString();
        IsInit = true;



    }


}
