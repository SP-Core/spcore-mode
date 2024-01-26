package com.example.spcore;

import com.example.spcore.models.MCProfile;
import com.example.spcore.models.TreeProfile;

import java.time.OffsetDateTime;

public class SpCoreApiContext {

    public static boolean IsAuthorized;

    public static String Code;

    public static String UserName;
    public static String Uuid;

    public static boolean IsInit;

    public static MCProfile CurrentMCProfile;
    public OffsetDateTime LastUpdateMCProfile;


    public static TreeProfile CurrentTreeProfile;
    public OffsetDateTime LastUpdateTreeProfile;

}
