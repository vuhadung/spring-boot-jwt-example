.PHONY: docker

PWD=$(shell pwd)
VERSION?=1.1.0

default: docker

docker:
	docker build --force-rm -t docker.fortna.com/hackathon2021/hackathon-backend:$(VERSION) -f $(PWD)/Dockerfile $(PWD)
	docker tag docker.fortna.com/hackathon2021/hackathon-backend:$(VERSION) docker.fortna.com/hackathon2021/hackathon-backend:latest

publish: 
	docker push docker.fortna.com/hackathon2021/hackathon-backend:$(VERSION)
	docker push docker.fortna.com/hackathon2021/hackathon-backend:latest