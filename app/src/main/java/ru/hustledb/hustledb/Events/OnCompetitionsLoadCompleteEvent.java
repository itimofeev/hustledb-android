package ru.hustledb.hustledb.Events;

public class OnCompetitionsLoadCompleteEvent {
    private final boolean isError;
    public OnCompetitionsLoadCompleteEvent(boolean isError){
        this.isError = isError;
    }

    public boolean isError() {
        return isError;
    }
}
