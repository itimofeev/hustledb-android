package ru.hustledb.hustledb.Events;

/**
 * Created by sergey on 20.11.16.
 */

public class OnPreregistrationLoadCompleteEvent{
    private final boolean isError;
    public OnPreregistrationLoadCompleteEvent(boolean isError){
        this.isError = isError;
    }

    public boolean isError() {
        return isError;
    }
}
