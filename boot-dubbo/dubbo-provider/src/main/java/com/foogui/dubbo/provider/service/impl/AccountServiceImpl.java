package com.foogui.dubbo.provider.service.impl;

import com.foogui.dubbo.provider.domain.AccountPO;
import com.foogui.dubbo.provider.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Override
    public Boolean reduce(AccountPO accountPO) {
        log.info("Reducing account successfully, reduced amount is {}", accountPO.getAmount());
        return Boolean.TRUE;
    }
}
