// language=JQuery-CSS

//AJAX Post

function sendAjaxRequest(message) {
    $.ajax({
        type: "POST",
        url: "/robot",
        data: {button: message},
        success: function (result) {
            console.log("Message sent");
            console.log(message)
        },
        error: function () {
            console.log("Dupa");
        }
    })
}

$('.control-btn').on('click', function (e) {
    e.preventDefault();
    var message = $(this).val();
    sendAjaxRequest(message)
});

//Robot moves on keys
var isKeyPressed = false;

$('body').keydown(function (e) {
    e.preventDefault();
    if (!isKeyPressed) {
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
    if (isKeyPressed) {
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