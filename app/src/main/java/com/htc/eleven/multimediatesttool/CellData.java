package com.htc.eleven.multimediatesttool;

import org.w3c.dom.Element;

/**
 * Created by eleven on 17-8-22.
 */

public class CellData {

    public static final int Playback_Test_Id = 0;
    public static final int VoiceCall_Test_Id = Playback_Test_Id + 1;
    public static final int Numbers = VoiceCall_Test_Id +1;


//    public static final String testItems[] = {"Playback", "VoiceCall"};

    private Element mElement;
    private String mId;
    private String mClass;
    private String[] subItems;

    /**
     * we plan to parse detail result items architecture here.
     * */
    public CellData(String id, String stringClass, Element element) {
        this.mId = id;
        this.mClass = stringClass;
        this.mElement = element;

        switch (mId) {
            case "1":
                subItems = new String[]{"Normal_Playback", "Headset_Playback", "A2DP_Playback"};
                break;
            case "2":
                subItems = new String[]{"Normal_Call", "Speaker_Call", "Headset_Call", "BT_Call"};
                break;
        }
    }

    public String getmId() {
        return mId;
    }

    public String getmClass() {
        return mClass;
    }

    public Element getmElement() {
        return mElement;
    }

    public String[] getSubItems() {
        return subItems;
    }
}
