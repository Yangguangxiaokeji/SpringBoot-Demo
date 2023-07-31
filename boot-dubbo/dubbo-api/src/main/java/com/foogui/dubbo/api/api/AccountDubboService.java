package com.foogui.dubbo.api.api;

import com.foogui.dubbo.api.dto.AccountDTO;

/**
 * 账户RPC接口
 *
 * @author Foogui
 * @date 2023/07/31
 */
public interface AccountDubboService {
    Boolean reduce(AccountDTO accountDTO);
}
