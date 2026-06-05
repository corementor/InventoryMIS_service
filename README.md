# InventoryMIS_service 

A comprehensive **Financial System for Inventory Management** built with Java and Spring Boot. This system manages financial operations related to inventory including purchase orders, sales orders, and product tracking with role-based access control.

## 📋 Project Overview

**InventoryMIS_service** is an enterprise-grade inventory management system that handles financial transactions, purchase orders, and sales orders. It integrates financial accounting with inventory control, providing a complete solution for managing products, purchases, and sales with proper workflow states and audit trails.

### Key Features

- 🛒 **Product Management** - Organize products with standardized codes (PT001 format)
- 📦 **Purchase Order Management** - Track purchases with codes (PO2025-034 format)
- 💰 **Sales Order Management** - Manage sales with codes (SO2025-034 format)
- 🔐 **Role-Based Access Control** - Multiple user roles with specific permissions
- 📊 **Financial Calculations** - Tax-inclusive and tax-exclusive calculations
- 📈 **Audit Trail** - Complete history of all orders and state changes
- 🔒 **Security** - JWT-based authentication and authorization

## 🏗️ Architecture

### Tech Stack

- **Framework**: Spring Boot 3.5.6
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA
- **Authentication**: Spring Security + JWT (JSON Web Tokens)
- **JSON Processing**: Jackson
- **Additional**: Project Lombok for code simplification

### Key Dependencies

| Dependency | Purpose |
|---|---|
| Spring Boot Starter Web | REST API and Web framework |
| Spring Boot Starter Data JPA | Database persistence |
| Spring Boot Starter Security | Authentication & Authorization |
| PostgreSQL Driver | Database connectivity |
| JWT (jjwt) | Token-based authentication |
| Jackson Databind | JSON serialization/deserialization |
| Lombok | Code generation (getters, setters, constructors) |
| PsycheMesh Framework | Custom framework for core and persistence layers |

## 📊 Domain Model

### Purchase Orders

**Status States**:
- `CREATED` - Initial state when PO is created
- `SUBMITTED` - Submitted for approval
- `APPROVED` - Approved by authorized personnel
- `RETURNED` - Sent back for modifications

**Key Constraints**:
- Cannot have duplicate product types in a single purchase order
- Format: `PO{YEAR}-{SEQUENCE}` (e.g., PO2025-034)
- Subtotal excludes taxes
- Total includes taxes

**User Roles & Permissions**:
| Role | Permissions |
|---|---|
| ADMIN | Full system control |
| MANAGER | Order approval and oversight |
| SALES_OFFICER | Create and manage sales orders |
| STOCK_OFFICER | Inventory and stock management |

**Editing & Deletion Rules**:
- Edit/Delete only possible when order is in `CREATED` or `RETURNED` state
- Officers with appropriate permissions can perform these operations

### Purchase Order History

Tracks all changes and transitions:
```java
{
  purchaseOrderId: reference_to_purchase_order,
  creationDate: LocalDateTime,
  state: PurchaseOrderState[CREATED, RETURNED, SUBMITTED, APPROVED],
  userId: username_of_action_performer,
  comments: String
}
```

### Product Codes

- **Format**: PT{SEQUENCE} (e.g., PT001)
- **Purpose**: Unique identifier for product types
- **Constraint**: No duplicate product types per purchase order

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/corementor/InventoryMIS_service.git
cd InventoryMIS_service
```

2. **Configure database**
Create a PostgreSQL database and update `application.properties` or `application.yml`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_mis
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

3. **Build the project**
```bash
./mvnw clean package
```

4. **Run the application**
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 📝 API Structure

The system follows RESTful conventions with the following main entities:

- `/api/products` - Product management endpoints
- `/api/purchase-orders` - Purchase order operations
- `/api/sales-orders` - Sales order operations
- `/api/auth` - Authentication endpoints

## 🔐 Authentication

The system uses JWT (JSON Web Tokens) for stateless authentication:

1. User logs in with credentials
2. Server returns JWT token
3. Client includes token in `Authorization: Bearer <token>` header for subsequent requests
4. Server validates token and authorizes based on user role

## 📦 Project Structure

```
InventoryMIS_service/
├── src/
│   ├── main/java/io/corementor/finexp/
│   │   ├── controller/          # REST API controllers
│   │   ├── service/             # Business logic
│   │   ├── entity/              # JPA entities
│   │   ├── repository/          # Data access layer
│   │   ├── security/            # JWT and security configs
│   │   └── config/              # Application configuration
│   └── resources/
│       └── application.yml      # Application properties
├── pom.xml                      # Maven configuration
└── logs/                        # Application logs
```

## 🔄 Workflow Example: Purchase Order Lifecycle

```
1. CREATED
   └─ Officer creates PO
      
2. SUBMITTED
   └─ PO submitted for review
   
3. APPROVED or RETURNED
   ├─ APPROVED: PO accepted by manager
   └─ RETURNED: PO sent back for modifications
                (Can be edited and resubmitted)
```

## 📊 Financial Calculations

### Tax Calculations

- **Subtotal**: Sum of all line items (Tax Exclusive)
- **Total Tax Exclusive**: Same as subtotal
- **Total**: Subtotal + Tax Amount (Tax Inclusive)

### Line Item Validation

- No duplicate product types allowed in single purchase order
- Each line must have valid product code, quantity, and unit price

## 🛠️ Development Notes

Key business rules documented in `notes` file:
- Product type code validation and uniqueness constraints
- Purchase order state machine implementation
- Tax calculation logic
- User role-based access control
- Purchase order history tracking

## 📝 License

This project is part of the InventoryMIS suite. For license information, please refer to the LICENSE file.

## 👤 Author

- **corementor** - Project maintainer

## 📞 Support & Contribution

For issues, questions, or contributions, please create an issue in the GitHub repository.

---

**Last Updated**: 2026-06-05  
**Version**: 1.0.0  
**Status**: Active Development
