package com.tss.smartparking;

public class SlotsItems {
    int id;
   int slotsnumber;
    String SlotsStatus;


    public SlotsItems(int id, int slotsnumber, String slotsStatus) {
        this.id = id;
        this.slotsnumber = slotsnumber;
        SlotsStatus = slotsStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlotsnumber() {
        return slotsnumber;
    }

    public void setSlotsnumber(int slotsnumber) {
        this.slotsnumber = slotsnumber;
    }

    public String getSlotsStatus() {
        return SlotsStatus;
    }

    public void setSlotsStatus(String slotsStatus) {
        SlotsStatus = slotsStatus;
    }
}
