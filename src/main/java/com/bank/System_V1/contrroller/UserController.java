package com.bank.System_V1.contrroller;

import com.bank.System_V1.dto.*;
import com.bank.System_V1.services.impl.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
@CrossOrigin("*")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<BankResponse> createAccount(RequestEntity<UserRequest> requestEntity) {
        BankResponse BR =  userService.createAccount(requestEntity.getBody());
        if(BR.getCode().equals("002")){
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("account created", "true")
                    .body(BR);
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("account created", "false")
                    .body(BR);
        }
    }

    @GetMapping("/balanceEnquiry")
    public ResponseEntity<BankResponse> balanceEnquiry(RequestEntity<EnquiryRequest> requestEntity) {
        BankResponse br =  userService.balanceEnquiry(requestEntity.getBody());
        if(br.getCode().equals("003")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(br);
        }
        return ResponseEntity.status(HttpStatus.OK).body(br);
    }

    @GetMapping("/nameEnquiry")
    public ResponseEntity<String> nameEnquiry(@RequestParam String accountNumber) {
        String name =  userService.nameEnquiry(EnquiryRequest.builder().accountNumber(accountNumber).build());
        if(name == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(name);
    }

    @PostMapping("/creditAccount")
    public ResponseEntity<BankResponse> creditAccount(@RequestBody CreditDebitRequest req) {
        BankResponse br = userService.creditAccount(req);
        if(!(br.getCode().equals("005"))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(br);
        }
        return ResponseEntity.status(HttpStatus.OK).body(br);
    }

    @PostMapping("/debitAccount")
    public ResponseEntity<BankResponse> debitAccount(@RequestBody CreditDebitRequest req) {
        BankResponse br = userService.debitAccount(req);
        if(!(br.getCode().equals("006"))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(br);
        }
        return ResponseEntity.status(HttpStatus.OK).body(br);
    }

    @PostMapping("/Transfer")
    public ResponseEntity<BankResponse> transfer(@RequestBody TransferRequest req) {
        BankResponse br = userService.transfer(req);
        if(!(br.getCode().equals("008"))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(br);
        }
        return ResponseEntity.status(HttpStatus.OK).body(br);
    }

}
