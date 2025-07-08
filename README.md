# 🧪 SoftMouse.NET Automation Framework

A clean and modular automated testing framework for **SoftMouse.NET**, built using **Java**, **Selenium WebDriver**, **TestNG**, and **Maven**.  
Supports both normal and data-driven testing with logging, reporting, and failure screenshots.

---

## 🔧 Project Overview

This project enables:

- Browser-based test automation with Selenium
- TestNG-based suite execution
- Data-driven testing support
- Centralized logging via Log4j
- ExtentReports integration (optional)
- Screenshot capture on test failures

---

## 📁 Folder Structure

SoftMouse_Assessment/
├── src/ # Test scripts and automation logic
├── resources/ # Configuration files (log4j, XMLs, etc.)
├── Drivers/ # WebDriver executables (e.g., ChromeDriver)
├── Configuration/ # Custom test settings (if used)
├── .mvn/ # Maven wrapper support
├── Screenshots/ # Captured on test failures
├── target/ # Maven build output (ignored)
├── test-output/ # TestNG output reports (ignored)

Files:
├── pom.xml # Maven configuration & dependencies
├── extent-config # ExtentReports settings (if enabled)
├── log4j.properties # Logging configuration
├── testng_Normal.xml # Standard TestNG suite
├── testng_DataDriven.xml # Data-driven test suite

## ✅ Prerequisites

Ensure you have the following installed:

- Java JDK 8 or higher
- Maven
- Git
- Chrome browser (if using ChromeDriver)

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/sopnil7777/SoftMouse_Assessment.git
cd SoftMouse_Assessment
2. Build the Project
bash
Copy
Edit
mvn clean install
This will download all dependencies and compile the project.

3. Run Tests
➤ Run Normal Test Suite
bash
Copy
Edit
mvn test -DsuiteXmlFile=testng_Normal.xml
➤ Run Data-Driven Test Suite
bash
Copy
Edit
mvn test -DsuiteXmlFile=testng_DataDriven.xml
You can also run tests directly from your IDE using the TestNG plugin.

📊 Reports & Logs
TestNG Reports: Available in test-output/

ExtentReports (if configured): Advanced HTML reports

Log Files: Managed via log4j.properties

Failure Screenshots: Saved in Screenshots/

🛠 Tools & Technologies
Java

Selenium WebDriver

TestNG

Maven

Log4j

ExtentReports (optional)

🙋‍♂️ Author
Sopnil Nepal
Automation Engineer
GitHub Profile

📃 License
This project is intended for educational and internal automation purposes.

yaml
Copy
Edit

---

### ✅ Next Steps

1. Save this as `README.md` in your project root.
2. Commit and push:

```bash
git add README.md
git commit -m "docs: add polished README with setup and usage instructions"
git push
