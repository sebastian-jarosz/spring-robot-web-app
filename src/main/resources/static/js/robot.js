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
            if(message === 'run'){
                runFunction(result);
            }
            console.log(result);
        },
        error: function (result) {
            console.log("Request failed");
            console.log(result);
        }
    })
}

function runFunction(result) {
    if (result.IS_LISTENING === 'false') {
        alert("Problem połączenia z RaspberryPi");
        $('#main').show();
        $('#controlButtons').hide();
        $('#connectingInfo').hide();
        $('#runButton').show();
    } else if (result.IS_MQTT_PROCESS_RUNNING === 'false') {
        alert("RasberryPi jeszcze nie jest gotowe - proszę poczekać ok. 1 min");
        $('#main').show();
        $('#controlButtons').hide();
        $('#connectingInfo').hide();
        $('#runButton').show();
    } else {
        $('#main').show();
        $('#connectingInfo').hide();
        $('#controlButtons').show();
        $('#runButton').hide();
    }
}

$( document ).ready(function() {
    $.get({
        url: "/robot/connectionInfo",
        dataType: "json",
        success: function(result){
            if (result.IS_LISTENING === 'false' || result.IS_MQTT_PROCESS_RUNNING === 'false') {
                $('#main').show();
                $('#controlButtons').hide();
                $('#runButton').show();
            } else {
                $('#main').show();
                $('#controlButtons').show();
                $('#runButton').hide();
            }
        }
    })
});

$('.control-btn').on('click', function (e) {
    e.preventDefault();
    var message = $(this).val();
    sendAjaxRequest(message)
});

$('.run-btn').on('click', function (e) {
    e.preventDefault();
    $('#main').hide();
    $('#connectingInfo').show();
    var runMessage = $('#runButton').val();
    var stopMessage = $('#stopButton').val();
    sendAjaxRequest(runMessage);
    sendAjaxRequest(stopMessage);
});

//Robot moves on keys
var isKeyPressed = false;

$('body').keydown(function (e) {
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
});

$('body').keyup(function (e) {
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