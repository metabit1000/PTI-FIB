package main

import (
    "fmt"
    "log"
    "net/http"
    "github.com/gorilla/mux"
    "encoding/json"
    "io"
    "io/ioutil"
    "encoding/csv"
    "os"

)

type ResponseMessage struct {
    Field1 string
    Field2 string
}

type RequestMessage struct {
    Field1 string
    Field2 string
}

type Cotxe struct {
    Model string
    Venedor string
    Dies string
    Unitats string
}

func main() {

router := mux.NewRouter().StrictSlash(true)
router.HandleFunc("/", Index)
router.HandleFunc("/endpoint/", llistar)
router.HandleFunc("/endpoint2/{param}", endpointFunc2JSONInput)


log.Fatal(http.ListenAndServe(":8080", router))
}

func Index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Service OK")
}

func llistar(w http.ResponseWriter, r *http.Request) {
    var rentalsArray []Cotxe
    file, err := os.Open("/home/pau/go/rentals.csv", )
	if err != nil {
	    log.Fatal(err)
	}
	reader := csv.NewReader(file)
	for {
		line, err := reader.Read()
		if err != nil {
		    if err == io.EOF {
		        break
		    }
		    log.Fatal(err)
		}
    	rentalsArray = append(rentalsArray, Cotxe{Model: line[0], Venedor: line[1],
        Dies: line[2], Unitats: line[3]})
	}
	json.NewEncoder(w).Encode(rentalsArray)
}

func endpointFunc2JSONInput(w http.ResponseWriter, r *http.Request) {
    //var requestMessage RequestMessage
    var cotxe Cotxe
    body, err := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))
    if err != nil {
        panic(err)
    }
    if err := r.Body.Close(); err != nil {
        panic(err)
    }
    if err := json.Unmarshal(body, &cotxe); err != nil {
        w.Header().Set("Content-Type", "application/json; charset=UTF-8")
        w.WriteHeader(422) // unprocessable entity
        if err := json.NewEncoder(w).Encode(err); err != nil {
            panic(err)
        }
    } else {
    	llogarcotxe(w, []string{cotxe.Model, cotxe.Venedor, cotxe.Dies, cotxe.Unitats})
        fmt.Fprintln(w, "Successfully received request with Field1 =", cotxe.Model)
    }
}

func llogarcotxe (w http.ResponseWriter, values []string) {
    file, err := os.OpenFile("/home/pau/go/rentals.csv", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
    if err!=nil {
        json.NewEncoder(w).Encode(err)
        return
        }
    writer := csv.NewWriter(file)
    writer.Write(values)
    writer.Flush()
    file.Close()
}
