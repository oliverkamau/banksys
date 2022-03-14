$(document).ready(function () {

    saveUser();
})


function deleteuser() {
    console.log($('#email').val())
    var formData = new FormData();
    formData.append('email',$('#email').val())
    $.ajax({
        type: 'GET',
        url: 'deleteuser',
        data: formData,
        processData: false,
        contentType: false
    }).done(function (s) {

    }).fail(function (xhr, error) {
        // bootbox.alert(xhr.responseText)
        swal({
            type: 'error',
            title: 'Error!',
            text: xhr.responseText,
            showConfirmButton: true

        })
    })
}

function saveUser(){
    $('#register').click(function () {
        var cpass = $('#confirmPass').val();
        var pass = $('#password').val();

        if ($('#name').val() === '') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Name Field is required!',
                confirmButtonText: 'OK'
            })
        } else if ($('#email').val() === '') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Email Field is required!',
                confirmButtonText: 'OK'
            })
        } else if ($('#username').val() === '') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Username Field is required!',
                confirmButtonText: 'OK'
            })
        } else if ($('#password').val() === '') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Password Field is required!',
                confirmButtonText: 'OK'
            })
        } else if ($('#confirmPass').val() === '') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Confirm Password Field is required!',
                confirmButtonText: 'OK'
            })
        } else if (cpass !== pass) {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Password and confirm password do not match!',
                confirmButtonText: 'OK'
            })
        }
        else {
            var form = $("#register-form")[0];
            var data = new FormData(form);
            $.ajax({
                type: 'POST',
                url: 'registerUser',
                data: data,
                processData: false,
                contentType: false
            }).done(function (s) {
                swal({
                    type: 'success',
                    title: 'Success',
                    text: s.success,
                    showConfirmButton: true
                })
            }).fail(function (xhr, error) {
                // bootbox.alert(xhr.responseText)
                swal({
                    type: 'error',
                    title: 'Error!',
                    text: xhr.responseText,
                    showConfirmButton: true

                })
                // deleteuser();
            })
        }


    })
}