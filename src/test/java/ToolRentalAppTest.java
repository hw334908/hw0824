import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import toolrental.constants.ToolBrand;
import toolrental.loader.ToolLoader;
import toolrental.rental.RentalAgreement;
import toolrental.service.CheckoutService;
import toolrental.tools.Tool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ToolRentalAppTest {
    private static CheckoutService checkoutService;

    @BeforeAll
    public static void setup() {
        List<Tool> tools = ToolLoader.loadTools();
        checkoutService = new CheckoutService(tools);
    }

    @Test
    public void testCheckout_WithTooLargeDiscount_ThrowsException_RequiredTestCase1() {
        String toolCode = "JAKR";
        int rentalDays = 5;
        int discountPercent = 101;
        LocalDate checkoutDate = LocalDate.parse("09/03/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate));
        assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
    }

    @Test
    public void testCheckout_WithValidLadderRental_RequiredTestCase2() {
        String toolCode = "LADW";
        int rentalDays = 3;
        int discountPercent = 10;
        LocalDate checkoutDate = LocalDate.parse("07/02/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        RentalAgreement agreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        assertNotNull(agreement);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals(ToolBrand.WERNER, agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(LocalDate.parse("07/02/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("07/05/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getDueDate());
        assertEquals(1.99, agreement.getDailyRentalCharge(), 0.01);
        assertEquals(2, agreement.getChargeDays());
        assertEquals(3.98, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(10, agreement.getDiscountPercent());
        assertEquals(0.40, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.58, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void testCheckout_withValidChainsawRental_RequiredTestCase3() {
        String toolCode = "CHNS";
        int rentalDays = 5;
        int discountPercent = 25;
        LocalDate checkoutDate = LocalDate.parse("07/02/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        RentalAgreement agreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        assertNotNull(agreement);
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals("Chainsaw", agreement.getToolType());
        assertEquals(ToolBrand.STIHL, agreement.getToolBrand());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(LocalDate.parse("07/02/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("07/07/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getDueDate());
        assertEquals(1.49, agreement.getDailyRentalCharge(), 0.01);
        assertEquals(3, agreement.getChargeDays());
        assertEquals(4.47, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(25, agreement.getDiscountPercent());
        assertEquals(1.12, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.35, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void testCheckout_WithValidJackhammerDeWaltRental_NoDiscount_RequiredTestCase4() {
        String toolCode = "JAKD";
        int rentalDays = 6;
        int discountPercent = 0;
        LocalDate checkoutDate = LocalDate.parse("09/03/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        RentalAgreement agreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        assertNotNull(agreement);
        assertEquals("JAKD", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals(ToolBrand.DEWALT, agreement.getToolBrand());
        assertEquals(6, agreement.getRentalDays());
        assertEquals(LocalDate.parse("09/03/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("09/09/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge(), 0.01);
        assertEquals(3, agreement.getChargeDays());
        assertEquals(8.97, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(8.97, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void testCheckout_WithValidJackhammerRidgidRental_NoDiscount_LongRental_RequiredTestCase5() {
        String toolCode = "JAKR";
        int rentalDays = 9;
        int discountPercent = 0;
        LocalDate checkoutDate = LocalDate.parse("07/02/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        RentalAgreement agreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals(ToolBrand.RIDGID, agreement.getToolBrand());
        assertEquals(9, agreement.getRentalDays());
        assertEquals(LocalDate.parse("07/02/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("07/11/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge(), 0.01);
        assertEquals(5, agreement.getChargeDays());
        assertEquals(14.95, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(14.95, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void testCheckout_WithValidJackhammerRidgidRental_HighDiscount_RequiredTestCase6() {
        String toolCode = "JAKR";
        int rentalDays = 4;
        int discountPercent = 50;
        LocalDate checkoutDate = LocalDate.parse("07/02/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        RentalAgreement agreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals(ToolBrand.RIDGID, agreement.getToolBrand());
        assertEquals(4, agreement.getRentalDays());
        assertEquals(LocalDate.parse("07/02/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("07/06/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy")), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge(), 0.01);
        assertEquals(1, agreement.getChargeDays());
        assertEquals(2.99, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(50, agreement.getDiscountPercent());
        assertEquals(1.50, agreement.getDiscountAmount(), 0.01);
        assertEquals(1.50, agreement.getFinalCharge(), 0.01);
    }

    // Additional Test Cases
    @Test
    public void testCheckout_WithInvalidToolCode_ThrowsException() {
        String toolCode = "INVALID";
        int rentalDays = 5;
        int discountPercent = 10;
        LocalDate checkoutDate = LocalDate.parse("07/02/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate));
        assertEquals("Invalid tool code.", exception.getMessage());
    }

    @Test
    public void testCheckout_WithNegativeRentalDays_ThrowsException() {
        String toolCode = "JAKR";
        int rentalDays = -1;
        int discountPercent = 10;
        LocalDate checkoutDate = LocalDate.parse("07/02/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate));
        assertEquals("Rental day count must be 1 or greater.", exception.getMessage());
    }

    @Test
    public void testCheckout_WithNegativeDiscount_ThrowsException() {
        String toolCode = "JAKR";
        int rentalDays = 5;
        int discountPercent = -10;
        LocalDate checkoutDate = LocalDate.parse("07/02/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate));
        assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
    }
}
