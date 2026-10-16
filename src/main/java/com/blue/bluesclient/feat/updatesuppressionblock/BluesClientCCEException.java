package com.blue.bluesclient.feat.updatesuppressionblock;

public class BluesClientCCEException extends ClassCastException{
    public static final BluesClientCCEException INSTANCE = new BluesClientCCEException();

    private BluesClientCCEException() {
        super("Manually throw an exception by BluesClient.");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
