clean:
	rm -rf ./project/target
	rm -rf ./target

build:
	docker-compose run --rm sbt

.SILENT:
