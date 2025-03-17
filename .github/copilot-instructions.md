# GitHub Copilot Instructions for Android (Kotlin)

## **Development Guidelines**
- Development machine is Mac
- Follow **MVVM architecture** using **Jetpack ViewModel**.
- Use **Hilt** for dependency injection.
- Prefer **Retrofit** with **Kotlin Coroutines** for network requests.
- Handle API responses with **sealed classes** or **Result wrappers**.
- Ensure **error handling** with **OkHttp Interceptors**.
- Implement **feature flagging** to enable/disable features dynamically.
- Follow **Trunk Based Development** methodology for code commits.

## **Testing Guidelines**
- Write **unit tests** using **JUnit 5** and **MockK**.
- Use **Espresso** for UI testing.
- Implement integration tests using **Android Test Orchestrator**.
- Generate code coverage reports with **JaCoCo**.
- Perform static analysis using **Lint, SonarCloud, and Detekt**.

## **Backend API & Integration**
- API endpoints are shared via **Swagger**.
- Backend follows **trunk-based deployment**, with release branches cut at sprint-end.
- Frontend leans on backend-defined API contracts and feature flags.
- API dependencies are defined **real-time** in collaboration with backend teams.



