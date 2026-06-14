package khe.banking.models.records;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import khe.banking.models.Category;

public record TxnFilter(String sort, Set<Category> categories, BigDecimal minAmount, BigDecimal maxAmount, LocalDate startDate, LocalDate endDate) {

}
