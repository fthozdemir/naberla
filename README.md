# Naber' la

“Naber’la” is a chatting application using end to end encryption of messages. A simple application in java sockets which uses AES and RSA to ensure confidentiality and integrity of messages besides ensuring the authenticity with Digital Signature. Application primary goal is transferring encrypted messages between terminals as users. <br> <br>
This Java chat application is a console application that is launched from the command line. The server and clients can run on different computers in the same network, e.g. Local Area Network (LAN). There can be multiple clients connect to a server and they can chat to each other privately. <br><br>
This application creates threads for each connected user. Every individual user can communicate peer to peer. **But there is an imposter!! (I added it as per the scenario.)**  The Application demonstrate that how to avoid passive and active attacks via AES, RSA and Digital Signature.
![image](https://user-images.githubusercontent.com/25895189/132112114-4edebed8-12a8-4e3e-b677-67e24e472d02.png)

## How to install & Run

download the git clone zip and extract or clone it

```
git clone https://github.com/fthozdemir/naberla.git
```


#### Windows OS Run

for run the application go src folder and open terminal here
then run following code for running the server.

```
java ChatServer 3003
```

Than open new terminal in same src folder and run the folling code.  
```
java ChatClient localhost 3003
```
For every new user, repeat this - only ChatClient- process

-------------------------------------------
if change any code build the project with following codes in terminal

```
javac --release 8 ChatServer.java 
javac --release 8 ChatClient.java
```

## Model and Implementation
With pure asymmetric or encryption there is no way to ensure integrity and authenticity, since anyone who knows your public key can encrypt any message for you. For that you would need either a symmetric key to use for a MAC (in which case you could use it/derivatives for symmetric encryption too) or a signature from the sender. So I combined AES and RSA for message confidentiality and integrity the sign it with RSA digital signature. During development of the project there is 6 main implantation steps which given following paragraphs. <br><br>
First of all , I create ChatServer . The ChatServer class starts the server, listening on a specific port. When a new client gets connected, an instance of UserThread is created to serve that client. Since each connection is processed in a separate thread, the server is able to handle multiple clients at the same time. <br><br>
The ChatServer class has two Set collections to keep track the names and threads of the connected clients. Set is used because it doesn’t allow duplication and the order of elements does not matter. Then I create an important method sendMassage() which deliver a message from one client to other. Also there is broadcast() which deliver a message such as
joined or quitted users to the server from server to all other clients. <br><br>
The second important class that I work on is the UserThread class which extends java threads. This class is responsible for reading messages sent from the client and send messages to other clients. First, it sends a list of online users to the new user. Then it reads the username and notifies other users about the new user. <br><br>
While reading the users input, I want to add some command type inputs which users can use for interrupting to the program. This Commands defined with regex “./”. I implemented 4 commands for now: ./list returns all online users, ./bye ends the user session, ./commands returns all available commands list and ./select <username> sets the target user which you want to pier for private messaging. <br><br>
The third, the client is implemented by three classes: ChatClient, ReadThread and WriteThread. The ChatClient starts the client program, connects to a server specified by hostname/IP address and port number. Once the connection is made, it creates and starts two threads ReadThread and WriteThread. The ReadThread is responsible for reading input from the server and printing it to the console repeatedly, until the client disconnects. And the WriteThread is responsible for reading input from the user and sending it to the server, continuously until the user types ‘./bye’ to end the chat. <br><br>
The reasons for running these two threads simultaneously is that the reading operation always blocks the current thread (both reading user’s input from command line and reading server’s input via network). That means if the current thread is waiting for the user’s input, it can’t read input from the server. Therefore, two separate threads are used to make the client responsive: it can display messages from other users while reading message from the current user. 
After completing the above tasks, I managed to run a simple terminal to terminal communication application.
Then I add simple message encryption and decryption with AES algorithm with the AES class. The AES s responsible for message digest. generateSecretKey() create randomized 256 bit secret key for each user
wth Java SecretKey. The AES class has 3 main job: Message digest the users secret key, encryption and decryption. setKey() function digest the users secret key with SHA-1 which produce 160-bit hash value. Then encrypt() function using the message digest encrypts the message with Java Cipher library according to Advanced Encryption Standard(AES). Last method decrypt() do the decryption() with user’s secret Key. <br><br>
The fourth step the most fun, I created a scenario which muhittin the imposter penetrate the server and come through to read private messages. An imposter whom the user “muhittin” can see all the messaging transfer in the server with imposterCast() method. If I hadn’t applied AES encryption, muhittin could read the private information between user. But so the imposter only see the encrypted data. On the other hand ,this “imposter scenario” allows us to see the cipher Text. <br><br>
The fifth task is implementing the RSA. Each induvial user have a public key and private key. When a user ./select other, both of the users will get public key of the other. On the sender user side a method encrypt AES secret key with RSA then append it to ciphertext and send this to the receiver user. On the receiver side client decrypt the cipher message to get AES secret key then decrypt the message using AES decryption with it. <br><br>
The last one is Digital signature part. The sender will gets a cipher with own private key and the receiver will confirm the message authenticity with sender’s hash. On the other hand, I add a feature, which is about manipulating data and sending fake messages, to the imposter. In this way, I can check the digital signatures prevent muhittin the imposter or not.
