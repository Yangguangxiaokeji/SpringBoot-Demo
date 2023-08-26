package com.foogui.bootevent.template.event;

import lombok.Data;

@Data
public class RegisterMessageEvent extends BaseEvent{
    private String messageId;
    // 这里可以定义Message对象传递,这里做了简化
    public RegisterMessageEvent(String messageId) {
        // 当然这里也调用父类的有参构造把messageId传给父类的Source，
        // 但是不建议，因为数据最后还是维护到子类本身，父类容易被子类相互覆盖
        super();
        this.messageId=messageId;
    }
}
