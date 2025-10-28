package com.bank.System_V1.contrroller;
import java.io.FileNotFoundException;
import java.util.List;

import com.bank.System_V1.entity.Transaction;
import com.bank.System_V1.services.impl.BankStatementService;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/BankStatement")
public class TransactionController {

    @Autowired
    private BankStatementService bankStatement;

    @GetMapping("/transaction")
    public List<Transaction> generateBankStatements(@RequestParam String accountNumber,
                                                    @RequestParam String startDate,
                                                    @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }

}
