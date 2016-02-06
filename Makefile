build: PROJECT_VERSION = $(shell docker-compose run --rm sbt_project_version | tail -1)
build: sbt-version
	docker-compose run --rm sbt_build
	tar czvf ./release/eidolon-console.tar.gz -C ./release ./eidolon
	cp ./release/eidolon-console.tar.gz ./release/eidolon-console_${PROJECT_VERSION}.tar.gz

clean:
	rm -rf ./project/target
	rm -rf ./release
	rm -rf ./target

sbt-version:
	docker-compose run --rm sbt_sbt_version

.PHONY: build clean
.SILENT:
