# Smart Academic & Lab Support Platform

## Cau truc thu muc de xuat

```text
src/main/java/com/example/projeck_cuoi_mon
|-- controller
|   |-- AuthController.java
|   |-- ProfileController.java
|   |-- EquipmentController.java
|   |-- MentoringController.java
|   |-- BorrowingController.java
|   `-- DashboardController.java
|-- service
|   |-- AuthService.java
|   |-- UserService.java
|   |-- EquipmentService.java
|   |-- MentoringService.java
|   |-- AcademicEvaluationService.java
|   `-- BorrowingService.java
|-- repository
|   |-- UserRepository.java
|   |-- DepartmentRepository.java
|   |-- LabRoomTypeRepository.java
|   |-- LecturerRepository.java
|   |-- EquipmentRepository.java
|   |-- MentoringSessionRepository.java
|   |-- AcademicEvaluationRepository.java
|   |-- BorrowingRecordRepository.java
|   `-- BorrowingDetailRepository.java
|-- dto
|   |-- request
|   `-- response
|-- entity
|   |-- enums
|   `-- *.java
`-- ProjeckCuoiMonApplication.java

src/main/resources
|-- templates
|   |-- auth
|   |-- profile
|   |-- equipments
|   |-- mentoring
|   |-- borrowing
|   `-- dashboard
|-- static
|   |-- css
|   |-- js
|   `-- images
`-- db
    |-- schema.sql
    `-- seed.sql
```

## Ghi chu thiet ke

- Session/Cookies xu ly dang nhap bang `HttpSession`, khong dung JWT/OAuth.
- Role chinh: `STUDENT`, `LECTURER`, `ADMIN`.
- Trang thai va role luu bang `VARCHAR` trong MySQL va map bang `EnumType.STRING`.
- Tat ca bang nghiep vu deu co `created_at`, `updated_at`.
