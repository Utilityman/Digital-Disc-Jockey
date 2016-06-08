var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var users = [];

server.listen(8989, function()
		{
	console.log("Server is now running...");
});

io.on('connection', function(socket)
{
	console.log("User connected!");	
});
