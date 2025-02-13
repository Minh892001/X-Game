package com.morefun.XSanGo.pushmsg;

import java.util.List;

import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 推送消息的管理类
 * 
 * @author qinguofeng
 * */
public class XsgPushMsgManager {

    private String pushMsgs;

    private static XsgPushMsgManager instance = new XsgPushMsgManager();

    public static XsgPushMsgManager getInstance() {
        return instance;
    }

    public XsgPushMsgManager() {
        List<PushMsgT> msgList = ExcelParser.parse(PushMsgT.class);
        if (msgList != null && msgList.size() > 0) {
            this.pushMsgs = TextUtil.GSON.toJson(msgList.toArray(new PushMsgT[msgList.size()]));
        }
    }

    public String getMsgs() {
        return pushMsgs;
    }
}
