#ifndef _SUMMON_HERO_
#define _SUMMON_HERO_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
    module XSanGo {
        module Protocol {

            // 召唤消耗
            struct CollectConsume {
                int type; // 消耗类型, wine(0),gold(1),rmby(2)
                int num; // 消耗数量
                int count; // 剩余次数
            };

            sequence<CollectConsume> ConsumeSeq;
            // 武将召唤View
            struct CollectHeroSoulView {
                int type; // 召唤类型, 在野武将(0),限时活动(1)
                int status; // 状态，未开始(0), 进行中(1), 结束(2)
                StringSeq heroSoulList; // 能召唤的武将列表
                ConsumeSeq consumeList; // 召唤消耗列表
                long time; // 刷新时间(在野武将)/结束时间(限时活动)
                int refreshConsume; // 刷新消耗的元宝数
            };
            
            // 召唤结果
            struct CollectHeroSoulResView {
                string templateId; // 将魂模版ID
                int num;           // 个数
                int rmbyPrice; // 元宝召唤价格
            };

            // 武将召唤接口
            interface CollectHeroSoul {
                ["ami", "amd"] string reqCollectHeroSoul() throws NoteException;
                ["ami", "amd"] string doCollectHeroSoul(int cType, int consumeType) throws NoteException, NotEnoughMoneyException;
                ["ami", "amd"] string doRefresh(int cType) throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException;
            };
        };
    };
};

#endif