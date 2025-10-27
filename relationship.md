# Airbnb Backend - Project Documentation

## Overview
This is a Spring Boot-based backend application for a hotel booking system (Airbnb-like platform) built with Java 24, Spring Data JPA, PostgreSQL, and Lombok. The project implements a comprehensive booking management system with multiple entities and complex relationships.

## Technology Stack
- **Java**: 24
- **Spring Boot**: 3.5.5
- **Spring Data JPA**: For ORM and database operations
- **PostgreSQL**: Primary database
- **Lombok**: For reducing boilerplate code
- **Hibernate**: ORM provider with automatic timestamps

## Database Schema & Entity Relationships

### Entity Relationship Diagram (Textual)
```
User (1) ----< (M) Guest
User (1) ----< (M) Booking
Hotel (1) ----< (M) Room
Hotel (1) ----< (M) Booking
Hotel (1) ----< (M) Inventory
Hotel (1) ---> (1) HotelContactInfo (Embedded)
Room (1) ----< (M) Booking
Room (1) ----< (M) Inventory
Booking (M) ----< (M) Guest
Booking (1) ---> (1) Payment
```

---

## Detailed Entity Analysis

### 1. **User Entity**
**File**: `User.java`

**Purpose**: Represents system users (guests and hotel managers)

**Attributes**:
- `id` (Long, Primary Key, Auto-generated)
- `name` (String, NOT NULL)
- `email` (String, NOT NULL, UNIQUE)
- `password` (String, NOT NULL)
- `roles` (Set<Role>, EAGER fetch)

**Relationships**:
- **One-to-Many** with `Guest`: A user can have multiple guest profiles
- **One-to-Many** with `Booking`: A user can make multiple bookings

**Special Features**:
- Uses `@ElementCollection` for roles, storing them in a separate table
- Email uniqueness ensures no duplicate accounts
- Multiple roles support (GUEST, HOTEL_MANAGER)

---

### 2. **Hotel Entity**
**File**: `Hotel.java`

**Purpose**: Represents hotel properties in the system

**Attributes**:
- `id` (Long, Primary Key, Auto-generated)
- `name` (String, NOT NULL)
- `city` (String)
- `photos` (List<String>, stored as TEXT)
- `amenities` (List<String>, stored as TEXT)
- `isActive` (Boolean, NOT NULL)
- `createdAt` (LocalDateTime, auto-generated)
- `updatedAt` (LocalDateTime, auto-updated)
- `hotelContactInfo` (Embedded object)

**Relationships**:
- **One-to-Many** with `Room`: A hotel can have multiple rooms (LAZY fetch)
- **One-to-Many** with `Booking`: A hotel can have multiple bookings
- **One-to-Many** with `Inventory`: A hotel has inventory records for each room-date combination
- **Embedded** relationship with `HotelContactInfo`

**Collection Tables**:
- `hotel_photos`: Stores multiple photo URLs per hotel
- `hotel_amenities`: Stores multiple amenities per hotel

**Special Features**:
- Soft delete capability via `isActive` flag
- Automatic timestamp management
- Embedded contact information for better organization

---

### 3. **HotelContactInfo (Embeddable)**
**File**: `HotelContactInfo.java`

**Purpose**: Stores contact details for hotels (Value Object pattern)

**Attributes**:
- `address` (String)
- `phoneNumber` (String)
- `email` (String)
- `location` (String)

**Relationships**:
- **Embedded** in `Hotel` entity (no separate table)

**Special Features**:
- Uses `@Embeddable` annotation
- Fields are stored directly in the hotels table
- Promotes reusability and cleaner code organization

---

### 4. **Room Entity**
**File**: `Room.java`

**Purpose**: Represents different room types within a hotel

**Attributes**:
- `id` (Long, Primary Key, Auto-generated)
- `hotel` (Hotel, Many-to-One)
- `type` (String, NOT NULL) - e.g., "Deluxe", "Standard", "Suite"
- `basePrice` (BigDecimal, precision=10, scale=2, NOT NULL)
- `photos` (List<String>, stored as TEXT)
- `amenities` (List<String>, stored as TEXT)
- `capacity` (Integer, NOT NULL) - Max persons per room
- `totalCount` (Integer, NOT NULL) - Total rooms of this type
- `createdAt` (LocalDateTime, auto-generated)
- `updatedAt` (LocalDateTime, auto-updated)

**Relationships**:
- **Many-to-One** with `Hotel`: Each room belongs to one hotel
- **One-to-Many** with `Booking`: A room can have multiple bookings
- **One-to-Many** with `Inventory`: A room has inventory records per date

**Collection Tables**:
- `hotel_photos`: Stores room photos (shares table with hotel photos)
- `hotel_amenities`: Stores room amenities (shares table with hotel amenities)

