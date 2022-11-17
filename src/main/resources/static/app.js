let stompClientAlexov = null;
let stompClientJohn = null;
let stompClientMark = null;
let accessTokenAlexov = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIQVkhIiwic3ViIjoiQXV0aFRva2VuIiwiaWQiOjEsImlhdCI6MTY2MDQ0NjM4MSwiZXhwIjoxNjYwNDQ5OTgxfQ.snevOxpfmxkW05CeBuMoBK73ynbOJUl9Zwep60fFaWg";
let accessTokenJohn = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIQVkhIiwic3ViIjoiQXV0aFRva2VuIiwiaWQiOjIsImlhdCI6MTY2MDQ0NjQwNywiZXhwIjoxNjYwNDUwMDA3fQ.RQRVPlHgHobns1ND7is9fHETjVZZ6lfqG6WNNgtNA30";
let accessTokenMark = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIQVkhIiwic3ViIjoiQXV0aFRva2VuIiwiaWQiOjMsImlhdCI6MTY1ODc0MjE4NiwiZXhwIjoxNjU4NzQ1Nzg2fQ.nhaGKXTld1Jr1MRUAYXLNclJG67X1ZyyFFZvS5IakTM";
$(function () {
    $( "#connect-user-alexov" ).click(function() {
        connect_alexov();
    });

    $( "#send-first-alexov" ).click(function() {
        sendMessageAlexov($( "#name-recipient-alexov" ).val(), $( "#content-alexov" ).val(), $("#id-chat-alexov").val());
    });

    $( "#send-post-alexov" ).click(function() {
        sendPostAlexov($( "#content-post-alexov" ).val() );
    });

    $( "#update-wall-alexov" ).click(function() {
        updateWallAlexov();
    });

    $( "#connect-user-john" ).click(function() {
        connect_john();
    });
    $( "#send-first-john" ).click(function() {
        sendFirstMessageJohn($( "#name-recipient-john" ).val(), $( "#content-john" ).val() );
    });
    $( "#send-next-john" ).click(function() {
        sendNewMessageJohn($( "#id-chat-john" ).val(), $( "#content-john" ).val() );
    });
    $( "#send-post-john" ).click(function() {
        sendPostJohn($( "#content-post-john" ).val() );
    });

    $( "#connect-user-mark" ).click(function() {
        connectMark();
    });
    $( "#send-first-mark" ).click(function() {
        sendFirstMessageMark($( "#name-recipient-mark" ).val(), $( "#content-john" ).val() );
    });
    $( "#send-next-mark" ).click(function() {
        sendNewMessageMark($( "#id-chat-mark" ).val(), $( "#content-john" ).val() );
    });
    $( "#send-post-mark" ).click(function() {
        sendPostMark($( "#content-post-mark" ).val() );
    });
});

//-----------------------------------------user alexov
function connect_alexov() {
    var socket = new SockJS('/ruchat');
    stompClientAlexov = Stomp.over(socket);
    stompClientAlexov.onmessage = function (msg) {
        console.log("Message stomp : " + msg.data);
    }
    stompClientAlexov.connect(
        {access_token : accessTokenAlexov}, function (frame) {
        console.log('Connected: ' + frame);
        stompClientAlexov.subscribe('/user/alexov/new/chatRoom' , function (chatroom) {
            newChatRoomAlexov(chatroom);
		});
        stompClientAlexov.subscribe('/user/alexov/queue/message', function (message) {
            console.log(message);
		});
        stompClientAlexov.subscribe('/user/alexov/new/post', function (message) {
            console.log(message);
        });
    });
}

function newChatRoomAlexov(message) {
    html_before_id = "<div class='chatroom' style='border: solid black 1px'> <div class='id'> id = ";
    html_after_id = "</div> <div class='name'> name = "
    html_after_name = "</div> <div class='date'> date = "
    html_after_date = "</div></div>";
    chatRoom = JSON.parse(JSON.stringify(message));
    chatJson = JSON.parse(chatRoom["body"]);
    console.log(chatJson);
    $( "#chatrooms-alexov" ).html($("#chatrooms-alexov")[0].outerHTML + html_before_id + chatJson["id"] + html_after_id + chatJson["chatName"] + html_after_name + chatJson["dateLastUpdate"] + html_after_date);
}

function sendMessageAlexov(recipientName, contentAlexov, chatId) {
    path ="/app/sendMessage";
    if ($("#content-message-alexov")[0].files[0] == null) {
        stompClientAlexov.send(path, {}, JSON.stringify({
            'recipientName': recipientName,
            'content': contentAlexov,
            'chatId': chatId,
        }));
    } else {
        file2 = $("#content-message-alexov")[0].files[0];
        let reader2 = new FileReader();
        reader2.readAsDataURL(file2);
        reader2.onload = function () {
            stompClientAlexov.send(path, {}, JSON.stringify({
                'recipientName': recipientName,
                'content': contentAlexov,
                'chatId': chatId,
                'media' : reader2.result
            }));
        }
    }

}

