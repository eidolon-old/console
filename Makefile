build:
	docker-compose run --rm sbt

clean:
	rm -rf ./project/target
	rm -rf ./release
	rm -rf ./target

.PHONY: build clean
.SILENT:
