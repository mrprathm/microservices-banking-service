@echo off
echo ============================================
echo   Microservices Banking Service - Git Push
echo   Author: Pratham Rathod
echo ============================================

echo.
echo Initializing Git repository...
git init

echo.
echo Adding all files...
git add .

echo.
echo Creating initial commit...
git commit -m "feat: Add Microservices Banking Service - Complete Spring Boot Project

Author: Pratham Rathod
Email: prathamrathod200@gmail.com
Phone: +91-9890394356
Location: Pune, Maharashtra

Services included:
- Eureka Server (Service Discovery) - Port 8761
- Config Server - Port 8888
- API Gateway - Port 8080
- Customer Service (KYC, CRUD) - Port 8081
- Account Service (Savings/Current/FD) - Port 8082
- Transaction Service (NEFT/RTGS/IMPS/UPI) - Port 8083
- Loan Service (Home/Personal/Car/Education) - Port 8084
- Notification Service (Email/SMS) - Port 8085

Tech Stack: Java 17, Spring Boot 3.2, Spring Cloud, MySQL, Hibernate, Maven"

echo.
echo Setting up remote origin...
echo Please enter your GitHub repository URL:
set /p REPO_URL="GitHub URL: "

git remote add origin %REPO_URL%
git branch -M main
git push -u origin main

echo.
echo ============================================
echo   Successfully pushed to GitHub!
echo ============================================
pause
