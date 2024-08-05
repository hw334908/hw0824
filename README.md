# Tool Rental Application

This application is a point-of-sale tool for a store, like Home Depot, that rents big tools. It allows customers to rent tools for a specified number of days and generates a Rental Agreement at checkout.

## Features

- **Tool Management**: Manage different types of tools with specific rental charges.
- **Checkout Process**: Calculate charges based on rental days, including special handling for weekends and holidays.
- **Discounts**: Apply discounts to the total rental charges.
- **Validation**: Ensure correct input values for rental days and discount percentages.
- **Holiday Handling**: Correctly handle holidays like Independence Day and Labor Day.
- **Comprehensive Unit Tests**: Ensure robustness of the checkout logic with various test scenarios.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/hw334908/tool-rental-app.git
    cd tool-rental-app
    ```

2. **Build the project**:
    ```bash
    mvn clean install
    ```

### Running Tests

Run the unit tests to ensure everything is working correctly:
```bash
mvn test
```

## Technical Details

The main logic for the application resides in the CheckoutService class. This class handles the checkout process, validates the input parameters, calculates the chargeable days, and generates a rental agreement.

### Tool and ToolType
The Tool and ToolType classes represent the tools and their respective properties. Tools have a toolCode, toolType, and toolBrand.

### Rental Agreement
The RentalAgreement class encapsulates the details of a rental transaction, including the tool code, type, brand, rental days, checkout date, due date, daily rental charge, charge days, pre-discount charge, discount percent, discount amount, and final charge.

### Holiday Handling
The application handles two holidays:

**Independence Day (July 4th):** 
If it falls on a weekend, it is observed on the closest weekday.

**Labor Day (First Monday in September):** 
Always observed on the first Monday of September.
