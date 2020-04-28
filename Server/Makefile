gen:
	protoc --proto_path=./ protobuf/event.proto --go_out=./

clean:
    rm events/pb/*.go

run:
    go run main.go
