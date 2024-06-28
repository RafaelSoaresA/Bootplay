package br.com.sysmap.bootcamp.appuserapi.domain.listeners;

import br.com.sysmap.bootcamp.appuserapi.domain.service.WalletService;
import br.com.sysmap.bootcamp.appuserapi.dto.WalletDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RabbitListener(queues = "WalletQueue")
public class WalletListener {

    @Autowired
    private WalletService walletService;

    @RabbitHandler
    public void process(WalletDto walletDto) {
        walletService.debit(walletDto);
        log.info("Wallet debited: {}", walletDto);
    }
}
