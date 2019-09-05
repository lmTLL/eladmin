package me.zhengjie.modules.system.domain;



import java.util.Map;

/**
 * 客户接口消息发送实体
 *
 * @author groot
 * @date 2018-2-6 11:00:30
 */
public class TestMessage {

    //openid
    private String touser;

    //消息类型
    private String msgtype;

    //消息内容
    private Map<String,Object> text ;


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Map<String, Object> getText() {
        return text;
    }

    public void setText(Map<String, Object> text) {
        this.text = text;
    }
}
