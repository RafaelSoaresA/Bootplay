package br.com.sysmap.bootcamp.appuserapi.web;


import br.com.sysmap.bootcamp.appuserapi.domain.entities.Users;
import br.com.sysmap.bootcamp.appuserapi.domain.entities.Wallet;
import br.com.sysmap.bootcamp.appuserapi.domain.service.UsersService;
import br.com.sysmap.bootcamp.appuserapi.domain.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;
    private final UsersService usersService;

    @PostMapping("/credit")
    @Operation(summary = "Credit value", description = "This function is responsible to credit value in the wallet.")
    public ResponseEntity<Wallet> credit() {
        return ResponseEntity.ok(walletService.credit());
    }

    @GetMapping("/")
    @Operation(summary = "Get wallet", description = "This function is responsible to get a wallet from an user.")
    public ResponseEntity<Wallet> getWallet() {
        Wallet wallet = walletService.getWallet();
        return ResponseEntity.ok().body(wallet);

    }
}
