package org.example.dto.response;

import lombok.Data;
import org.example.data.model.Account;

import java.util.List;

@Data
public class DepositResponse {
    private List<Account> accountsList;
}
