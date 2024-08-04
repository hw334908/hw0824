package toolrental.service;

import toolrental.rental.RentalAgreement;
import toolrental.tools.Tool;
import toolrental.tools.ToolType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class CheckoutService {
    private final List<Tool> tools;

    public CheckoutService(List<Tool> tools) {
        this.tools = tools;
    }

    /**
     * Performs the checkout process and generates a rental agreement.
     *
     * @param toolCode      the code of the tool to rent
     * @param rentalDays    the number of days for the rental
     * @param discountPercent the discount percentage to apply
     * @param checkoutDate  the date of checkout
     * @return a RentalAgreement instance containing the rental details
     * @throws IllegalArgumentException if any of the input parameters are invalid
     */
    public RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) throws IllegalArgumentException {
        validateCheckoutParams(rentalDays, discountPercent);

        Tool tool = findToolByCode(toolCode);
        if (tool == null) {
            throw new IllegalArgumentException("Invalid tool code.");
        }

        ToolType toolType = tool.getToolType();
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int chargeDays = calculateChargeDays(checkoutDate, dueDate, toolType);
        double preDiscountCharge = chargeDays * toolType.getDailyCharge();
        double discountAmount = preDiscountCharge * discountPercent / 100.0;
        double finalCharge = preDiscountCharge - discountAmount;

        return new RentalAgreement(
                toolCode,
                toolType.getName(),
                tool.getToolBrand(),
                rentalDays,
                checkoutDate,
                dueDate,
                toolType.getDailyCharge(),
                chargeDays,
                roundToCents(preDiscountCharge),
                discountPercent,
                roundToCents(discountAmount),
                roundToCents(finalCharge)
        );
    }

    /**
     * Validates the checkout parameters.
     *
     * @param rentalDays    the number of days for the rental
     * @param discountPercent the discount percentage to apply
     * @throws IllegalArgumentException if rental days are less than 1 or if discount percent is not between 0 and 100
     */
    private void validateCheckoutParams(int rentalDays, int discountPercent) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
    }

    /**
     * Finds the tool by its code.
     *
     * @param toolCode the code of the tool to find
     * @return the Tool instance if found, otherwise null
     */
    private Tool findToolByCode(String toolCode) {
        return tools.stream()
                .filter(tool -> tool.getToolCode().equalsIgnoreCase(toolCode))
                .findFirst()
                .orElse(null);
    }

    /**
     * Calculates the chargeable days for the rental period.
     *
     * @param checkoutDate the date of checkout
     * @param dueDate      the due date of the rental
     * @param toolType     the type of the tool
     * @return the number of chargeable days
     */
    private int calculateChargeDays(LocalDate checkoutDate, LocalDate dueDate, ToolType toolType) {
        int chargeDays = 0;
        LocalDate currentDate = checkoutDate.plusDays(1);

        while (!currentDate.isAfter(dueDate)) {
            if (isHoliday(currentDate)) {
                if (toolType.isHolidayCharge()) {
                    chargeDays++;
                }
            } else if (isWeekDay(currentDate) && toolType.isWeekdayCharge()) {
                chargeDays++;
            } else if (isWeekend(currentDate) && toolType.isWeekendCharge()) {
                chargeDays++;
            }

            currentDate = currentDate.plusDays(1);
        }

        return chargeDays;
    }

    /**
     * Checks if the given date is a holiday.
     *
     * @param date the date to check
     * @return true if the date is a holiday, otherwise false
     */
    private boolean isHoliday(LocalDate date) {
        return isIndependenceDay(date) || isLaborDay(date);
    }

    /**
     * Checks if the given date is Independence Day or an observed holiday for Independence Day.
     *
     * @param date the date to check
     * @return true if the date is Independence Day or an observed holiday for Independence Day, otherwise false
     */
    private boolean isIndependenceDay(LocalDate date) {
        int dayOfMonth = date.getDayOfMonth();
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        if (date.getMonth() != Month.JULY) {
            return false;
        }

        if (dayOfMonth == 4 && isWeekDay(date)) {
            return true; // The actual holiday
        }

        if (dayOfMonth == 3 && dayOfWeek == DayOfWeek.FRIDAY) {
            return true; // Observed holiday if July 4th falls on Saturday
        }

        return dayOfMonth == 5 && dayOfWeek == DayOfWeek.MONDAY; // Observed holiday if July 4th falls on Sunday
    }

    /**
     * Checks if the given date is Labor Day.
     *
     * @param date the date to check
     * @return true if the date is Labor Day, otherwise false
     */
    private boolean isLaborDay(LocalDate date) {
        return date.getMonth() == Month.SEPTEMBER && date.getDayOfWeek() == DayOfWeek.MONDAY && date.getDayOfMonth() <= 7;
    }

    /**
     * Checks if the given date is a weekday.
     *
     * @param date the date to check
     * @return true if the date is a weekday, otherwise false
     */
    private boolean isWeekDay(LocalDate date) {
        return !isWeekend(date);
    }

    /**
     * Checks if the given date is a weekend.
     *
     * @param date the date to check
     * @return true if the date is a weekend, otherwise false
     */
    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    /**
     * Rounds the given amount to the nearest cent.
     *
     * @param amount the amount to round
     * @return the rounded amount
     */
    private double roundToCents(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }
}