**Special Features**:
- Base price used for calculating final price with surge factors
- Separate amenities and photos for each room type
- Capacity management for guest count validation

---

### 5. **Inventory Entity**
**File**: `Inventory.java`

**Purpose**: Manages room availability, pricing, and booking status per date (Core business logic entity)

**Attributes**:
- `id` (Long, Primary Key, Auto-generated)
- `room` (Room, Many-to-One, LAZY fetch)
- `hotel` (Hotel, Many-to-One, EAGER fetch)
- `date` (LocalDate, NOT NULL)
- `bookedCount` (Integer, NOT NULL, default=0)
- `totalCount` (Integer, NOT NULL)
- `surgeFactor` (BigDecimal, precision=5, scale=2, NOT NULL)
- `price` (BigDecimal, precision=10, scale=2, NOT NULL)
- `city` (String, NOT NULL)
- `closed` (Boolean, NOT NULL) - Room availability flag
- `createdAt` (LocalDateTime, auto-generated)
- `updatedAt` (LocalDateTime, auto-updated)

**Relationships**:
- **Many-to-One** with `Room`: Links to specific room type
- **Many-to-One** with `Hotel`: Links to specific hotel

**Constraints**:
- **Unique Constraint**: `unique_room_hotel_date` ensures one inventory record per room-hotel-date combination

**Special Features**:
- Dynamic pricing with surge factor support
- Available count = `totalCount - bookedCount`
- Per-date granularity for precise inventory management
- City denormalization for faster searches
- Closed flag for manual availability control

---

### 6. **Booking Entity**
**File**: `Booking.java`

**Purpose**: Represents customer reservations (Central transaction entity)

**Attributes**:
- `id` (Long, Primary Key, Auto-generated)
- `room` (Room, Many-to-One, LAZY fetch)
- `hotel` (Hotel, Many-to-One, LAZY fetch)
- `user` (User, Many-to-One, LAZY fetch)
- `roomCount` (Integer, NOT NULL) - Number of rooms booked
- `checkInDate` (LocalDate, NOT NULL)
- `checkOutDate` (LocalDate)
- `bookingStatus` (BookingStatus enum, NOT NULL)
- `createdAt` (LocalDateTime, auto-generated)
- `updatedAt` (LocalDateTime, auto-updated)
- `payment` (Payment, One-to-One, EAGER fetch)
- `guests` (List<Guest>, Many-to-Many, LAZY fetch)

**Relationships**:
- **Many-to-One** with `Room`: Links to specific room type
- **Many-to-One** with `Hotel`: Links to specific hotel
- **Many-to-One** with `User`: Links to booking user
- **One-to-One** with `Payment`: Each booking has one payment
- **Many-to-Many** with `Guest`: Multiple guests per booking

**Join Table**:
- `booking_guests`: Links bookings with multiple guests

**Booking Statuses**:
- `RESERVED`: Initial booking state
- `CONFIRMED`: Payment confirmed
- `CANCELLED`: Booking cancelled

**Special Features**:
- Multiple room booking support
- Guest list management for registration
- Payment integration
- Automatic timestamp tracking

---

### 7. **Guest Entity**
**File**: `Guest.java`

**Purpose**: Stores information about individuals staying at hotels

**Attributes**:
- `id` (Long, Primary Key, Auto-generated)
- `user` (User, Many-to-One)
- `name` (String, NOT NULL)
- `gender` (Gender enum)
- `age` (Integer, NOT NULL)

**Relationships**:
- **Many-to-One** with `User`: Links guest to user account
- **Many-to-Many** with `Booking`: A guest can be part of multiple bookings

**Gender Options**:
- `MALE`
- `FEMALE`
- `OTHER`

**Special Features**:
- Separate from User for handling multiple travelers
- Allows users to maintain a guest list (family, friends)
- Required for hotel registration and compliance

---

### 8. **Payment Entity**
**File**: `Payment.java`

**Purpose**: Tracks payment transactions for bookings

**Attributes**:
- `id` (Long, Primary Key, Auto-generated)
- `transactionId` (String, NOT NULL) - External payment gateway ID
- `status` (PaymentStatus enum)
- `amount` (BigDecimal, precision=10, scale=2, NOT NULL)

**Relationships**:
- **One-to-One** with `Booking`: Each payment linked to one booking

**Payment Statuses**:
- `COMPLETED`: Payment successful
- `PENDING`: Payment in progress
- `CANCELLED`: Payment cancelled/failed

**Special Features**:
- External transaction ID for payment gateway integration
- Precise decimal handling for monetary values
- Status tracking for payment lifecycle

---

## Relationship Details

### 1. **One-to-Many Relationships**

#### User → Guest
- **Type**: Unidirectional One-to-Many
- **Fetch**: Not explicitly defined (likely LAZY by default)
- **Cascade**: Not defined
- **Description**: A user can create multiple guest profiles for themselves or companions

