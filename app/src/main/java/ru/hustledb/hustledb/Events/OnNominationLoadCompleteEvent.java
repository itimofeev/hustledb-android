package ru.hustledb.hustledb.Events;

/**
 * Created by sergey on 01.12.16.
 */
public class OnNominationLoadCompleteEvent {
    private final boolean isError;
    public OnNominationLoadCompleteEvent(boolean isError){
        this.isError = isError;
    }

    public boolean isError() {
        return isError;
    }
}
