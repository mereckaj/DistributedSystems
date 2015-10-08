#!/usr/bin/python3
import socket
import urllib
class HTTP:
	hostName = "localhost"
	port = 8000
	scriptName = "echo.php"
	CRLF = "\r\n\r\n"

	def get(self,message):

		# Check input
		# if (" " in message == True):
		# 	# print("Can't deal with messages that contain spaces")
		# 	message.replace('+','+')

		# Create the HTTP GET request and convert it to bytes
		request = "GET /%s?message=%s HTTP/1.0%s" % (self.scriptName,message,self.CRLF)
		requestBytes = str.encode(request)

		# Create a TCP socket
		s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)

		# Set the socket time out, prevent socket already in use errors
		s.settimeout(1.0)

		# Connect the socket
		s.connect((self.hostName,self.port))

		# Send the rqeuest
		s.send(requestBytes)
		
		# Get the reponse
		response = s.recv(4096)
		reponseLines = response.split(str.encode("\r\n"))
		print(reponseLines[-1])

def getUsersInput():
	print("Enter message to send")
	msg = input()
	msg = msg.replace(" ","+")
	return msg

if(__name__ == "__main__"):
	test = HTTP()
	test.get(getUsersInput())