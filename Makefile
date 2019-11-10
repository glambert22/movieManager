.DEFAULT_GOAL := help

PWD=$(shell pwd)
COMMIT_HASH=$(shell git rev-parse --short HEAD)
APP_VERSION=$(shell xmllint --xpath "//*[local-name()='project']/*[local-name()='version']/text()" pom.xml)

APP_NAME=movie-manager
JAR_FILE=target/$(APP_NAME)-$(APP_VERSION).jar
TAG=$(APP_NAME)\:$(APP_VERSION)

JAVA_11_INSTALLED=$(shell java -version 2>&1|grep 11.*.* >/dev/null; printf $$?)
MVN_DOCKER=docker run --rm\
	$(EXTRA_DOCKER_ARGS) \
	-v $(PWD):/opt/$(APP_NAME) \
	-v "$(HOME)/.m2":/root/.m2 \
	-w /opt/$(APP_NAME) \
	--name $(APP_NAME)-build \
	openjdk:11  \
	./mvnw

ifeq ($(JAVA_11_INSTALLED), 0)
	MVN=./mvnw
else
	MVN=$(MVN_DOCKER)
endif

help: ## Show help
	@awk -F ':|##' \
		'/^[^\t].+?:.*?##/ {\
			printf "\033[36m%-30s\033[0m %s\n", $$1, $$NF \
		 }' $(MAKEFILE_LIST)

app-version:
	@echo $(APP_VERSION)

commit-hash:
	@echo $(COMMIT_HASH)

set-version: ## Interactively set the version.
	$(MVN) versions:set

build: ## Build jars for all modules
	$(MVN) clean package spring-boot:repackage

test: ## Run unit and integration tests for Java artifacts
	$(MVN) surefire:test failsafe:integration-test

run-unit-tests: ## Run unit tests for Java artifacts
	$(MVN) surefire:test

run-integration-tests: ## Run integration tests for Java artifacts
	$(MVN) failsafe:integration-test

run: ## Run the app
	$(MVN) clean spring-boot:run

clean: ## Clean build artifacts for all modules
	$(MVN) clean

deploy: ## Publish Java artifact
	$(MVN) deploy -DskipTests=true

install: ## Install artifact to local repository
	$(MVN) install -DskipTests=true

docker/build: build ## Build Docker image
	docker build --build-arg JAR_FILE=$(JAR_FILE) -t $(APP_NAME) .

docker/build-nc: ## Build the container without caching
    docker build --build-arg JAR_FILE=$(JAR_FILE) --no-cache -t $(APP_NAME) .

docker/run: ## Run the application in tagged container
    docker run -d -p9001:9001 -t $(APP_NAME)