#### User → Booking
- **Type**: Unidirectional One-to-Many (from Booking side)
- **Fetch**: LAZY
- **Cascade**: Not defined
- **Description**: A user can make multiple bookings over time

#### Hotel → Room
- **Type**: Unidirectional One-to-Many
- **Fetch**: LAZY
- **Cascade**: Not defined
- **Description**: A hotel can have multiple room types/categories

#### Hotel → Booking
- **Type**: Unidirectional One-to-Many (from Booking side)
- **Fetch**: LAZY
- **Cascade**: Not defined
- **Description**: A hotel can have multiple bookings from different users

#### Hotel → Inventory
- **Type**: Unidirectional One-to-Many (from Inventory side)
- **Fetch**: EAGER for Hotel
- **Cascade**: Not defined
- **Description**: Hotel inventory tracked per room per date

#### Room → Booking
- **Type**: Unidirectional One-to-Many (from Booking side)
- **Fetch**: LAZY
- **Cascade**: Not defined
- **Description**: A room type can be booked multiple times

#### Room → Inventory
- **Type**: Unidirectional One-to-Many (from Inventory side)
- **Fetch**: LAZY for Room
- **Cascade**: Not defined
- **Description**: Each room type has daily inventory records

### 2. **Many-to-Many Relationships**

#### Booking ↔ Guest
- **Type**: Unidirectional Many-to-Many (from Booking side)
- **Fetch**: LAZY
- **Join Table**: `booking_guests`
- **Join Columns**: 
  - `booking_id` (from Booking)
  - `guest_id` (from Guest)
- **Description**: A booking can include multiple guests, and a guest can be part of multiple bookings

### 3. **One-to-One Relationships**

#### Booking → Payment
- **Type**: Unidirectional One-to-One
- **Fetch**: EAGER
- **Join Column**: `payment_id` in Booking table
- **Description**: Each booking has exactly one payment transaction

### 4. **Embedded Relationships**

#### Hotel → HotelContactInfo
- **Type**: Embedded (Value Object)
- **Storage**: Fields stored directly in hotels table
- **Description**: Contact information is part of hotel entity, not a separate table

### 5. **Element Collections**

#### User → Roles
- **Type**: @ElementCollection
- **Fetch**: EAGER
- **Storage**: Separate table for user roles
- **Description**: Users can have multiple roles (GUEST, HOTEL_MANAGER)

#### Hotel → Photos
- **Type**: @ElementCollection
- **Collection Table**: `hotel_photos`
- **Description**: Multiple photo URLs per hotel

#### Hotel → Amenities
- **Type**: @ElementCollection
- **Collection Table**: `hotel_amenities`
- **Description**: Multiple amenities per hotel (WiFi, Pool, Gym, etc.)

#### Room → Photos
- **Type**: @ElementCollection
- **Collection Table**: `hotel_photos` (shared with hotel)
- **Description**: Multiple photo URLs per room type

#### Room → Amenities
- **Type**: @ElementCollection
- **Collection Table**: `hotel_amenities` (shared with hotel)
- **Description**: Room-specific amenities

---

## Enumerations

### 1. **Role** (`Role.java`)
Defines user access levels:
- `GUEST`: Regular users who book hotels
- `HOTEL_MANAGER`: Hotel administrators who manage properties

### 2. **BookingStatus** (`BookingStatus.java`)
Defines booking lifecycle states:
- `RESERVED`: Initial booking created
- `CONFIRMED`: Payment completed, booking confirmed
- `CANCELLED`: Booking cancelled by user or system

### 3. **PaymentStatus** (`PaymentStatus.java`)
Defines payment transaction states:
- `COMPLETED`: Payment successfully processed
- `PENDING`: Payment initiated, awaiting confirmation
- `CANCELLED`: Payment failed or cancelled

### 4. **Gender** (`Gender.java`)
Defines guest gender options:
- `MALE`
- `FEMALE`
- `OTHER`

---

## Key Design Patterns & Features

### 1. **Inventory Management System**
The Inventory entity implements a sophisticated availability tracking system:
- **Daily Granularity**: Separate records for each room-date combination
- **Dynamic Pricing**: Surge factor multiplier for demand-based pricing
- **Booking Tracking**: `bookedCount` vs `totalCount` for availability
- **Manual Control**: `closed` flag for maintenance/blocking dates

### 2. **Soft Delete Pattern**
- Hotels use `isActive` boolean for soft deletion
- Allows data retention while hiding inactive properties

### 3. **Audit Trail**
All major entities include:
- `@CreationTimestamp`: Automatic creation timestamp
- `@UpdateTimestamp`: Automatic update timestamp
- Enables tracking changes and debugging

