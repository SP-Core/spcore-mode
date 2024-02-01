package spcore;

import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalContext {
    public static final String MOD_ID = "spcore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String API_URL = "https://spcore.ru/";

    private static String UserName;
    private static String Uuid;

    private static boolean IsInit;


    public static String getUserName()
    {
        return "Dima5x9";
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
