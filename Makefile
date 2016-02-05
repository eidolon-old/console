build:
	docker-compose run --rm sbt
	tar czvf ./release/eidolon-console.tar.gz ./release/eidolon

clean:
	rm -rf ./project/target
	rm -rf ./release
	rm -rf ./target

.PHONY: build clean
.SILENT:
