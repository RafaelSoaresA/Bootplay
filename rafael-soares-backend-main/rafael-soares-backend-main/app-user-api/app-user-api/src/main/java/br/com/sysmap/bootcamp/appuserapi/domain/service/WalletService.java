package br.com.sysmap.bootcamp.appuserapi.domain.service;


import br.com.sysmap.bootcamp.appuserapi.domain.entities.Users;
import br.com.sysmap.bootcamp.appuserapi.domain.entities.Wallet;
import br.com.sysmap.bootcamp.appuserapi.domain.enums.PointsWeek;
import br.com.sysmap.bootcamp.appuserapi.domain.repository.UsersRepository;
import br.com.sysmap.bootcamp.appuserapi.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.appuserapi.dto.AuthDto;
import br.com.sysmap.bootcamp.appuserapi.dto.WalletDto;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class WalletService {

    private final UsersService usersService;
    private final WalletRepository walletRepository;

    public void debit(WalletDto walletDto) {
        Users user = usersService.findByEmail(walletDto.getEmail());
        Wallet wallet = new Wallet();
        wallet.setBalance(wallet.getBalance().subtract(walletDto.getValue()));
    }

    //Desafio de pontos
    private Long calculatePoints(LocalDate date){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        PointsWeek points = PointsWeek.fromDayOfWeek(dayOfWeek);
        return points.getPoints();
    }

    public Wallet credit(){
        Users user = usersService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Wallet> walletOptional = this.walletRepository.findByUsers(user);

        long resultPoints = 0;

        Wallet wallet = new Wallet();
        wallet.setPoints(calculatePoints(LocalDate.now()));
        wallet.setUsers(user);
        if (walletOptional.isPresent()) {
            wallet.setId(walletOptional.get().getId());
            resultPoints = walletOptional.get().getBalance().longValue() + wallet.getPoints();
        }else{
            resultPoints = wallet.getPoints();
        }
        wallet.setBalance(BigDecimal.valueOf(resultPoints));
        wallet.setLastUpdate(LocalDateTime.now());
        return walletRepository.save(wallet);
    }

    public Wallet getWallet(){
        Users user = usersService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return walletRepository.findByUsers(user).get();
    }
}
