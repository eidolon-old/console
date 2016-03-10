build: docker_pull docker_build

clean:
	rm -rf ./project/target
	rm -rf ./release
	rm -rf ./target

docker_build: PROJECT_VERSION = $(shell docker-compose -f build-compose.yml run --rm project_version)
docker_build:
	docker-compose -f build-compose.yml run --rm sbt_build
	tar czvf ./release/eidolon-console.tar.gz -C ./release ./eidolon
	cp ./release/eidolon-console.tar.gz ./release/eidolon-console_${PROJECT_VERSION}.tar.gz

docker_pull:
	docker-compose -f build-compose.yml pull

.PHONY: build clean
.SILENT:
