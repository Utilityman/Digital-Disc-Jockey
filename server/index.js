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
	socket.emit('socketID', {id: socket.id});
	socket.emit('getUers', users);
	socket.broadcast.emit('newUser', {id: socket.id});
	socket.on('diconnect', function()
	{
		console.log("User Disconnected");
		socket.broadcast.emit('userConnect', {id: socket.id});
		for(var i = 0; i < users.length; i++)
		{
			if(users[i].id == socket.id)
			{
				users.splice(i, 1);
			}
		}
	});
	users.push(new user(socket.id));
});

function user(id)
{
	this.id = id;
}

