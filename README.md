# 🚀 CI/CD Pipeline: Java Maven App to Amazon EKS

This project implements a full CI/CD pipeline that:

* Builds a Java Maven application
* Builds and pushes a Docker image to Docker Hub
* Deploys the application to a Kubernetes cluster (EKS)
* Commits version updates back to the GitHub repository

All automation is handled through a declarative Jenkins pipeline using Groovy.

---

## 📁 Pipeline Breakdown

### ✅ 1. **Stage: `increment version`**

* Uses `maven-build-helper` and `versions:set` to increment the application version automatically.
* Extracts the new version from `pom.xml`.
* Stores the version and Jenkins build number in an environment variable `IMAGE_NAME`.

📌 **Example Result:**

```bash
IMAGE_NAME = 1.0.3-25
```

---

### ✅ 2. **Stage: `build app`**

* Executes Maven clean and package:

```bash
mvn clean package
```

* Compiles and packages the Java app into a JAR or WAR file.

---

### ✅ 3. **Stage: `build image`**

* Builds a Docker image using the previously generated artifact.
* Uses Jenkins credentials (`docker-hub-credentials`) to log into Docker Hub securely.
* Tags the image with `DOCKER_REPO:IMAGE_NAME` and pushes it to Docker Hub.

📌 Example:

```bash
docker build -t tsemb/java-maven-app:1.0.3-25 .
```

---

### ✅ 4. **Stage: `deploy`**

* Uses AWS credentials to authenticate against EKS (or any Kubernetes cluster).
* Uses `envsubst` to replace environment variables in Kubernetes YAML templates:

  * `kubernetes/deployment.yaml`
  * `kubernetes/service.yaml`
* Applies the manifest files using `kubectl apply`.

📌 Required variables like `IMAGE_NAME` and `APP_NAME` are substituted into the templates before deployment.

---

### ✅ 5. **Stage: `commit version update`**

* Uses `github-credentials` to push back the updated `pom.xml` version into the main GitHub repository.
* Performs a standard Git workflow:

  * `git add .`
  * `git commit -m "ci: version bump"`
  * `git push origin HEAD:master`

---

## 🔐 Security and Credential Handling

* Uses `withCredentials()` to securely access:

  * Docker Hub credentials (`docker-hub-credentials`)
  * AWS IAM credentials (`aws_access_key_id`, `aws_secret_access_key`)
  * GitHub token (`github-credentials`)
* All secrets are masked and never printed in logs.

---

## 🌐 Deployment Target

* Kubernetes EKS cluster
* Assumes proper `kubeconfig` is already configured in Jenkins (or through `withKubeConfig` if using Linode)
* Kubernetes manifests are dynamically templated using `envsubst`

---

## 🧰 Technologies Used

* **Jenkins (Groovy Pipeline)**
* **Maven**
* **Docker**
* **Docker Hub**
* **Kubernetes (EKS-compatible)**
* **AWS IAM + kubectl**
* **Git + GitHub**
