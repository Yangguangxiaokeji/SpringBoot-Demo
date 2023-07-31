package com.foogui.dubbo.provider.dubbo;

import com.foogui.dubbo.api.api.AccountDubboService;
import com.foogui.dubbo.api.dto.AccountDTO;
import com.foogui.dubbo.provider.domain.AccountPO;
import com.foogui.dubbo.provider.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
@Slf4j
public class AccountDubboServiceImpl implements AccountDubboService {
    @Autowired
    private AccountService accountService;
    @Override
    public Boolean reduce(AccountDTO accountDTO) {

        AccountPO accountPO = new AccountPO();
        accountPO.setId(accountPO.getId());
        accountPO.setUserId(accountDTO.getUserId());
        accountPO.setAmount(accountDTO.getAmount());

        return accountService.reduce(accountPO);
    }
}
