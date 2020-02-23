// language=JQuery-CSS

//AJAX Post
function sendAjaxRequest(message) {
    $.ajax({
        type: "POST",
        url: "/robot",
        dataType: "json",
        data: {button: message},
        success: function (result) {
            console.log("Message sent");
            console.log(message);
            if (message === 'connect') {
                connectFunction(result);
            } else if (message === 'kill') {
                killFunction(result);
                changeSpeedFunction(result);
            } else if (message === 'speed'){
                changeSpeedFunction(result);
            }
            console.log(result);
        },
        error: function (result) {
            console.log("Request failed");
            console.log(result);
        }
    })
}

//Function executed while message is 'connect'
function connectFunction(result) {
    if (result.IS_LISTENING === 'false') {
        alert("Problem połączenia z RaspberryPi");
        $('#main').show();
        $('#connectingInfo').hide();
        $('#controlButtons').hide();
        $('#configButtons').hide();
        $('#connectButton').show();
        $('#connectionStatus').css('background-color', 'red').text('NIE POŁĄCZONO');
    } else if (result.IS_MQTT_PROCESS_RUNNING === 'false') {
        alert("RasberryPi jeszcze nie jest gotowe - proszę poczekać ok. 1 min");
        $('#main').show();
        $('#connectingInfo').hide();
        $('#controlButtons').hide();
        $('#configButtons').hide();
        $('#connectButton').show();
        $('#connectionStatus').css('background-color', 'red').text('NIE POŁĄCZONO');
    } else {
        $('#main').show();
        $('#connectingInfo').hide();
        $('#controlButtons').show();
        $('#configButtons').show();
        $('#connectButton').hide();
        $('#connectionStatus').css('background-color', 'green').text('POŁĄCZONO');
    }
}

//Function executed while message is 'kill'
function killFunction(result) {
    if (result.IS_LISTENING === 'false') {
        $('#main').show();
        $('#turningOffInfo').hide();
        $('#controlButtons').hide();
        $('#configButtons').hide();
        $('#connectButton').show();
        $('#connectionStatus').css('background-color', 'red').text('NIE POŁĄCZONO');
    }
}

function changeSpeedFunction(result){
    if(result.IS_TOP_SPEED_MODE_ON === 'true'){
        $('#speedButton').text('TRYB WOLNY').css('background-color', 'cornflowerblue')
    } else {
        $('#speedButton').text('TRYB SZYBKI').css('background-color', 'orange')
    }
}

//On load function
$(document).ready(function () {
    $.get({
        url: "/robot/connectionInfo",
        dataType: "json",
        success: function (result) {
            if (result.IS_LISTENING === 'false' || result.IS_MQTT_PROCESS_RUNNING === 'false') {
                $('#main').show();
                $('#controlButtons').hide();
                $('#configButtons').hide();
                $('#connectButton').show();
                $('#connectionStatus').css('background-color', 'red').text('NIE POŁĄCZONO');
                changeSpeedFunction(result);
            } else {
                $('#main').show();
                $('#controlButtons').show();
                $('#configButtons').show();
                $('#connectButton').hide();
                $('#connectionStatus').css('background-color', 'green').text('POŁĄCZONO');
                changeSpeedFunction(result);
            }
        }
    })
});

//Function used to send messages from proper buttons
$('.control-btn').on('click', function (e) {
    e.preventDefault();
    var message = $(this).val();
    sendAjaxRequest(message)
});

//Function used for connecting to robot
$('#connectButton').on('click', function (e) {
    e.preventDefault();
    $('#main').hide();
    $('#connectingInfo').show();
    var connectMessage = $('#connectButton').val();
    var stopMessage = $('#stopButton').val();
    sendAjaxRequest(connectMessage);
    sendAjaxRequest(stopMessage);
});

//Function used for connecting to robot
$('#killButton').on('click', function (e) {
    e.preventDefault();
    $('#configButtons').hide();
    $('#main').hide();
    $('#turningOffInfo').show();
    var killMessage = $('#killButton').val();
    sendAjaxRequest(killMessage);
});

//Robot moves on keys
var isKeyPressed = false;

$('body')
//Keyboard keys pressed function
    .keydown(function (e) {
        e.preventDefault();
        if (!isKeyPressed && $('#controlButtons').is(':visible')) {
            switch (e.code) {

                case 'KeyW':
                case 'ArrowUp':
                    console.log(e.code);
                    sendAjaxRequest('forward');
                    isKeyPressed = true;
                    break;

                case 'KeyA':
                case 'ArrowLeft':
                    console.log(e.code);
                    sendAjaxRequest('left');
                    isKeyPressed = true;
                    break;

                case 'KeyD':
                case 'ArrowRight':
                    console.log(e.code);
                    sendAjaxRequest('right');
                    isKeyPressed = true;
                    break;

                case 'KeyS':
                case 'ArrowDown':
                    console.log(e.code);
                    sendAjaxRequest('backward');
                    isKeyPressed = true;
                    break;
            }
        }
    })
    //Keyboard keys released function
    .keyup(function (e) {
        e.preventDefault();
        if (isKeyPressed && $('#controlButtons').is(':visible')) {
            switch (e.code) {

                case 'KeyW':
                case 'ArrowUp':
                    console.log("STOP");
                    sendAjaxRequest('stop');
                    isKeyPressed = false;
                    break;

                case 'KeyA':
                case 'ArrowLeft':
                    console.log("STOP");
                    sendAjaxRequest('stop');
                    isKeyPressed = false;
                    break;

                case 'KeyD':
                case 'ArrowRight':
                    console.log("STOP");
                    sendAjaxRequest('stop');
                    isKeyPressed = false;
                    break;

                case 'KeyS':
                case 'ArrowDown':
                    console.log("STOP");
                    sendAjaxRequest('stop');
                    isKeyPressed = false;
                    break;
            }
        }
    });