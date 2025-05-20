# 🚀 Demo Project: Complete CI/CD Pipeline with EKS & Private DockerHub Registry

## 🔧 Technologies Used

* **CI/CD**: Jenkins, Groovy (Declarative Pipeline)
* **Containerization**: Docker, Docker Hub (private registry)
* **Orchestration**: Kubernetes (Amazon EKS)
* **Build Tools**: Java, Maven
* **Infrastructure**: AWS IAM, kubectl, AWS CLI
* **VCS**: GitHub
* **OS**: Linux (Jenkins container environment)

---

## 📦 Project Overview

This project demonstrates a full **CI/CD pipeline** for deploying a Java Maven application into a **Kubernetes cluster (EKS)** using Jenkins and Docker Hub as a private container registry. The pipeline automates everything from version bumping, building, image publishing, to production deployment and Git commit syncing.

---

## 📜 CI/CD Pipeline Breakdown

### ✅ CI Stages

1. **Increment Version**

   * Automatically bumps the semantic version of the application in `pom.xml` using `maven-build-helper` and `versions:set`.
   * Sets a version tag combining Maven version and Jenkins build number (e.g. `1.1.12-9`).

2. **Build Artifact**

   * Uses Maven to compile the Java application and package it into a `.jar` file.

3. **Build & Push Docker Image**

   * Docker image is built from the compiled artifact.
   * Image is tagged and pushed to a **private Docker Hub** repository using Jenkins credentials.

### ✅ CD Stages

4. **Deploy to Amazon EKS**

   * Uses `envsubst` to dynamically inject variables into Kubernetes YAML manifests (`deployment.yaml` and `service.yaml`).
   * Applies the manifests using `kubectl`, deploying the latest image to the EKS cluster.
   * Assumes valid `kubeconfig` is available in the Jenkins container with IAM-authenticated access.

5. **Commit Version Update**

   * Updates `pom.xml` with the new version.
   * Commits the change back to the GitHub repository using a GitHub Personal Access Token (PAT).
   * Configures Git author identity in the Jenkins job before committing.

---

## 🔐 Authentication & Security

* **Docker Hub**: Uses Jenkins `usernamePassword` credentials for private image registry login.
* **GitHub**: Uses a PAT stored in Jenkins credentials for authenticated Git push.
* **AWS**: Uses IAM credentials to authenticate and interact with the EKS cluster.

---

## 📁 Kubernetes Deployment Structure

* `kubernetes/deployment.yaml`: Defines deployment resource with `image: $DOCKER_REPO:$IMAGE_NAME`
* `kubernetes/service.yaml`: Exposes the app using a `LoadBalancer` service (EKS-compatible)
* Includes support for image pull secrets if using a private registry

---

## 📌 Summary of Pipeline Stages

| Stage Name              | Type | Description                                               |
| ----------------------- | ---- | --------------------------------------------------------- |
| `increment version`     | CI   | Bump application version using Maven helper plugins       |
| `build app`             | CI   | Compile and package the Java Maven application            |
| `build image`           | CI   | Build Docker image and push to Docker Hub (private)       |
| `deploy to K8s EKS`     | CD   | Deploy new Docker image to AWS EKS via dynamic manifests  |
| `commit version update` | CD   | Push updated `pom.xml` with bumped version back to GitHub |

---

## 📚 Module Reference

This project corresponds to **Module 11: Kubernetes on AWS – EKS** and demonstrates practical implementation of CI/CD principles with cloud-native tooling.