function updateWallAlexov() {
    console.log(111);
    var x = new XMLHttpRequest();
    x.open("GET", "/post/v1.0/userPosts/" + accessTokenAlexov, true);
    x.onload = function () {
        console.log(x.responseText);
        $("#posts-alexov").html(x.responseText);
    }
    x.onerror = function (message) {
        console.log(message);
    }
}

function sendPostAlexov(postContent) {
    path = "/app/new/post";
    stompClientAlexov.send(path, {}, JSON.stringify({
        'content': postContent
    }));
}


function sendPostAlexovWithImg(postContent) {
    path = "/app/new/post";
    file = $("#source_file")[0].files[0];
    let reader1 = new FileReader();
    reader1.readAsDataURL(file);
    reader1.onload = function () {
        file2 = $("#source_file2")[0].files[0];
        let reader2 = new FileReader();
        reader2.readAsDataURL(file2);
        reader2.onload = function () {
            stompClientAlexov.send(path, {}, JSON.stringify({
                'content': postContent,
                'imageList' : [reader1.result]
            }));
        }
    };
}

//-----------------------------------------user john
function connect_john() {
    let socket = new SockJS('/ruchat');
    stompClientJohn = Stomp.over(socket);
    stompClientJohn.connect(
        {access_token : accessTokenJohn},
        function (frame) {
            console.log('Connected: ' + frame);
            stompClientJohn.subscribe('/user/john/new/chatRoom' , function (chatroom) {
                newChatRoomJohn(chatroom);
            });
            stompClientJohn.subscribe('/user/john/queue/message', function (message) {
                console.log(message);
            });
            stompClientJohn.subscribe('/user/alexov/new/post', function (message) {
                console.log(message);
            });
        });
    stompClientJohn.onmessage = function (msg) {
        console.log("Message : " + msg.data);
    }
}

function sendFirstMessageJohn(recipientName, content_m) {
    path ="/app/firstMessage/" + recipientName;
    stompClientJohn.send(path, {}, JSON.stringify({
        'content': content_m,
    }));
}

function sendNewMessageJohn(idChat, content_m) {
    path ="/app/newMessage";
    stompClientJohn.send(path, {}, JSON.stringify({
        'chatId': idChat,
        'content': content_m,
    }));
}

function sendPostJohn(postContent) {
    path = "/app/new/post";
    stompClientJohn.send(path, {}, JSON.stringify({
        'content': postContent
    }));
}

function newChatRoomJohn(message) {
    html_before_id = "<div class='chatroom' style='border: solid black 1px'> <div class='id'> id = ";
    html_after_id = "</div> <div class='name'> name = "
    html_after_name = "</div> <div class='date'> date = "
    html_after_date = "</div></div>";
    chatRoom = JSON.parse(JSON.stringify(message));
    chatJson = JSON.parse(chatRoom["body"]);
    console.log(chatJson);
    $( "#chatrooms-john" ).html($("#chatrooms-john")[0].outerHTML + html_before_id + chatJson["id"] + html_after_id + chatJson["chatName"] + html_after_name + chatJson["dateLastUpdate"] + html_after_date);
}

//-----------------------------------------user mark
function connectMark() {
    let socket = new SockJS('/ruchat');
    stompClientMark = Stomp.over(socket);
    stompClientMark.connect(
        {access_token : accessTokenMark},
        function (frame) {
            console.log('Connected: ' + frame);
            stompClientMark.subscribe('/user/mark/new/chatRoom' , function (chatroom) {
                newChatRoomMark(chatroom);
            });
            stompClientMark.subscribe('/user/mark/queue/message', function (message) {
                console.log(message);
            });
        });
    stompClientMark.onmessage = function (msg) {
        console.log("Message : " + msg.data);
    }
}

function sendFirstMessageMark(recipientName, content_m) {
    path ="/app/firstMessage/" + recipientName;
    stompClientMark.send(path, {}, JSON.stringify({
        'content': content_m,
    }));
}

function sendNewMessageMark(idChat, content_m) {
    path ="/app/newMessage";
    stompClientMark.send(path, {}, JSON.stringify({
        'chatId': idChat,
        'content': content_m,
    }));
}

function sendPostMark(postContent) {
    path = "/app/new/post";
    stompClientMark.send(path, {}, JSON.stringify({
        'content': postContent
    }));
}

function newChatRoomMark(message) {
    html_before_id = "<div class='chatroom' style='border: solid black 1px'> <div class='id'> id = ";
    html_after_id = "</div> <div class='name'> name = "
    html_after_name = "</div> <div class='date'> date = "
    html_after_date = "</div></div>";
    chatRoom = JSON.parse(JSON.stringify(message));
    chatJson = JSON.parse(chatRoom["body"]);
    console.log(chatJson);
    $( "#chatrooms-mark" ).html($("#chatrooms-mark")[0].outerHTML + html_before_id + chatJson["id"] + html_after_id + chatJson["chatName"] + html_after_name + chatJson["dateLastUpdate"] + html_after_date);
}