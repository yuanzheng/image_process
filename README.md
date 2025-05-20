# Image to Structured PDF Converter

This project aims to develop a web application that converts images into structured PDF files.
The backend interacts with an LLM (DeepSeek/Gemini) to obtain LaTeX source code for the image content
and combines it with extracted illustrations (if any) to generate the final PDF.

## Tech Stack

*   **Backend:** Spring Boot 3 + JPA + REST API
*   **Web Frontend:** Vue 3 + Vite
*   **Scripts:** Python 3.x + openCV
*   **LLM Interaction:** HTTP API (DeepSeek/Gemini)
*   **PDF Generation:** LaTeX (pdflatex command)

## Project Directories
- `frontend/`：Front-end code
- `backend/`：Back-end services code
- `backend/scripts/`：Image processing python scripts

## Phase 1: Core Backend API & Basic Web Frontend (MVP)

*   **Goal:** Establish a basic flow: image upload -> LLM interaction (mocked) -> LaTeX source display.
*   **Focus:** End-to-end communication, LLM API call validation (mocked for now).

### Task 1.1: Basic Backend Environment & API Endpoint
*   Initialize Spring Boot project.
*   Basic project structure (Controller, Service, DTOs).
*   REST Controller (`/api/v1/convert`) with POST endpoint (`/image-to-latex`).
*   File upload handling (save to temp dir).
*   Service layer with hardcoded LaTeX response.

### Task 1.2: Basic Web Frontend & Image Upload
*   Initialize Vue.js project.
*   Simple page with file upload.
*   Send image to backend API.
*   Display backend's LaTeX response.