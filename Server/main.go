package main

import "./events/pb"

import (
	"encoding/csv"
	"google.golang.org/protobuf/proto"
	"log"
	"net"
	"os"
	"strconv"
)

const (
	ConnHost = "172.17.171.241"
	ConnPort = "3333"
	ConnType = "tcp"
)

func main() {
	log.Println("Starting the server...")
	c := make(chan *pb.Event)
	go func() {
		for {
			message := <-c
			storeEventMessage(message)

		}
	}()
	listener, err := net.Listen(ConnType, ConnHost+":"+ConnPort)
	checkError(err)
	for {
		if conn, err := listener.Accept(); err == nil {
			go handleProtoClient(conn, c)
		} else {
			continue
		}
	}
}

func handleProtoClient(conn net.Conn, c chan *pb.Event) {
	log.Println("Connection established...")
	defer conn.Close()

	data := make([]byte, 4096)
	n, err := conn.Read(data)
	checkError(err)

	log.Println("Decoding the protobuf event message...")
	eventData := new(pb.Event)
	err = proto.Unmarshal(data[0:n], eventData)
	checkError(err)
	c <- eventData
}

func storeEventMessage(data *pb.Event) {
	Timestamp := data.GetTimestamp()
	UserId := data.GetUserId()
	Event := data.GetEvent()

	log.Println("Starting to store the event for the UserId : ", UserId)
	file, err := os.OpenFile("events.csv", os.O_RDWR|os.O_APPEND|os.O_CREATE, 0644)
	checkError(err)
	defer file.Close()

	writer := csv.NewWriter(file)
	record := []string{strconv.Itoa(int(Timestamp)), strconv.Itoa(int(UserId)), Event}
	writer.Write(record)
	writer.Flush()
	log.Println("Done storing the event for the UserId :", UserId)
}

func checkError(err error) {
	if err != nil {
		log.Println("Fatal error: ", err.Error())
		os.Exit(1)
	}
}
