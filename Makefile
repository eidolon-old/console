build: PROJECT_VERSION = $(shell sbt --error 'set showSuccess := false' project-version)
build: sbt-version
	docker-compose run --rm sbt
	tar czvf ./release/eidolon-console.tar.gz -C ./release ./eidolon
	cp ./release/eidolon-console.tar.gz ./release/eidolon-console_${PROJECT_VERSION}.tar.gz

clean:
	rm -rf ./project/target
	rm -rf ./release
	rm -rf ./target

sbt-version:
	sbt sbtVersion

.PHONY: build clean
.SILENT:
