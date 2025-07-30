package com.Unite.UniteMobileApp;



import java.util.UUID;

public class CallIDGenerator {

    public static String generateCallID() {
        return "call_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