### 4. **Value Object Pattern**
- `HotelContactInfo` as embedded entity
- Groups related fields without separate table
- Promotes cohesion and reusability

### 5. **Flexible Guest Management**
- Separation of User and Guest entities
- Users can create guest profiles for companions
- Supports family/group bookings

### 6. **Financial Precision**
- `BigDecimal` for all monetary values
- Defined precision (10,2) and scale prevents rounding errors
- Critical for payment accuracy

### 7. **Lazy Loading Strategy**
- Most relationships use LAZY fetch
- Reduces initial query overhead
- Improves performance for large datasets

### 8. **Bidirectional Flexibility**
- Most relationships are unidirectional from child to parent
- Reduces circular dependency issues
- Simplifies JSON serialization

---

## Database Tables Structure

### Main Tables:
1. **users** - User accounts
2. **hotels** - Hotel properties
3. **rooms** - Room types within hotels
4. **inventories** - Daily room availability and pricing
5. **bookings** - Customer reservations
6. **guests** - Guest information
7. **payment** - Payment transactions

### Junction/Collection Tables:
1. **booking_guests** - Links bookings to multiple guests
2. **hotel_photos** - Stores hotel and room photos
3. **hotel_amenities** - Stores hotel and room amenities
4. **user_roles** - Stores user roles (auto-generated by @ElementCollection)

---

## Business Logic Highlights

### Booking Flow:
1. User searches for hotels in a city with dates
2. System checks `Inventory` table for availability
3. User selects room and creates `Booking` (status: RESERVED)
4. User adds `Guest` details
5. User initiates `Payment`
6. Payment completed → Booking status: CONFIRMED
7. Inventory `bookedCount` incremented

### Inventory Management:
1. Hotels/Room records created
2. Daily Inventory records generated for future dates
3. Surge factors applied based on demand
4. Price calculated: `basePrice * surgeFactor`
5. Available rooms: `totalCount - bookedCount`

### Multi-Room Booking:
- `roomCount` in Booking entity
- Deducts multiple rooms from inventory
- Supports large group reservations

---

## Potential Improvements & Missing Features

### Current Gaps:
1. **No bidirectional relationships** - Consider adding for easier navigation
2. **Missing cascade operations** - Could simplify delete operations
3. **No review/rating system** - Common for hotel platforms
4. **No cancellation policy** - Business rule for refunds
5. **No loyalty program** - Points, rewards tracking
6. **No search history** - User preference tracking
7. **No notification system** - Booking confirmations, reminders
8. **No pricing history** - Track price changes over time
9. **No booking modifications** - Change dates, room count
10. **No hotel ownership** - Link hotels to hotel manager users

### Recommended Additions:
- **Review Entity**: User reviews with ratings
- **Cancellation Policy Entity**: Refund rules
- **Notification Entity**: Email/SMS tracking
- **Search History Entity**: User search patterns
- **Hotel Manager → Hotel**: Link managers to their properties
- **Booking validation**: Check-in/out date logic, capacity validation
- **Inventory generation service**: Auto-create future inventory records

---

## Setup & Configuration

### Prerequisites:
- Java 24
- PostgreSQL database
- Gradle 8.x

### Database Configuration:
Configure PostgreSQL connection in `application.properties` or `application.yml`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/airbnb_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Build & Run:
```bash
# Build project
gradlew build

# Run application
gradlew bootRun
```

---

## Project Structure
```
src/main/java/org/example/airbnbbackend/
├── AirbnbbackendApplication.java    # Main Spring Boot application
└── models/
    ├── Booking.java                  # Booking entity
    ├── Guest.java                    # Guest entity
    ├── Hotel.java                    # Hotel entity
    ├── HotelContactInfo.java         # Embedded contact info
    ├── Inventory.java                # Inventory management
    ├── Payment.java                  # Payment tracking
    ├── Room.java                     # Room types
    ├── User.java                     # User accounts
    └── enums/
        ├── BookingStatus.java        # Booking states
        ├── Gender.java               # Gender options
        ├── PaymentStatus.java        # Payment states
        └── Role.java                 # User roles
```

---

## Summary

This Airbnb backend project implements a **robust hotel booking system** with:
- **8 main entities** with complex relationships
- **4 enumeration types** for business logic
- **Inventory management** with dynamic pricing
- **Multi-guest booking** support
- **Payment integration** ready
- **Audit trails** on all major entities
- **Scalable architecture** with lazy loading

The project demonstrates solid understanding of:
- JPA/Hibernate relationships (One-to-Many, Many-to-Many, One-to-One, Embedded)
- Database normalization and denormalization
- Financial data handling
- Booking system architecture
- Multi-tenancy concepts (hotels, users, guests)

---

**Generated on**: October 27, 2025  
**Version**: 0.0.1-SNAPSHOT  
**Author**: Airbnb Backend Team

