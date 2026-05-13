package khe.banking.models.enums;

public enum SavingsPlan {
	BASIC(1.5),
    PREMIUM(3.0),
    HIGH_YIELD(4.5);

    private final double interestRate;

    SavingsPlan(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }
}

//SavingsPlan plan =
//SavingsPlan.PREMIUM;
//
//double rate =
//plan.getInterestRate();